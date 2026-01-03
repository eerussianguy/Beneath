package com.eerussianguy.beneath.providers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.component.LostPage;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.HideItemType;
import net.dries007.tfc.common.items.TFCItems;

public class BuiltinLostPages extends DataManagerProvider<LostPage> implements Accessors
{
    public BuiltinLostPages(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(LostPage.MANAGER, output, lookup, Beneath.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add("slime", BeneathItems.RAW_SLIME,
            new int[]{64, 100, 100, 200, 200, 220, 300},
            TFCItems.GRADED_ORES.get(Ore.GARNIERITE).get(Ore.Grade.RICH),
            new int[]{7, 12, 15, 15, 18, 19, 22},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.SLIME, LostPage.Punishment.DRUNKENNESS}
        );

        add("cursed_hide", BeneathItems.CURSED_HIDE,
            new int[]{1},
            TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE),
            new int[]{1},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION, LostPage.Punishment.DRUNKENNESS}
        );

        add("cursed_hide2", BeneathItems.CURSED_HIDE,
            new int[]{2, 3},
            TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE),
            new int[]{2, 3},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION, LostPage.Punishment.DRUNKENNESS}
        );

        add("cursed_hide3", BeneathItems.CURSED_HIDE,
            new int[]{4, 5, 6},
            TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE),
            new int[]{4, 5, 6},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION, LostPage.Punishment.DRUNKENNESS}
        );

        add("blackstone", Ingredient.of(commonTagOf(Registries.ITEM, "cobblestone")),
            new int[]{48, 64, 64, 64, 72, 78},
            Blocks.BLACKSTONE,
            new int[]{48, 52, 52, 64, 74},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.INFESTATION},
            Component.translatable("beneath.lost_page.cobble")
        );
    }

    private void add(String name, ItemLike cost, int[] costs, ItemLike reward, int[] rewards, LostPage.Punishment[] punishments)
    {
        add(name, Ingredient.of(cost), costs, reward, rewards, punishments, null);
    }

    private void add(String name, Ingredient cost, int[] costs, ItemLike reward, int[] rewards, LostPage.Punishment[] punishments, @Nullable Component translation)
    {
        final Holder<Item> rewardHolder = BuiltInRegistries.ITEM.wrapAsHolder(reward.asItem());
        final List<Integer> costList = java.util.Arrays.stream(costs).boxed().toList();
        final List<Integer> rewardList = java.util.Arrays.stream(rewards).boxed().toList();
        final List<LostPage.Punishment> punishmentList = java.util.Arrays.asList(punishments);
        final Optional<Component> translationOpt = translation == null ? Optional.empty() : Optional.of(translation);

        add(name, new LostPage(cost, costList, rewardHolder, rewardList, punishmentList, translationOpt));
    }
}
