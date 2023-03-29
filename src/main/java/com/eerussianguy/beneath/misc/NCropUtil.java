package com.eerussianguy.beneath.misc;

import java.util.Random;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.common.blocks.NetherCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;

public final class NCropUtil
{
    public static final long UPDATE_INTERVAL = 2 * ICalendar.TICKS_IN_DAY;

    public static final float GROWTH_FACTOR = 1f / (24 * ICalendar.TICKS_IN_DAY);
    public static final float NUTRIENT_CONSUMPTION = 1f / (12 * ICalendar.TICKS_IN_DAY);
    public static final float NUTRIENT_GROWTH_FACTOR = 0.5f;
    public static final float GROWTH_LIMIT = 1f;
    public static final float EXPIRY_LIMIT = 2f;
    public static final float YIELD_MIN = 0.2f;
    public static final float YIELD_LIMIT = 1f;

    /**
     * @return {@code true} if the crop survived.
     */
    public static boolean growthTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        final long firstTick = crop.getLastGrowthTick(), thisTick = Calendars.SERVER.getTicks();
        long tick = firstTick + UPDATE_INTERVAL, lastTick = firstTick;
        for (; tick < thisTick; tick += UPDATE_INTERVAL)
        {
            if (!growthTickStep(level, pos, state, level.getRandom(), lastTick, tick, crop))
            {
                return false;
            }
            lastTick = tick;
        }
        return lastTick >= thisTick || growthTickStep(level, pos, state, level.getRandom(), lastTick, thisTick, crop);
    }

    public static boolean growthTickStep(Level level, BlockPos pos, BlockState state, Random random, long fromTick, long toTick, CropBlockEntity crop)
    {
        // Calculate invariants
        final ICalendar calendar = Calendars.get(level);
        final BlockPos sourcePos = pos.below();
        final long tickDelta = toTick - fromTick;

        final NetherCropBlock cropBlock = (NetherCropBlock) state.getBlock();
        final boolean growing = level.dimensionType().ultraWarm();

        // Nutrients are consumed first, since they are independent of growth or health.
        // As long as the crop exists it consumes nutrients.

        final SoulFarmlandBlockEntity.NutrientType primaryNutrient = cropBlock.getRestoreNutrient();
        float nutrientsAvailable = 0, nutrientsRequired = NUTRIENT_CONSUMPTION * tickDelta, nutrientsConsumed = 0;
        if (level.getBlockEntity(sourcePos) instanceof SoulFarmlandBlockEntity farmland)
        {
            for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
            {
                if (primaryNutrient == type)
                {
                    farmland.addNutrient(type, nutrientsRequired);
                }
                else
                {
                    nutrientsAvailable += farmland.getNutrient(type);
                    nutrientsConsumed += farmland.consumeNutrient(type, nutrientsRequired);
                }

            }
        }

        final float growthModifier = TFCConfig.SERVER.cropGrowthModifier.get().floatValue(); // Higher = Slower growth
        final float expiryModifier = TFCConfig.SERVER.cropExpiryModifier.get().floatValue(); // Higher = Slower expiry
        final float localExpiryLimit = EXPIRY_LIMIT * expiryModifier * (1f / growthModifier);

        // Total growth is based on the ticks and the nutrients consumed. It is then allocated to actual growth or expiry based on other factors.
        final float totalGrowthDelta = (1f / growthModifier) * Helpers.uniform(random, 0.9f, 1.1f) * tickDelta * GROWTH_FACTOR + nutrientsConsumed * NUTRIENT_GROWTH_FACTOR;
        final float initialGrowth = crop.getGrowth();
        float remainingGrowthDelta = totalGrowthDelta;
        float growth = initialGrowth, expiry = crop.getExpiry(), actualYield = crop.getYield();

        // Re-scale expiry to within our imaginary limits
        expiry *= localExpiryLimit / EXPIRY_LIMIT;

        final float growthLimit = cropBlock.getGrowthLimit(level, pos, state);
        if (remainingGrowthDelta > 0 && level.dimensionType().ultraWarm() && growth < growthLimit)
        {
            // Allocate to growth
            final float delta = Math.min(remainingGrowthDelta, growthLimit - growth);

            growth += delta;
            remainingGrowthDelta -= delta;
        }
        if (remainingGrowthDelta > 0)
        {
            // Allocate remaining growth to expiry
            final float delta = Math.min(remainingGrowthDelta, localExpiryLimit - expiry);

            expiry += delta;
        }

        // Calculate yield, which depends both on a flat rate per growth, and on the nutrient satisfaction, which is a measure of nutrient consumption over the growth time.
        final float growthDelta = growth - initialGrowth;
        final float nutrientSatisfaction;
        if (growthDelta <= 0 || nutrientsRequired <= 0)
        {
            nutrientSatisfaction = 1; // Either condition causes the below formula to result in NaN
        }
        else
        {
            nutrientSatisfaction = Math.min(1, (totalGrowthDelta / growthDelta) * (nutrientsAvailable / nutrientsRequired));
        }

        actualYield += growthDelta * Helpers.lerp(nutrientSatisfaction, YIELD_MIN, YIELD_LIMIT);

        // Check if the crop should've expired.
        if (expiry >= localExpiryLimit || !growing)
        {
            // Lenient here - instead of assuming it expired at the start of the duration, we assume at the end. Including growth during this period.
            cropBlock.die(level, pos, state, growth >= 1);
            return false;
        }

        // Re-scale expiry to constant values to maintain invariance if the config value is updated
        expiry *= EXPIRY_LIMIT / localExpiryLimit;

        crop.setGrowth(growth);
        crop.setYield(actualYield);
        crop.setExpiry(expiry);
        crop.setLastGrowthTick(calendar.getTicks());

        return true;
    }

    public static boolean useFertilizer(Level level, Player player, InteractionHand hand, BlockPos farmlandPos)
    {
        final ItemStack stack = player.getItemInHand(hand);
        final NetherFertilizer fertilizer = NetherFertilizer.get(stack);
        if (fertilizer != null && level.getBlockEntity(farmlandPos) instanceof SoulFarmlandBlockEntity farmland)
        {
            if (!level.isClientSide())
            {
                int repeat = -1;
                if (player.isShiftKeyDown())
                {
                    for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
                    {
                        repeat = minAmountRequiredToNextFillBar(farmland, fertilizer, type, repeat);
                    }
                    repeat = Math.min(repeat, stack.getCount());
                }
                if (repeat == -1)
                {
                    repeat = 1; // By default, we consume 1
                }
                for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
                {
                    if (fertilizer.getNutrient(type) == 0 || farmland.getNutrient(type) == 1)
                    {
                        return false;
                    }
                }

                addNutrients(farmland, fertilizer, repeat);
                if (!player.isCreative()) stack.shrink(repeat);

                addNutrientParticles((ServerLevel) level, farmlandPos.above(), fertilizer);
                Helpers.playSound(level, farmlandPos, TFCSounds.FERTILIZER_USE.get());

            }
            return true;
        }
        return false;
    }

    public static void addNutrientParticles(ServerLevel level, BlockPos pos, NetherFertilizer fertilizer)
    {
        for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
        {
            final SimpleParticleType particle = switch(type) {
                case DEATH -> BeneathParticles.DEATH.get();
                case DESTRUCTION -> BeneathParticles.DESTRUCTION.get();
                case DECAY -> BeneathParticles.DECAY.get();
                case SORROW -> BeneathParticles.SORROW.get();
                case FLAME -> BeneathParticles.FLAME.get();
            };
            final float amount = fertilizer.getNutrient(type);
            for (int i = 0; i < (int) (amount > 0 ? Mth.clamp(amount * 10, 1, 5) : 0); i++)
            {
                level.sendParticles(particle, pos.getX() + level.random.nextFloat(), pos.getY() + level.random.nextFloat() / 5D, pos.getZ() + level.random.nextFloat(), 0, 0D, 0D, 0D, 1D);
            }
        }
    }

    private static void addNutrients(SoulFarmlandBlockEntity farmland, NetherFertilizer fertilizer, float multiplier)
    {
        for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
        {
            farmland.addNutrient(type, fertilizer.getNutrient(type) * multiplier);
        }
    }

    private static int minAmountRequiredToNextFillBar(SoulFarmlandBlockEntity farmland, NetherFertilizer fertilizer, SoulFarmlandBlockEntity.NutrientType type, int prevValue)
    {
        if (fertilizer.getNutrient(type) > 0 && farmland.getNutrient(type) < 1)
        {
            final int requiredValue = Mth.ceil((1 - farmland.getNutrient(type)) / fertilizer.getNutrient(type));
            if (prevValue == -1 || requiredValue < prevValue)
            {
                return requiredValue;
            }
        }
        return prevValue;
    }

}
