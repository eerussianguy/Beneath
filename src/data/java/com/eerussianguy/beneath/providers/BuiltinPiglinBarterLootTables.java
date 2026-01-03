package com.eerussianguy.beneath.providers;

import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;

import static net.minecraft.world.level.storage.loot.entries.LootItem.*;

public class BuiltinPiglinBarterLootTables implements LootTableSubProvider
{
    private final HolderLookup.Provider myRegistries;

    public BuiltinPiglinBarterLootTables(HolderLookup.Provider myRegistries)
    {
        this.myRegistries = myRegistries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output)
    {
        output.accept(BuiltInLootTables.PIGLIN_BARTERING, LootTable.lootTable().withPool(
            LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(weight(Items.ENDER_PEARL, 1, 2, 4))
                .add(weight(Items.STRING, 2, 3, 9))
                .add(weight(TFCItems.BURLAP_CLOTH, 1, 2, 4))
                .add(weight(Items.QUARTZ, 2, 5, 12))
                .add(weight(Blocks.OBSIDIAN, 1))
                .add(weight(Items.CRYING_OBSIDIAN, 1, 1, 3))
                .add(weight(Items.LEATHER, 2, 2, 4))
                .add(weight(Items.SADDLE, 1))
                .add(weight(Items.NETHER_BRICK, 2, 8, 12))
                .add(weight(Items.ARROW, 2, 6, 12))
                .add(weight(Items.SPECTRAL_ARROW, 2, 6, 12))
                .add(weight(TFCItems.GLOW_ARROW, 2, 6, 12))
                .add(weight(rockBlock(Rock.BASALT, Rock.BlockType.GRAVEL), 1, 8, 16))
                .add(weight(Blocks.BLACKSTONE, 1, 8, 16))
                .add(weight(TFCItems.POWDERS.get(Powder.FLUX), 2, 4, 12))
        ));
    }

    private ItemLike rockBlock(Rock rock, Rock.BlockType type)
    {
        return TFCBlocks.ROCK_BLOCKS.get(rock).get(type);
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight, int min, int max)
    {
        return weight(item, weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight, int value)
    {
        return weight(item, weight).apply(SetItemCountFunction.setCount(ConstantValue.exactly(value)));
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight)
    {
        return lootTableItem(item).setWeight(weight);
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item)
    {
        return lootTableItem(item);
    }
}
