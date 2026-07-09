package com.eerussianguy.beneath.providers;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.google.common.base.Preconditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.IdHolder;

import static com.eerussianguy.beneath.common.blocks.BeneathBlockTags.*;

public class BuiltinBlockTags extends TagsProvider<Block> implements Accessors
{
    private final ExistingFileHelper.IResourceType resourceType;

    public BuiltinBlockTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(event.getGenerator().getPackOutput(), Registries.BLOCK, lookup, Beneath.MOD_ID, event.getExistingFileHelper());
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(BREAKS_SLOWLY).add(Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL, Blocks.MAGMA_BLOCK, Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM);
        tag(EVENT_REPLACEABLE).add(Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL, Blocks.MAGMA_BLOCK, Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM, Blocks.BLACKSTONE)
            .addTags(BlockTags.BASE_STONE_OVERWORLD, BlockTags.DIRT, TFCTags.Blocks.GRASS);
        tag(HELLFORGE_INSULATION).add(BeneathBlocks.HELLBRICKS);
        tag(NETHER_BUSH_PLANTABLE_ON)
            .addTag(BlockTags.BASE_STONE_NETHER)
            .add(Blocks.SOUL_SOIL, Blocks.SOUL_SAND, BeneathBlocks.SOUL_FARMLAND.get(), BeneathBlocks.SOUL_CLAY.get(), Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM);
        tag(MUSHROOMS).add(BeneathBlocks.MUSHROOMS);
        tag(NETHER_BRICKS).add(Blocks.NETHER_BRICKS, Blocks.RED_NETHER_BRICKS);
        tag(NETHER_BRICK_DECOR)
            .add(Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_SLAB, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_BRICK_WALL)
            .add(Blocks.RED_NETHER_BRICK_SLAB, Blocks.RED_NETHER_BRICK_STAIRS, Blocks.RED_NETHER_BRICK_WALL);
        tag(BLACKSTONE).add(Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, Blocks.CHISELED_POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.GILDED_BLACKSTONE);
        tag(BLACKSTONE_DECOR).add(Blocks.BLACKSTONE_SLAB, Blocks.BLACKSTONE_STAIRS, Blocks.BLACKSTONE_WALL, Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_STAIRS, Blocks.POLISHED_BLACKSTONE_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);

