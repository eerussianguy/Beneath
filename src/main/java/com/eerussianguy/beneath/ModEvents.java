package com.eerussianguy.beneath;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import com.eerussianguy.beneath.common.BeneathDispenserBehaviors;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.misc.BeneathClimateModels;
import com.eerussianguy.beneath.mixin.BlockEntityTypeAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.IForgeBlockExtension;
import net.dries007.tfc.common.blocks.wood.Wood;

public class ModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ModEvents::setup);
        bus.addListener(BeneathEntities::onAttributes);
    }

    private static void setup(FMLCommonSetupEvent event)
    {
        Beneath.LOGGER.debug("Beneath Common Setup");
        event.enqueueWork(() -> {
            BeneathDispenserBehaviors.registerDispenseBehaviors();
            BeneathBlocks.registerFlowerPotFlowers();
            BeneathEntities.onSpawnPlacement();
            BeneathClimateModels.registerModels();

            modifyFlammability();
            modifyBlockEntityTypes();
        });
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
        modifyWood(TFCBlockEntities.CHEST.get(), Wood.BlockType.CHEST);
        modifyWood(TFCBlockEntities.CHEST.get(), Wood.BlockType.TRAPPED_CHEST);
        modifyWood(TFCBlockEntities.LOOM.get(), Wood.BlockType.LOOM);
        modifyWood(TFCBlockEntities.TICK_COUNTER.get(), Wood.BlockType.SAPLING);
        modifyWood(TFCBlockEntities.BARREL.get(), Wood.BlockType.BARREL);
        modifyWood(TFCBlockEntities.SLUICE.get(), Wood.BlockType.SLUICE);
        modifyWood(TFCBlockEntities.BOOKSHELF.get(), Wood.BlockType.BOOKSHELF);
        modifyWood(TFCBlockEntities.TOOL_RACK.get(), Wood.BlockType.TOOL_RACK);
        modifyWood(TFCBlockEntities.SIGN.get(), Wood.BlockType.SIGN);
        modifyWood(TFCBlockEntities.SIGN.get(), Wood.BlockType.WALL_SIGN);
        modifyWood(TFCBlockEntities.LECTERN.get(), Wood.BlockType.LECTERN);
    }

    private static void modifyWood(BlockEntityType<?> type, Wood.BlockType blockType)
    {
        modifyBlockEntityType(type, BeneathBlocks.WOODS.values().stream().map(map -> map.get(blockType).get()));
    }

    private static void modifyBlockEntityType(BlockEntityType<?> type, Stream<Block> extraBlocks)
    {
        Beneath.LOGGER.debug("Modifying block entity type: " + ForgeRegistries.BLOCK_ENTITIES.getKey(type));
        Set<Block> blocks = ((BlockEntityTypeAccessor) (Object) type).accessor$getValidBlocks();
        blocks = new HashSet<>(blocks);
        blocks.addAll(extraBlocks.toList());
        ((BlockEntityTypeAccessor) (Object) type).accessor$setValidBlocks(blocks);
    }
}
