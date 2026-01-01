package com.eerussianguy.beneath.providers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.BeneathMineral;
import com.eerussianguy.beneath.common.blocks.BeneathOre;
import com.eerussianguy.beneath.common.blocks.NCrop;
import com.eerussianguy.beneath.common.blocks.NetherCropBlock;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.SequentialEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.loot.IsIsolatedCondition;

import static net.minecraft.world.level.storage.loot.LootPool.*;
import static net.minecraft.world.level.storage.loot.LootTable.*;
import static net.minecraft.world.level.storage.loot.entries.LootItem.*;
import static net.minecraft.world.level.storage.loot.predicates.ExplosionCondition.*;
import static net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition.hasBlockStateProperties;

public class BuiltinBlockLootTables extends BlockLootSubProvider implements Accessors
{
    private final LootItemCondition.Builder HAS_SHEARS_LIKE = MatchTool.toolMatches(ItemPredicate.Builder.item().of(commonTagOf(Registries.ITEM, "tools/shear")));
    private final LootItemCondition.Builder HAS_SHARP_TOOL = matchesTool(TFCTags.Items.TOOLS_SHARP);

    public BuiltinBlockLootTables(HolderLookup.Provider provider)
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate()
    {
        final HolderLookup.RegistryLookup<Enchantment> enchants = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        // VANILLA

        addIsolated(Blocks.NETHERRACK, BeneathBlocks.NETHER_PEBBLE);
        addIsolated(Blocks.CRIMSON_NYLIUM, BeneathBlocks.NETHER_PEBBLE);
        addIsolated(Blocks.WARPED_NYLIUM, BeneathBlocks.NETHER_PEBBLE);
        addIsolated(Blocks.BASALT, rock(Rock.BASALT, Rock.BlockType.LOOSE));
        addIsolated(Blocks.BLACKSTONE, BeneathBlocks.BLACKSTONE_PEBBLE);

        add(Blocks.GOLD_BLOCK, createSingleItemTable(BeneathItems.GOLD_CHUNK, UniformGenerator.between(3, 6)));
        add(Blocks.GILDED_BLACKSTONE, lootTable()
            .withPool(lootPool().add(lootTableItem(Blocks.BLACKSTONE)))
            .withPool(lootPool().add(lootTableItem(BeneathItems.GOLD_CHUNK)).when(chance(0.25f)))
        );

        createGrass(Blocks.CRIMSON_ROOTS, BeneathItems.CRIMSON_STRAW, BeneathItems.SEEDS.get(NCrop.CRIMSON_ROOTS));
        createGrass(Blocks.WARPED_ROOTS, BeneathItems.WARPED_STRAW, BeneathItems.SEEDS.get(NCrop.WARPED_ROOTS));

        add(Blocks.BONE_BLOCK, createSingleItemTable(Items.BONE_MEAL, UniformGenerator.between(1, 3)));
        add(Blocks.LANTERN, lootTable().withPool(lootPool().add(SequentialEntry.sequential(
            lootTableItem(TFCItems.LAMP_GLASS),
            lootTableItem(metal(Metal.WROUGHT_IRON, Metal.BlockType.BARS))
        ))));
        add(Blocks.SOUL_LANTERN, lootTable().withPool(lootPool().add(SequentialEntry.sequential(
            lootTableItem(TFCItems.LAMP_GLASS),
            lootTableItem(metal(Metal.WROUGHT_IRON, Metal.BlockType.BARS))
        ))));
        final Block gravel = rock(Rock.BASALT, Rock.BlockType.GRAVEL);
        add(Blocks.GRAVEL, createSilkTouchDispatchTable(gravel, applyExplosionCondition(gravel,
            lootTableItem(Items.FLINT)
                .when(
                    BonusLevelTableCondition.bonusLevelFlatChance(enchants.getOrThrow(Enchantments.FORTUNE), 0.1F, 0.14285715F, 0.25F, 1.0F)
                )
                .otherwise(lootTableItem(gravel))
        )));
        add(Blocks.NETHER_WART, lootTable().withPool(lootPool().add(SequentialEntry.sequential(
            lootTableItem(BeneathItems.SEEDS.get(NCrop.NETHER_WART)).apply(setCount(1, 3)),
            lootTableItem(Items.NETHER_WART).when(hasProperty(Blocks.NETHER_WART, NetherWartBlock.AGE, NetherWartBlock.MAX_AGE))
        ))));

        // Beneath

        BeneathBlocks.GRADED_ORES.forEach((ore, map) -> map.forEach((grade, block) -> {
            if (ore == BeneathOre.NETHER_GOLD)
                dropOther(block.get(), TFCItems.GRADED_ORES.get(Ore.NATIVE_GOLD).get(grade));
        }));
        BeneathBlocks.MINERALS.forEach((mineral, block) -> {
            if (mineral == BeneathMineral.NETHER_PYRITE)
                dropOther(block.get(), TFCItems.ORES.get(Ore.PYRITE));
            else if (mineral == BeneathMineral.BLACKSTONE_SYLVITE)
                dropOther(block.get(), TFCItems.ORES.get(Ore.PYRITE));
            else if (mineral == BeneathMineral.NETHER_CURSECOAL)
                dropOther(block.get(), BeneathItems.CURSECOAL);
        });
        dropOther(BeneathBlocks.SLIMED_NETHERRACK.get(), BeneathItems.RAW_SLIME);

        add(BeneathBlocks.HAUNTED_SPIKE.get(), lootTable().withPool(lootPool().add(lootTableItem(BeneathBlocks.NETHER_PEBBLE).apply(setCount(1, 2)))));
        add(BeneathBlocks.GLOWSTONE_SPIKE.get(), lootTable().withPool(lootPool().add(lootTableItem(Items.GLOWSTONE_DUST).apply(setCount(1, 2)))));

        add(BeneathBlocks.NETHER_PEBBLE.get(), b -> lootTable().withPool(lootPool().add(
            lootTableItem(b)
                .apply(setCount(2)).when(hasProperty(b, LooseRockBlock.COUNT, 2))
                .apply(setCount(3)).when(hasProperty(b, LooseRockBlock.COUNT, 3))
        ).when(survivesExplosion())));
        add(BeneathBlocks.BLACKSTONE_PEBBLE.get(), b -> lootTable().withPool(lootPool().add(
            lootTableItem(b)
                .apply(setCount(2)).when(hasProperty(b, LooseRockBlock.COUNT, 2))
                .apply(setCount(3)).when(hasProperty(b, LooseRockBlock.COUNT, 3))
        ).when(survivesExplosion())));

        dropOther(BeneathBlocks.SULFUR.get(), TFCItems.ORE_POWDERS.get(Ore.SULFUR));

        // todo yield?
        BeneathBlocks.CROPS.forEach((crop, block) -> {
            add(block.get(), b -> lootTable().withPool(
                lootPool().when(hasProperty(b, NetherCropBlock.AGE, crop.getStages() - 1))
            ).withPool(
                lootPool().add(lootTableItem(BeneathItems.SEEDS.get(crop)))
            ));
        });
        dropSelf(BeneathBlocks.GLEAMFLOWER.get());
        dropSelf(BeneathBlocks.BURPFLOWER.get());
        dropSelf(BeneathBlocks.UNPOSTER.get());
        dropSelf(BeneathBlocks.COBBLERACK.get());
        dropSelf(BeneathBlocks.FUNGAL_COBBLERACK.get());
        dropSelf(BeneathBlocks.WARPED_THATCH.get());
        dropSelf(BeneathBlocks.CRIMSON_THATCH.get());
        dropSelf(BeneathBlocks.HELLBRICKS.get());
        dropOther(BeneathBlocks.CURSECOAL_PILE.get(), BeneathItems.CURSECOAL);
        add(BeneathBlocks.HELLFORGE.get(), lootTable().withPool(lootPool().add(lootTableItem(BeneathBlocks.NETHER_PEBBLE).apply(setCount(7)))));
        add(BeneathBlocks.HELLFORGE.get(), lootTable().withPool(lootPool().add(lootTableItem(BeneathBlocks.NETHER_PEBBLE).apply(setCount(7)))));
        add(BeneathBlocks.SOUL_CLAY.get(), lootTable().withPool(lootPool().add(lootTableItem(Items.CLAY_BALL).apply(setCount(1, 4)))));
        dropSelf(BeneathBlocks.BLACKSTONE_AQUEDUCT.get());
        dropSelf(BeneathBlocks.ANCIENT_ALTAR.get());
        dropOther(BeneathBlocks.SOUL_FARMLAND.get(), Blocks.SOUL_SOIL);
        BeneathBlocks.MUSHROOMS.forEach((shroom, block) -> dropOther(block.get(), BeneathItems.MUSHROOMS.get(shroom)));
        addIsolated(BeneathBlocks.CRACKRACK.get(), BeneathItems.CRACKRACK_ROCK);

    }