        tag(TFCTags.Blocks.AQUEDUCTS).add(BeneathBlocks.BLACKSTONE_AQUEDUCT);
        tag(TFCTags.Blocks.BREAKS_WHEN_ISOLATED).add(Blocks.BASALT, Blocks.BLACKSTONE, Blocks.NETHERRACK, BeneathBlocks.CRACKRACK.get(), Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM);
        tag(TFCTags.Blocks.TREE_GROWS_ON).add(Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM, Blocks.NETHERRACK);
        tag(TFCTags.Blocks.PROSPECTABLE).add2(BeneathBlocks.GRADED_ORES).add(BeneathBlocks.MINERALS).add(Blocks.NETHER_QUARTZ_ORE);
        tag(TFCTags.Blocks.CAN_BE_SNOW_PILED).add(BeneathBlocks.BLACKSTONE_PEBBLE, BeneathBlocks.NETHER_PEBBLE);
        tag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE).addTags(NETHER_BRICKS, NETHER_BRICK_DECOR, BLACKSTONE, BLACKSTONE_DECOR)
            .add(BeneathBlocks.CRACKRACK)
            .add(Blocks.GLOWSTONE, Blocks.BASALT, Blocks.BLACKSTONE, Blocks.GILDED_BLACKSTONE);
        tag(TFCTags.Blocks.CAN_START_COLLAPSE).addTags(NETHER_BRICKS, BLACKSTONE)
            .add(BeneathBlocks.CRACKRACK)
            .add(Blocks.GLOWSTONE, Blocks.BASALT, Blocks.BLACKSTONE, Blocks.GILDED_BLACKSTONE);
        tag(TFCTags.Blocks.CAN_COLLAPSE)
            .add(BeneathBlocks.HAUNTED_SPIKE, BeneathBlocks.GLOWSTONE_SPIKE)
            .add(BeneathBlocks.CRACKRACK)
            .add(Blocks.GLOWSTONE, Blocks.BASALT, Blocks.BLACKSTONE, Blocks.GILDED_BLACKSTONE);
        tag(TFCTags.Blocks.CAN_LANDSLIDE)
            .add(BeneathBlocks.COBBLERACK, BeneathBlocks.FUNGAL_COBBLERACK, BeneathBlocks.SOUL_CLAY, BeneathBlocks.SOUL_FARMLAND);

        tag(BlockTags.STONE_BRICKS).add(BeneathBlocks.HELLBRICKS);
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BeneathBlocks.BLACKSTONE_AQUEDUCT, BeneathBlocks.COBBLERACK, BeneathBlocks.FUNGAL_COBBLERACK, BeneathBlocks.HELLBRICKS)
            .add2(BeneathBlocks.GRADED_ORES).add(BeneathBlocks.MINERALS).add(BeneathBlocks.SLIMED_NETHERRACK)
            .add(BeneathBlocks.GLOWSTONE_SPIKE, BeneathBlocks.HAUNTED_SPIKE, BeneathBlocks.CRACKRACK)
            .add(BeneathBlocks.GLOWSTONE_ROPE_ANCHOR, BeneathBlocks.HAUNTED_ROPE_ANCHOR);
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(BeneathBlocks.CURSECOAL_PILE, BeneathBlocks.HELLFORGE, BeneathBlocks.SOUL_FARMLAND, BeneathBlocks.SOUL_CLAY, BeneathBlocks.SULFUR);
        tag(BlockTags.MINEABLE_WITH_HOE)
            .add(BeneathBlocks.WARPED_THATCH, BeneathBlocks.CRIMSON_THATCH).add(BeneathBlocks.CROPS)
            .add(BeneathBlocks.MUSHROOMS);
        tag(BlockTags.BASE_STONE_NETHER).add(BeneathBlocks.CRACKRACK);
        tag(BlockTags.FEATURES_CANNOT_REPLACE).addOnly2(BeneathBlocks.WOODS, type -> type == Wood.BlockType.CHEST || type == Wood.BlockType.TRAPPED_CHEST);
        tag(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE).addOnly2(BeneathBlocks.WOODS, type -> type == Wood.BlockType.CHEST || type == Wood.BlockType.TRAPPED_CHEST);

        tag(Tags.Blocks.COBBLESTONES).add(BeneathBlocks.COBBLERACK);
        tag(Tags.Blocks.COBBLESTONES_MOSSY).add(BeneathBlocks.FUNGAL_COBBLERACK);

        // WOODS

        // Stuff without individual tags that still needs to be mineable with an axe
        addAllStems(Wood.BlockType.BOOKSHELF, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.TOOL_RACK, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.TWIG, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.TWIG, TFCTags.Blocks.CAN_BE_SNOW_PILED);
        addAllStems(Wood.BlockType.LOOM, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.SLUICE, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.BARREL, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.LECTERN, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.SCRIBING_TABLE, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.SEWING_TABLE, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.SHELF, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.AXLE, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.BLADED_AXLE, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.ENCASED_AXLE, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.CLUTCH, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.GEAR_BOX, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.HORIZONTAL_SUPPORT, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.VERTICAL_SUPPORT, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.WATER_WHEEL, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.CRATE, BlockTags.MINEABLE_WITH_AXE);

        // For all true wood types
        addAllStems(Wood.BlockType.BARREL, TFCTags.Blocks.CLOCK_READABLE);
        addAllStems(Wood.BlockType.PLANKS, BlockTags.PLANKS);
        addAllStems(Wood.BlockType.DOOR, BlockTags.WOODEN_DOORS); // doors? mob interactable doors? added automatically from this?
        addAllStems(Wood.BlockType.TRAPDOOR, BlockTags.WOODEN_TRAPDOORS);
        addAllStems(Wood.BlockType.FENCE, Tags.Blocks.FENCES_WOODEN);
        addAllStems(Wood.BlockType.LOG_FENCE, Tags.Blocks.FENCES_WOODEN);
        addAllStems(Wood.BlockType.FENCE, BlockTags.WOODEN_FENCES);
        addAllStems(Wood.BlockType.LOG_FENCE, BlockTags.WOODEN_FENCES);
        addAllStems(Wood.BlockType.FENCE_GATE, Tags.Blocks.FENCE_GATES_WOODEN); // unstable bottom center?
        addAllStems(Wood.BlockType.BUTTON, BlockTags.WOODEN_BUTTONS);
        addAllStems(Wood.BlockType.PRESSURE_PLATE, BlockTags.WOODEN_PRESSURE_PLATES); // Wall post overrides?
        addAllStems(Wood.BlockType.SLAB, BlockTags.WOODEN_SLABS);
        addAllStems(Wood.BlockType.STAIRS, BlockTags.WOODEN_STAIRS);
        addAllStems(Wood.BlockType.WORKBENCH, TFCTags.Blocks.WORKBENCHES);
        addAllStems(Wood.BlockType.WORKBENCH, Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES);
        addAllStems(Wood.BlockType.WORKBENCH, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.CHEST, Tags.Blocks.CHESTS_WOODEN); // tfc Pet sits on
        addAllStems(Wood.BlockType.CHEST, BlockTags.MINEABLE_WITH_AXE); // tfc Pet sits on
        addAllStems(Wood.BlockType.TRAPPED_CHEST, Tags.Blocks.CHESTS_WOODEN);
        addAllStems(Wood.BlockType.TRAPPED_CHEST, BlockTags.MINEABLE_WITH_AXE);
        addAllStems(Wood.BlockType.HORIZONTAL_SUPPORT, TFCTags.Blocks.SUPPORT_BEAMS);
        addAllStems(Wood.BlockType.VERTICAL_SUPPORT, TFCTags.Blocks.SUPPORT_BEAMS);
        addAllStems(Wood.BlockType.SIGN, BlockTags.STANDING_SIGNS);
        addAllStems(Wood.BlockType.WALL_SIGN, BlockTags.WALL_SIGNS);
        makeStandardLogTag(Stem.CRIMSON, CRIMSON_LOGS);
        makeStandardLogTag(Stem.CRIMSON, WARPED_LOGS);
        tag(BlockTags.LOGS).addTags(CRIMSON_LOGS, WARPED_LOGS);

        // For every tree species
        addAllStems(Wood.BlockType.SAPLING, BlockTags.SAPLINGS);
        addAllStems(Wood.BlockType.POTTED_SAPLING, BlockTags.FLOWER_POTS);
        addAllStems(Wood.BlockType.FALLEN_LEAVES, TFCTags.Blocks.FALLEN_LEAVES); // Lots to check
        addAllStems(Wood.BlockType.LEAVES, BlockTags.LEAVES); // Lots to check

        // Breakable by Sharp tools
        breakableBySharps(Wood.BlockType.LEAVES);
        breakableBySharps(Wood.BlockType.FALLEN_LEAVES);
        breakableBySharps(Wood.BlockType.SAPLING);

        // Hanging Signs
        for (Metal metal : Metal.values())
        {
            if (metal.allParts())
            {
                for (Stem wood : Stem.values())
                {
                    tag(BlockTags.WALL_HANGING_SIGNS).add(BeneathBlocks.WALL_HANGING_SIGNS.get(wood).get(metal).get());
                    tag(BlockTags.CEILING_HANGING_SIGNS).add(BeneathBlocks.CEILING_HANGING_SIGNS.get(wood).get(metal).get());
                }
            }
        }
    }

    private void breakableBySharps(Wood.BlockType wood)
    {
        addAllStems(wood, BlockTags.MINEABLE_WITH_HOE);
        addAllStems(wood, TFCTags.Blocks.MINEABLE_WITH_KNIFE);
        addAllStems(wood, TFCTags.Blocks.MINEABLE_WITH_SCYTHE);
    }

    private void addAllStems(Wood.BlockType type, TagKey<Block> tagKey)
    {
        BeneathBlocks.WOODS.forEach(
            (s, m) -> tag(tagKey).add(s.getBlock(type).get())
        );
    }

    private void makeStandardLogTag(Stem logType, TagKey<Block> blockTag)
    {
        tag(blockTag)
            .add(logType.getBlock(Wood.BlockType.LOG).get())
            .add(logType.getBlock(Wood.BlockType.WOOD).get())
            .add(logType.getBlock(Wood.BlockType.STRIPPED_LOG).get())
            .add(logType.getBlock(Wood.BlockType.STRIPPED_WOOD).get());
    }

    @Override
    protected BlockTagAppender tag(TagKey<Block> tag)
    {
        return new BlockTagAppender(getOrCreateRawBuilder(tag));
    }

    @Override
    protected TagBuilder getOrCreateRawBuilder(TagKey<Block> tag)
    {
        if (existingFileHelper != null) existingFileHelper.trackGenerated(tag.location(), resourceType);
        return this.builders.computeIfAbsent(tag.location(), key -> new TagBuilder()
        {
            @Override
            public TagBuilder add(TagEntry entry)
            {
                Preconditions.checkArgument(!entry.getId().equals(BuiltInRegistries.BLOCK.getDefaultKey()), "Adding air to block tag");
                return super.add(entry);
            }
        });
    }

    @SuppressWarnings("UnusedReturnValue")
    static class BlockTagAppender extends TagAppender<Block> implements Accessors
    {
        BlockTagAppender(TagBuilder builder)
        {
            super(builder);
        }

        BlockTagAppender add(Block... blocks)
        {
            for (Block block : blocks) add(key(block));
            return this;
        }

        BlockTagAppender add(Stream<? extends Supplier<? extends Block>> blocks)
        {
            blocks.forEach(b -> add(key(b.get())));
            return this;
        }

        @SafeVarargs
        final <T extends IdHolder<? extends Block>> BlockTagAppender add(T... blocks)
        {
            return add(Arrays.stream(blocks));
        }

        /**
         * Adds every TFC-added block matching the given predicate
         */
        BlockTagAppender addEveryBeneath(Predicate<Block> predicate)
        {
            return add(BeneathBlocks.BLOCKS.getEntries().stream().filter(e -> predicate.test(e.get())));
        }

        BlockTagAppender add(Map<?, ? extends IdHolder<? extends Block>> blocks)
        {
            blocks.values().forEach(this::add);
            return this;
        }

        BlockTagAppender add2(Map<?, ? extends Map<?, ? extends IdHolder<? extends Block>>> blocks)
        {
            blocks.values().forEach(m -> m.values().forEach(this::add));
            return this;
        }

        <V> BlockTagAppender add2(Map<?, ? extends Map<?, V>> blocks, Function<V, ? extends IdHolder<? extends Block>> ap)
        {
            blocks.values().forEach(m -> m.values().forEach(v -> add(ap.apply(v))));
            return this;
        }

        BlockTagAppender add3(Map<?, ? extends Map<?, ? extends Map<?, ? extends IdHolder<? extends Block>>>> blocks)
        {
            blocks.values().forEach(m1 -> m1.values().forEach(m2 -> m2.values().forEach(this::add)));
            return this;
        }

        BlockTagAppender addAll(Map<?, DecorationBlockHolder> blocks)
        {
            blocks.values().forEach(h -> add(h.slab(), h.stair(), h.wall()));
            return this;
        }

        BlockTagAppender addAll(DecorationBlockHolder blocks)
        {
            add(blocks.slab(), blocks.stair(), blocks.wall());
            return this;
        }

        BlockTagAppender addAll2(Map<?, ? extends Map<?, DecorationBlockHolder>> blocks)
        {
            blocks.values().forEach(m -> m.values().forEach(h -> add(h.slab(), h.stair(), h.wall())));
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Block>> BlockTagAppender add(Map<T1, Map<T2, V>> blocks, T2 key)
        {
            return add(pivot(blocks, key));
        }

        <T, V extends IdHolder<? extends Block>> BlockTagAppender addOnly(Map<T, V> blocks, Predicate<T> key)
        {
            blocks.forEach((k, v) -> {if (key.test(k)) add(v);});
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Block>> BlockTagAppender addOnly2(Map<T1, Map<T2, V>> blocks, Predicate<T2> key)
        {
            blocks.values().forEach(m -> addOnly(m, key));
            return this;
        }

        @SafeVarargs
        @SuppressWarnings("unchecked")
        final <K> BlockTagAppender addTags(Function<K, TagKey<Block>> apply, K... values)
        {
            return addTags(Arrays.stream(values).map(apply).toArray(TagKey[]::new));
        }

        @Override
        public BlockTagAppender addTag(TagKey<Block> tag)
        {
            return (BlockTagAppender) super.addTag(tag);
        }

        @Override
        @SafeVarargs
        public final BlockTagAppender addTags(TagKey<Block>... values)
        {
            return (BlockTagAppender) super.addTags(values);
        }

        BlockTagAppender remove(Block... blocks)
        {
            for (Block block : blocks) remove(key(block));
            return this;
        }

        private ResourceKey<Block> key(Block block)
        {
            return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
        }
    }
}
