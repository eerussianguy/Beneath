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
import com.eerussianguy.beneath.common.items.BeneathItemTags;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.google.common.base.Preconditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.registry.IdHolder;

import static com.eerussianguy.beneath.common.items.BeneathItemTags.*;

public class BuiltinItemTags extends TagsProvider<Item> implements Accessors
{
    private final ExistingFileHelper.IResourceType resourceType;

    public BuiltinItemTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(event.getGenerator().getPackOutput(), Registries.ITEM, lookup, Beneath.MOD_ID, event.getExistingFileHelper());
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(Tags.Items.MUSHROOMS).add(BeneathItems.MUSHROOMS);

        tag(SPARKS_ON_SULFUR)
            .addTags(TFCTags.Items.TOOLS_STEEL, TFCTags.Items.TOOLS_BLACK_STEEL, TFCTags.Items.TOOLS_BLUE_STEEL, TFCTags.Items.TOOLS_RED_STEEL, TFCTags.Items.TOOLS_WROUGHT_IRON);
        tag(USABLE_IN_JUICER)
            .addTags(TFCTags.Items.FRUITS, Tags.Items.MUSHROOMS)
            .add(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS);
        tag(UNPOSTABLE)
            .add(Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS, BeneathItems.GHOST_PEPPER.asItem(), BeneathBlocks.GLEAMFLOWER.asItem(), Items.CRIMSON_ROOTS, Items.WARPED_ROOTS, Items.NETHER_WART, Items.GHAST_TEAR)
            .addTag(Tags.Items.MUSHROOMS);

        tag(TFCTags.Items.AQUEDUCTS).add(BeneathBlocks.BLACKSTONE_AQUEDUCT.asItem());
        tag(TFCTags.Items.STONES_LOOSE).add(BeneathBlocks.NETHER_PEBBLE.asItem(), BeneathBlocks.BLACKSTONE_PEBBLE.asItem());
        tag(TFCTags.Items.STONES_LOOSE_CATEGORY.get(RockCategory.METAMORPHIC)).add(BeneathBlocks.BLACKSTONE_PEBBLE.asItem());
        tag(TFCTags.Items.STONES_LOOSE_CATEGORY.get(RockCategory.SEDIMENTARY)).add(BeneathBlocks.NETHER_PEBBLE.asItem());
        tag(Tags.Items.SEEDS).add(BeneathItems.SEEDS);

