package com.eerussianguy.beneath.common.blockentities;

import java.util.Arrays;
import java.util.function.BiConsumer;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.HellforgeBlock;
import com.eerussianguy.beneath.common.blocks.HellforgeSideBlock;
import com.eerussianguy.beneath.common.container.HellforgeContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blockentities.BellowsBlockEntity;
import net.dries007.tfc.common.blockentities.TickableInventoryBlockEntity;
import net.dries007.tfc.common.blocks.devices.CharcoalForgeBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTraits;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Fuel;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.IntArrayBuilder;
import net.dries007.tfc.util.calendar.ICalendarTickable;

public class HellforgeBlockEntity extends TickableInventoryBlockEntity<ItemStackHandler> implements ICalendarTickable, MenuProvider
{
    public static void serverTick(Level level, BlockPos pos, BlockState state, HellforgeBlockEntity forge)
    {
        forge.checkForLastTickSync();
        forge.checkForCalendarUpdate();

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        if (forge.needsRecipeUpdate)
        {
            forge.needsRecipeUpdate = false;
            forge.updateCachedRecipes();
        }

        if (level.getGameTime() % 20 == 0)
        {
            // Slurp in charcoal or other fuel.
            final AABB bounds = new AABB(pos.getX() - 1, pos.getY() + 0.875, pos.getZ() - 1, pos.getX() + 2, pos.getY() + 1.25, pos.getZ() + 2);
            Helpers.gatherAndConsumeItems(level, bounds, forge.inventory, 0, ITEM_SLOTS - 1);

            int heatLevel = state.getValue(CharcoalForgeBlock.HEAT) > 0 ? Mth.clamp((int) (forge.temperature / Heat.maxVisibleTemperature() * 6) + 1, 1, 7) : 0; // scaled 1 through 7
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    final BlockState theState = level.getBlockState(cursor.setWithOffset(pos, x, 0, z));
                    if (theState.hasProperty(CharcoalForgeBlock.HEAT) && heatLevel != theState.getValue(CharcoalForgeBlock.HEAT))
                    {
                        level.setBlockAndUpdate(cursor, theState.setValue(CharcoalForgeBlock.HEAT, heatLevel));
                        forge.markForSync();
                    }
                }
            }

        }

        final boolean isRaining = level.isRainingAt(pos) && !level.dimensionType().ultraWarm();
        if (state.getValue(CharcoalForgeBlock.HEAT) > 0)
        {
            if (isRaining && level.random.nextFloat() < 0.15F)
            {
                Helpers.playSound(level, pos, SoundEvents.LAVA_EXTINGUISH);
            }

            // Update fuel
            if (forge.burnTicks > 0)
            {
                forge.burnTicks -= forge.airTicks > 0 || isRaining ? 2 : 1; // Fuel burns twice as fast using bellows, or in the rain
            }

        }

        // Always update temperature / cooking, until the fire pit is not hot anymore
        if (forge.temperature > 0 || forge.burnTemperature > 0)
        {
            forge.temperature = HeatCapability.adjustDeviceTemp(forge.temperature, forge.burnTemperature, forge.airTicks, isRaining);

            cursor.setWithOffset(pos, 0, 1, 0);
            HeatCapability.provideHeatTo(level, cursor, forge.temperature);

            forge.forEachSlot((stack, slot) -> {
                stack.getCapability(HeatCapability.CAPABILITY).ifPresent(cap -> {
                    // Update temperature of item
                    float itemTemp = cap.getTemperature();
                    if (forge.temperature > itemTemp)
                    {
                        HeatCapability.addTemp(cap, forge.temperature);
                    }
                });

                final CachedTransformation ct = forge.cachedRecipes[slot];
                if (ct != null && ct.predicate.test(forge, stack, slot))
                {
                    ct.consumer.accept(forge, stack, slot);
                }
            }, false);
            forge.markForSync();
        }
        if (forge.burnTicks <= 0 && state.getValue(CharcoalForgeBlock.HEAT) > 0)
        {
            forge.extinguish();
        }

        if (forge.burnTemperature > 0 && state.getValue(CharcoalForgeBlock.HEAT) == 0)
        {
            forge.extinguish();
        }
        if (forge.airTicks > 0)
        {
            forge.airTicks--;
        }

    }

    public static void createFromCharcoalPile(Level level, BlockPos pos)
    {
        pos = pos.immutable();
        level.setBlockAndUpdate(pos, BeneathBlocks.HELLFORGE.get().defaultBlockState().setValue(HellforgeBlock.HEAT, 2));
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                if (!(x == 0 && z == 0))
                {
                    level.setBlockAndUpdate(pos.offset(x, 0, z), BeneathBlocks.HELLFORGE_SIDE.get().defaultBlockState().setValue(HellforgeSideBlock.HEAT, 2));
                }
            }
        }
        level.getBlockEntity(pos, BeneathBlockEntities.HELLFORGE.get()).ifPresent(HellforgeBlockEntity::onFirstCreation);
    }

    public static final int ITEM_SLOTS = 18;
    public static final int SLOT_EXTRA_MIN = 18;
    public static final int SLOT_EXTRA_MAX = 21;

    protected final ContainerData syncableData;
    private final CachedTransformation[] cachedRecipes = new CachedTransformation[ITEM_SLOTS];
    private float temperature; // Current Temperature
    private int burnTicks; // Ticks remaining on the current item of fuel
    private float burnTemperature; // Temperature provided from the current item of fuel
    private int airTicks; // Ticks of air provided by bellows
    private long lastPlayerTick; // Last player tick this forge was ticked (for purposes of catching up)
    private boolean needsRecipeUpdate; // Set to indicate on tick, the cached recipes need to be re-updated


    public HellforgeBlockEntity(BlockPos pos, BlockState state)
    {
        super(BeneathBlockEntities.HELLFORGE.get(), pos, state, defaultInventory(SLOT_EXTRA_MAX + 1), Beneath.blockEntityName("hellforge"));

        temperature = 0;
        burnTemperature = 0;
        burnTicks = 0;
        airTicks = 0;
        lastPlayerTick = Integer.MIN_VALUE;
        syncableData = new IntArrayBuilder().add(() -> (int) temperature, value -> temperature = value);

        if (TFCConfig.SERVER.charcoalForgeEnableAutomation.get())
        {
//            sidedInventory
//                .on(new PartialItemHandler(inventory).insert(SLOT_FUEL_MIN, 1, 2, 3, SLOT_FUEL_MAX), Direction.UP)
//                .on(new PartialItemHandler(inventory).insert(SLOT_INPUT_MIN, 6, 7, 8, SLOT_INPUT_MAX), Direction.Plane.HORIZONTAL)
//                .on(new PartialItemHandler(inventory).extract(SLOT_INPUT_MIN, 6, 7, 8, SLOT_INPUT_MAX), Direction.DOWN);
        }

        Arrays.fill(cachedRecipes, null);
    }

    public void intakeAir(int amount)
    {
        airTicks += amount;
        if (airTicks > BellowsBlockEntity.MAX_DEVICE_AIR_TICKS)
        {
            airTicks = BellowsBlockEntity.MAX_DEVICE_AIR_TICKS;
        }
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player)
    {
        return HellforgeContainer.create(this, inventory, containerId);
    }

    @Override
    public void onCalendarUpdate(long ticks)
    {
        assert level != null;
        final BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(CharcoalForgeBlock.HEAT) != 0)
        {
            final HeatCapability.Remainder remainder = HeatCapability.consumeFuelForTicks(ticks, inventory, burnTicks, burnTemperature, 0, ITEM_SLOTS - 1);

            burnTicks = remainder.burnTicks();
            burnTemperature = remainder.burnTemperature();

            if (remainder.ticks() > 0)
            {
                // Consumed all fuel, so extinguish and cool instantly
                extinguish();
                forEachSlot((stack, slot) -> stack.getCapability(HeatCapability.CAPABILITY).ifPresent(cap -> cap.setTemperature(0f)), false);
            }
        }
    }

    @Override
    public long getLastCalendarUpdateTick()
    {
        return lastPlayerTick;
    }

    @Override
    public void setLastCalendarUpdateTick(long tick)
    {
        lastPlayerTick = tick;
    }

    public ContainerData getSyncableData()
    {
        return syncableData;
    }

    public float getTemperature()
    {
        return temperature;
    }

    public int getAirTicks()
    {
        return airTicks;
    }

    public void onFirstCreation()
    {
        burnTicks = 200;
        burnTemperature = 500;
        markForSync();
    }

    @Override
    public void loadAdditional(CompoundTag nbt)
    {
        temperature = nbt.getFloat("temperature");
        burnTicks = nbt.getInt("burnTicks");
        airTicks = nbt.getInt("airTicks");
        burnTemperature = nbt.getFloat("burnTemperature");
        lastPlayerTick = nbt.getLong("lastPlayerTick");
        super.loadAdditional(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag nbt)
    {
        nbt.putFloat("temperature", temperature);
        nbt.putInt("burnTicks", burnTicks);
        nbt.putInt("airTicks", airTicks);
        nbt.putFloat("burnTemperature", burnTemperature);
        nbt.putLong("lastPlayerTick", lastPlayerTick);
        super.saveAdditional(nbt);
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        super.setAndUpdateSlots(slot);
        if (slot >= 0 && slot < ITEM_SLOTS)
        {
            singleRecipeUpdate(slot);
        }
        else
        {
            updateCachedRecipes();
        }
    }

    @Override
    public int getSlotStackLimit(int slot)
    {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        if (slot < ITEM_SLOTS)
        {
            return Helpers.mightHaveCapability(stack, HeatCapability.CAPABILITY) || Fuel.get(stack) != null;
        }
        else
        {
            return Helpers.mightHaveCapability(stack, Capabilities.FLUID_ITEM, HeatCapability.CAPABILITY);
        }
    }

    public boolean light()
    {
        assert level != null;
        for (int i = 0; i < ITEM_SLOTS; i++)
        {
            final ItemStack stack = inventory.getStackInSlot(i);
            if (Fuel.get(stack) != null)
            {
                final CachedTransformation ct = CachedTransformation.get(stack);
                if (ct != null)
                {
                    ct.consumer.accept(this, stack, i);

                    final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
                    for (int x = -1; x <= 1; x++)
                    {
                        for (int z = -1; z <= 1; z++)
                        {
                            final BlockState state = level.getBlockState(cursor.setWithOffset(worldPosition, x, 0, z));
                            if (state.hasProperty(HellforgeBlock.HEAT))
                            {
                                level.setBlockAndUpdate(cursor, state.setValue(HellforgeBlock.HEAT, 2));
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void extinguish()
    {
        assert level != null;
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                final BlockPos pos = worldPosition.offset(x, 0, z);
                final BlockState theState = level.getBlockState(pos);
                if (theState.hasProperty(HellforgeBlock.HEAT))
                {
                    level.setBlockAndUpdate(pos, theState.setValue(HellforgeBlock.HEAT, 0));
                }
            }
        }
        burnTicks = 0;
        burnTemperature = 0;
        markForSync();
    }

    public void updateCachedRecipes()
    {
        forEachSlot((stack, slot) -> cachedRecipes[slot] = CachedTransformation.get(stack), false);
    }

    public void singleRecipeUpdate(int slot)
    {
        cachedRecipes[slot] = CachedTransformation.get(inventory.getStackInSlot(slot));
    }

    private void forEachSlot(BiConsumer<ItemStack, Integer> function, boolean testEmptyStacks)
    {
        for (int i = 0; i < ITEM_SLOTS; i++)
        {
            final ItemStack stack = inventory.getStackInSlot(i);
            if (testEmptyStacks || !stack.isEmpty())
            {
                function.accept(stack, i);
            }
        }
    }

    private void handleInputMelting(HeatingRecipe recipe, ItemStack stack, int startIndex)
    {
        stack.getCapability(HeatCapability.CAPABILITY).ifPresent(cap -> {
            // Handle possible metal output
            final ItemStackInventory inventory = new ItemStackInventory(stack);
            FluidStack fluidStack = recipe.assembleFluid(inventory);
            ItemStack outputStack = recipe.assemble(inventory, null);
            float itemTemperature = cap.getTemperature();

            // Loop through all input slots
            for (int slot = SLOT_EXTRA_MIN; slot <= SLOT_EXTRA_MAX; slot++)
            {
                fluidStack = Helpers.mergeOutputFluidIntoSlot(this.inventory, fluidStack, itemTemperature, slot);
                if (fluidStack.isEmpty()) break;
            }

            FoodCapability.applyTrait(outputStack, FoodTraits.CHARCOAL_GRILLED);
            this.inventory.setStackInSlot(startIndex, outputStack);
            markForSync();
        });
    }

    private void consumeFuel(Fuel fuel, int slot)
    {
        // Try and consume a piece of fuel
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        burnTicks += fuel.getDuration();
        burnTemperature = fuel.getTemperature();
        markForSync();
    }

    private boolean isTurnedOn()
    {
        return getBlockState().hasProperty(CharcoalForgeBlock.HEAT) && getBlockState().getValue(CharcoalForgeBlock.HEAT) > 0;
    }

    private record CachedTransformation(TriConsumer<HellforgeBlockEntity, ItemStack, Integer> consumer, TriPredicate<HellforgeBlockEntity, ItemStack, Integer> predicate)
    {
        @Nullable
        static CachedTransformation get(ItemStack stack)
        {
            final Fuel fuel = Fuel.get(stack);
            if (fuel != null)
            {
                return create(fuel);
            }
            final HeatingRecipe recipe = HeatingRecipe.getRecipe(stack);
            if (recipe != null)
            {
                return create(recipe);
            }
            return null;
        }

        static CachedTransformation create(HeatingRecipe recipe)
        {
            return new CachedTransformation((forge, stack, slot) -> forge.handleInputMelting(recipe, stack, slot), (forge, stack, slot) -> stack.getCapability(HeatCapability.CAPABILITY).map(cap -> recipe.isValidTemperature(cap.getTemperature())).orElse(false));
        }

        static CachedTransformation create(Fuel fuel)
        {
            return new CachedTransformation((forge, stack, slot) -> forge.consumeFuel(fuel, slot), (forge, stack, slot) -> forge.burnTicks <= 0 && forge.isTurnedOn());
        }

    }
}
