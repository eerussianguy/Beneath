package com.eerussianguy.beneath;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import com.eerussianguy.beneath.providers.BuiltinBlockLootTables;
import com.eerussianguy.beneath.providers.BuiltinBlockTags;
import com.eerussianguy.beneath.providers.BuiltinChestLootTables;
import com.eerussianguy.beneath.providers.BuiltinDamageTypes;
import com.eerussianguy.beneath.providers.BuiltinEntityLootTables;
import com.eerussianguy.beneath.providers.BuiltinFoods;
import com.eerussianguy.beneath.providers.BuiltinFuels;
import com.eerussianguy.beneath.providers.BuiltinItemHeat;
import com.eerussianguy.beneath.providers.BuiltinItemSizes;
import com.eerussianguy.beneath.providers.BuiltinLostPages;
import com.eerussianguy.beneath.providers.BuiltinNetherFertilizers;
import com.eerussianguy.beneath.providers.BuiltinPiglinBarterLootTables;
import com.eerussianguy.beneath.providers.BuiltinRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.dries007.tfc.TerraFirmaCraft;

import static com.eerussianguy.beneath.Beneath.*;

@EventBusSubscriber(modid = MOD_ID)
public class DataEntryPoint
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        final PackOutput output = event.getGenerator().getPackOutput();
        final var lookup = add(event, new DatapackBuiltinEntriesProvider(
            event.getGenerator().getPackOutput(), event.getLookupProvider(),
            new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, BuiltinDamageTypes::new)
            , Set.of(MOD_ID, TerraFirmaCraft.MOD_ID, "minecraft")
        )).getRegistryProvider();

        final var blockTags = add(event, new BuiltinBlockTags(event, lookup)).contentsGetter();

        add(event, new BuiltinNetherFertilizers(output, lookup));
        add(event, new BuiltinLostPages(output, lookup));
        add(event, new BuiltinFoods(output, lookup));
        add(event, new BuiltinFuels(output, lookup));
        add(event, new BuiltinItemSizes(output, lookup));
        var itemHeat = add(event, new BuiltinItemHeat(output, lookup));

        add(event, new BuiltinRecipes(output, lookup, itemHeat));

        add(event,
            new LootTableProvider(
                output,
                Collections.emptySet(),
                List.of(
                    new LootTableProvider.SubProviderEntry(BuiltinBlockLootTables::new, LootContextParamSets.BLOCK),
                    new LootTableProvider.SubProviderEntry(BuiltinChestLootTables::new, LootContextParamSets.CHEST),
                    new LootTableProvider.SubProviderEntry(BuiltinPiglinBarterLootTables::new, LootContextParamSets.PIGLIN_BARTER),
                    new LootTableProvider.SubProviderEntry(BuiltinEntityLootTables::new, LootContextParamSets.ENTITY)
                ),
                lookup
            )
        );
    }

    private static <T extends DataProvider> T add(GatherDataEvent event, T provider)
    {
        return event.getGenerator().addProvider(true, provider);
    }

    private static <T> void tags(GatherDataEvent event, ResourceKey<Registry<T>> registry, CompletableFuture<HolderLookup.Provider> lookup, BiConsumer<HolderLookup.Provider, TagLookup<T>> callback)
    {
        add(event, new TagsProvider<T>(event.getGenerator().getPackOutput(), registry, lookup, MOD_ID, event.getExistingFileHelper())
        {
            @Override
            protected void addTags(HolderLookup.Provider provider)
            {
                callback.accept(provider, this::tag);
            }
        });
    }

    @FunctionalInterface
    interface TagLookup<T>
    {
        TagsProvider.TagAppender<T> tag(TagKey<T> tag);
    }
}
