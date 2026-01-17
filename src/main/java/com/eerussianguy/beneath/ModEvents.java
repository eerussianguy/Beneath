package com.eerussianguy.beneath;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import com.eerussianguy.beneath.common.BeneathDispenserBehaviors;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.eerussianguy.beneath.misc.BeneathClimateModels;
import com.eerussianguy.beneath.misc.BeneathInteractionManager;
import com.eerussianguy.beneath.mixin.BlockEntityTypeAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.IForgeBlockExtension;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.capabilities.ItemCapabilities;

public class ModEvents
{
    public static void init(IEventBus bus)
    {
        bus.addListener(ModEvents::setup);
        bus.addListener(ModEvents::onCaps);
        bus.addListener(BeneathEntities::onAttributes);
        bus.addListener(BeneathEntities::onSpawnPlacement);
    }

    public static void onCaps(RegisterCapabilitiesEvent event)
    {
        registerInventory(event, BeneathBlockEntities.HELLFORGE);
        registerInventory(event, BeneathBlockEntities.ANCIENT_ALTAR);

        event.registerItem(Capabilities.FluidHandler.ITEM, ItemCapabilities::forBucket, BeneathItems.JUICER);
    }

    private static void registerInventory(RegisterCapabilitiesEvent event, Supplier<? extends BlockEntityType<? extends InventoryBlockEntity<?>>> type)
    {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(), InventoryBlockEntity::getSidedInventory);
    }

    private static void setup(FMLCommonSetupEvent event)
    {
        Beneath.LOGGER.debug("Beneath Common Setup");
        event.enqueueWork(() -> {
            BeneathDispenserBehaviors.registerDispenseBehaviors();
            BeneathBlocks.registerFlowerPotFlowers();
            BeneathClimateModels.registerModels();

            modifyFlammability();
            modifyBlockEntityTypes();

            for (Stem stem : Stem.VALUES)
            {
                BlockSetType.register(stem.getBlockSet());
                WoodType.register(stem.getVanillaWoodType());
            }
        });

        BeneathInteractionManager.init();
    }

    private static void modifyFlammability()
    {
        BeneathBlocks.WOODS.values().stream().flatMap(map -> map.values().stream()).forEach(reg -> {
            if (reg.get() instanceof IForgeBlockExtension extension)
            {
                extension.getExtendedProperties().flammable(0, 0);
            }
        });
    }

    private static void modifyBlockEntityTypes()
    {
        modifyWood(TFCBlockEntities.TICK_COUNTER.get(), Wood.BlockType.SAPLING);
        modifyWood(TFCBlockEntities.CHEST.get(), Wood.BlockType.CHEST);
        modifyWood(TFCBlockEntities.TRAPPED_CHEST.get(), Wood.BlockType.TRAPPED_CHEST);
        modifyWood(TFCBlockEntities.LOOM.get(), Wood.BlockType.LOOM);
        modifyWood(TFCBlockEntities.BARREL.get(), Wood.BlockType.BARREL);
        modifyWood(TFCBlockEntities.SLUICE.get(), Wood.BlockType.SLUICE);
        modifyWood(TFCBlockEntities.BOOKSHELF.get(), Wood.BlockType.BOOKSHELF);
        modifyWood(TFCBlockEntities.TOOL_RACK.get(), Wood.BlockType.TOOL_RACK);
        modifyWood(TFCBlockEntities.LECTERN.get(), Wood.BlockType.LECTERN);
        modifyWood(TFCBlockEntities.AXLE.get(), Wood.BlockType.AXLE);
        modifyWood(TFCBlockEntities.BLADED_AXLE.get(), Wood.BlockType.BLADED_AXLE);
        modifyWood(TFCBlockEntities.WATER_WHEEL.get(), Wood.BlockType.WATER_WHEEL);
        modifyWood(TFCBlockEntities.WINDMILL.get(), Wood.BlockType.WINDMILL);
    }

    private static void modifyWood(BlockEntityType<?> type, Wood.BlockType... blockType)
    {
        modifyBlockEntityType(type, BeneathBlocks.WOODS.values().stream().flatMap(map -> Arrays.stream(blockType).map(map::get).map(Supplier::get)));
    }

    private static void modifyBlockEntityType(BlockEntityType<?> type, Stream<Block> extraBlocks)
    {
        Beneath.LOGGER.debug("Modifying block entity type: " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type));
        Set<Block> blocks = ((BlockEntityTypeAccessor) (Object) type).accessor$getValidBlocks();
        blocks = new HashSet<>(blocks);
        blocks.addAll(extraBlocks.toList());
        ((BlockEntityTypeAccessor) (Object) type).accessor$setValidBlocks(blocks);
    }
}