    private LootTable.Builder createGrass(Block block, ItemLike straw, ItemLike seed)
    {
        return LootTable.lootTable()
            .withPool(
                lootPool().add(
                    AlternativesEntry.alternatives(
                        lootTableItem(block).when(HAS_SHEARS_LIKE),
                        SequentialEntry.sequential(
                            lootTableItem(seed).when(chance(0.1f)),
                            lootTableItem(straw)
                        )
                ).when(survivesExplosion())
            ));
    }

    private static <T extends Comparable<T> & StringRepresentable> LootItemBlockStatePropertyCondition.Builder hasProperty(Block block, Property<T> property, T value)
    {
        return hasBlockStateProperties(block).setProperties(
            StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
        );
    }

    private static LootItemBlockStatePropertyCondition.Builder hasProperty(Block block, Property<Boolean> property, boolean value, Property<Boolean> property2, boolean value2)
    {
        return hasBlockStateProperties(block).setProperties(
            StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
        );
    }

    private static <T extends Comparable<T> & StringRepresentable> LootItemBlockStatePropertyCondition.Builder hasProperty(Block block, Property<Integer> property, int value)
    {
        return hasBlockStateProperties(block).setProperties(
            StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
        );
    }

    private static <T extends Comparable<T> & StringRepresentable> LootItemBlockStatePropertyCondition.Builder hasProperty(Block block, Property<Boolean> property, boolean value)
    {
        return hasBlockStateProperties(block).setProperties(
            StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
        );
    }

    private static LootItemCondition.@NotNull Builder matchesTool(TagKey<Item> tag)
    {
        return MatchTool.toolMatches(ItemPredicate.Builder.item().of(tag));
    }

    private static LootItemCondition.Builder chance(float chance)
    {
        return LootItemRandomChanceCondition.randomChance(chance);
    }

    private Block metal(Metal metal, Metal.BlockType type)
    {
        return TFCBlocks.METALS.get(metal).get(type).get();
    }

    private Block rock(Rock rock, Rock.BlockType type)
    {
        return TFCBlocks.ROCK_BLOCKS.get(rock).get(type).get();
    }

    private void addIsolated(Block netherrack, ItemLike alternate)
    {
        add(netherrack, createSelfDropDispatchTable(
            netherrack,
            () -> IsIsolatedCondition.INSTANCE,
            applyExplosionDecay(alternate, lootTableItem(alternate).apply(setCount(2, 4)))
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