        makeStandardLogTag(Stem.CRIMSON, BeneathItemTags.CRIMSON_LOGS);
        makeStandardLogTag(Stem.WARPED, BeneathItemTags.WARPED_LOGS);
        addAll(Wood.BlockType.PLANKS, ItemTags.PLANKS);
        addAll(Wood.BlockType.DOOR, ItemTags.WOODEN_DOORS);
        addAll(Wood.BlockType.TRAPDOOR, ItemTags.WOODEN_TRAPDOORS);
        addAll(Wood.BlockType.FENCE, ItemTags.WOODEN_FENCES);
        addAll(Wood.BlockType.LOG_FENCE, ItemTags.WOODEN_FENCES);
        addAll(Wood.BlockType.FENCE_GATE, ItemTags.FENCE_GATES);
        addAll(Wood.BlockType.BUTTON, ItemTags.BUTTONS);
        addAll(Wood.BlockType.PRESSURE_PLATE, ItemTags.WOODEN_PRESSURE_PLATES);
        addAll(Wood.BlockType.SLAB, ItemTags.WOODEN_SLABS);
        addAll(Wood.BlockType.STAIRS, ItemTags.WOODEN_STAIRS);
        addAll(Wood.BlockType.WORKBENCH, TFCTags.Items.WORKBENCHES);
        addAll(Wood.BlockType.CHEST, Tags.Items.CHESTS_WOODEN);
        addAll(Wood.BlockType.TRAPPED_CHEST, Tags.Items.CHESTS_TRAPPED);
        addAll(Wood.BlockType.TRAPPED_CHEST, Tags.Items.CHESTS_WOODEN);
        addAll(Wood.BlockType.LOG_FENCE, ItemTags.WOODEN_FENCES);
        addAll(Wood.BlockType.TWIG, Tags.Items.RODS_WOODEN);
        addAll(Wood.BlockType.TWIG, TFCTags.Items.TWIGS);
        tag(ItemTags.SIGNS).add(BeneathItems.SIGNS);
        tag(ItemTags.HANGING_SIGNS).add2(BeneathItems.HANGING_SIGNS);
        tag(TFCTags.Items.SUPPORT_BEAMS).add(BeneathItems.SUPPORTS);
        tag(TFCTags.Items.LUMBER).add(BeneathItems.LUMBER);
        tag(TFCTags.Items.MINECARTS).add(BeneathItems.CHEST_MINECARTS);
        addAll(Wood.BlockType.BARREL, TFCTags.Items.BARRELS);
        addAll(Wood.BlockType.BARREL, TFCTags.Items.CARRIED_BY_HORSE);
        addAll(Wood.BlockType.CHEST, TFCTags.Items.CARRIED_BY_HORSE);
        addAll(Wood.BlockType.TRAPPED_CHEST, TFCTags.Items.CARRIED_BY_HORSE);
        addAll(Wood.BlockType.LEAVES, ItemTags.LEAVES);
        addAll(Wood.BlockType.FALLEN_LEAVES, TFCTags.Items.FALLEN_LEAVES);
        addAll(Wood.BlockType.SAPLING, ItemTags.SAPLINGS);
    }

    private void addAll(Wood.BlockType type, TagKey<Item> tag)
    {
        for (Stem stem : Stem.VALUES)
            tag(tag).add(BeneathBlocks.WOODS.get(stem).get(type).asItem());
    }

    private void makeStandardLogTag(Stem wood, TagKey<Item> itemTag)
    {
        tag(itemTag)
            .add(wood.getBlock(Wood.BlockType.LOG).get().asItem())
            .add(wood.getBlock(Wood.BlockType.WOOD).get().asItem())
            .add(wood.getBlock(Wood.BlockType.STRIPPED_LOG).get().asItem())
            .add(wood.getBlock(Wood.BlockType.STRIPPED_WOOD).get().asItem());
    }

    @Override
    protected ItemTagAppender tag(TagKey<Item> tag)
    {
        return new ItemTagAppender(getOrCreateRawBuilder(tag));
    }

    @Override
    protected TagBuilder getOrCreateRawBuilder(TagKey<Item> tag)
    {
        if (existingFileHelper != null) existingFileHelper.trackGenerated(tag.location(), resourceType);
        return this.builders.computeIfAbsent(tag.location(), key -> new TagBuilder()
        {
            @Override
            public TagBuilder add(TagEntry entry)
            {
                Preconditions.checkArgument(!entry.getId().equals(BuiltInRegistries.ITEM.getDefaultKey()), "Adding air to item tag");
                return super.add(entry);
            }
        });
    }

    static class ItemTagAppender extends TagAppender<Item> implements Accessors
    {
        ItemTagAppender(TagBuilder builder)
        {
            super(builder);
        }

        ItemTagAppender add(Item... items)
        {
            for (Item item : items) add(key(item));
            return this;
        }

        ItemTagAppender add(Stream<? extends Supplier<? extends Item>> items)
        {
            items.forEach(b -> add(key(b.get())));
            return this;
        }

        @SafeVarargs
        final <T extends IdHolder<? extends Item>> ItemTagAppender add(T... items)
        {
            return add(Arrays.stream(items));
        }

        /**
         * Adds every FL-added item matching the given predicate
         */
        ItemTagAppender addEveryFL(Predicate<Item> predicate)
        {
            return add(BeneathItems.ITEMS.getEntries().stream().filter(e -> predicate.test(e.get())));
        }

        ItemTagAppender add(Map<?, ? extends IdHolder<? extends Item>> items)
        {
            items.values().forEach(this::add);
            return this;
        }

        ItemTagAppender add2(Map<?, ? extends Map<?, ? extends IdHolder<? extends Item>>> items)
        {
            items.values().forEach(m -> m.values().forEach(this::add));
            return this;
        }

        <V> ItemTagAppender add2(Map<?, ? extends Map<?, V>> items, Function<V, ? extends IdHolder<? extends Item>> ap)
        {
            items.values().forEach(m -> m.values().forEach(v -> add(ap.apply(v))));
            return this;
        }

        ItemTagAppender add3(Map<?, ? extends Map<?, ? extends Map<?, ? extends IdHolder<? extends Item>>>> items)
        {
            items.values().forEach(m1 -> m1.values().forEach(m2 -> m2.values().forEach(this::add)));
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Item>> ItemTagAppender add(Map<T1, Map<T2, V>> items, T2 key)
        {
            return add(pivot(items, key));
        }

        <T, V extends IdHolder<? extends Item>> ItemTagAppender addOnly(Map<T, V> items, Predicate<T> key)
        {
            items.forEach((k, v) -> {if (key.test(k)) add(v);});
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Item>> ItemTagAppender addOnly2(Map<T1, Map<T2, V>> items, Predicate<T2> key)
        {
            items.values().forEach(m -> addOnly(m, key));
            return this;
        }

        @SafeVarargs
        @SuppressWarnings("unchecked")
        final <K> ItemTagAppender addTags(Function<K, TagKey<Item>> apply, K... values)
        {
            return addTags(Arrays.stream(values).map(apply).toArray(TagKey[]::new));
        }

        @Override
        public ItemTagAppender addTag(TagKey<Item> tag)
        {
            return (ItemTagAppender) super.addTag(tag);
        }

        @Override
        @SafeVarargs
        public final ItemTagAppender addTags(TagKey<Item>... values)
        {
            return (ItemTagAppender) super.addTags(values);
        }

        ItemTagAppender remove(Item... items)
        {
            for (Item item : items) remove(key(item));
            return this;
        }

        private ResourceKey<Item> key(Item item)
        {
            return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
        }
    }
}
