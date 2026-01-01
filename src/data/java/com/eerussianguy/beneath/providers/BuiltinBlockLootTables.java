package com.eerussianguy.beneath.providers;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import net.dries007.tfc.util.loot.IsIsolatedCondition;

import static net.minecraft.world.level.storage.loot.entries.LootItem.*;

public class BuiltinBlockLootTables extends BlockLootSubProvider implements Accessors
{
    public BuiltinBlockLootTables(HolderLookup.Provider provider)
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate()
    {
        add(Blocks.NETHERRACK, createSelfDropDispatchTable(
            Blocks.NETHERRACK,
            () -> IsIsolatedCondition.INSTANCE,
            applyExplosionDecay(BeneathBlocks.NETHER_PEBBLE, lootTableItem(BeneathBlocks.NETHER_PEBBLE).apply(setCount(2, 4)))
        ));
    }

    // todo remove, this is to just let it generate while i'm writing it
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output)
    {
        this.generate();
        Set<ResourceKey<LootTable>> set = new HashSet<>();

        for (Block block : getKnownBlocks())
        {
            if (block.isEnabled(this.enabledFeatures))
            {
                ResourceKey<LootTable> resourcekey = block.getLootTable();
                if (resourcekey != BuiltInLootTables.EMPTY && set.add(resourcekey) && map.containsKey(resourcekey))
                {
                    LootTable.Builder loottable$builder = this.map.remove(resourcekey);
                    output.accept(resourcekey, loottable$builder);
                }
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return super.getKnownBlocks();
//        return BeneathBlocks.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).filter(block -> !(block instanceof LiquidBlock)).toList();
    }

    private static LootItemConditionalFunction.Builder<?> setCount(int count)
    {
        return SetItemCountFunction.setCount(ConstantValue.exactly(count));
    }

    private static LootItemConditionalFunction.Builder<?> setCount(int min, int max)
    {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

}
