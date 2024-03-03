package com.eerussianguy.beneath.common.blocks;

import java.util.List;
import java.util.function.Supplier;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.misc.NCropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.ClimateRange;

public abstract class NetherCropBlock extends CropBlock
{
    private final SoulFarmlandBlockEntity.NutrientType nutrient;

    protected NetherCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Item> seeds, SoulFarmlandBlockEntity.NutrientType primaryNutrient)
    {
        super(properties, maxAge, () -> Blocks.AIR, seeds, FarmlandBlockEntity.NutrientType.NITROGEN, () -> ClimateRange.NOOP);
        this.nutrient = primaryNutrient;
    }

    public SoulFarmlandBlockEntity.NutrientType getRestoreNutrient()
    {
        return nutrient;
    }

    @Override
    public abstract IntegerProperty getAgeProperty();

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return NCropUtil.useFertilizer(level, player, hand, pos.below()) ? InteractionResult.SUCCESS : super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return Helpers.isBlock(level.getBlockState(pos.below()), BeneathBlocks.SOUL_FARMLAND.get());
    }

    @Override
    public void die(Level level, BlockPos blockPos, BlockState blockState, boolean byPlayer)
    {

    }

    @Override
    public void growthTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        if (!level.isClientSide() && NCropUtil.growthTick(level, pos, state, crop))
        {
            this.postGrowthTick(level, pos, state, crop);
        }
    }

    @Override
    public void addHoeOverlayInfo(Level level, BlockPos pos, BlockState state, List<Component> text, boolean isDebug)
    {
        final ClimateRange range = this.climateRange.get();
        final BlockPos sourcePos = pos.below();
        text.add(FarmlandBlock.getTemperatureTooltip(level, pos, range, false));
        SoulFarmlandBlockEntity farmland = null;
        if (level.getBlockEntity(sourcePos) instanceof SoulFarmlandBlockEntity found)
        {
            farmland = found;
        }
        else
        {
            if (level.getBlockEntity(sourcePos.below()) instanceof SoulFarmlandBlockEntity found)
            {
                farmland = found;
            }
        }

        if (farmland != null)
        {
            farmland.addTooltipInfo(text);
        }

        if (level.getBlockEntity(pos) instanceof CropBlockEntity crop)
        {
            if (isDebug)
            {
                text.add(Component.literal(String.format("[Debug] Growth = %.4f Yield = %.4f Expiry = %.4f Last Tick = %d Delta = %d", crop.getGrowth(), crop.getYield(), crop.getExpiry(), crop.getLastGrowthTick(), Calendars.get(level).getTicks() - crop.getLastGrowthTick())));
            }
            if (crop.getGrowth() >= 1.0F)
            {
                text.add(Component.translatable("tfc.tooltip.farmland.mature"));
            }
        }

    }

    @Override
    @Deprecated
    public FarmlandBlockEntity.NutrientType getPrimaryNutrient()
    {
        return super.getPrimaryNutrient();
    }
}
