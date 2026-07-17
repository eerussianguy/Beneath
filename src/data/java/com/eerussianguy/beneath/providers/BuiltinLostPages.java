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
import net.minecraft.world.item.Items;
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
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.SLIME, LostPage.Punishment.DRUNKENNESS, LostPage.Punishment.WRATH, LostPage.Punishment.BLESSING}
        );

        add("cursed_hide", BeneathItems.CURSED_HIDE,
            new int[]{1},
            TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE),
            new int[]{1},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION, LostPage.Punishment.DRUNKENNESS, LostPage.Punishment.BLESSING}
        );

        add("cursed_hide2", BeneathItems.CURSED_HIDE,
            new int[]{2, 3},
            TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE),
            new int[]{2, 3},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION, LostPage.Punishment.DRUNKENNESS, LostPage.Punishment.CORRUPTION, LostPage.Punishment.BLESSING}
        );

        add("cursed_hide3", BeneathItems.CURSED_HIDE,
            new int[]{4, 5, 6},
            TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE),
            new int[]{4, 5, 6},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION, LostPage.Punishment.DRUNKENNESS, LostPage.Punishment.CORRUPTION, LostPage.Punishment.CHAMPION, LostPage.Punishment.BLESSING}
        );

        add("blackstone", Ingredient.of(commonTagOf(Registries.ITEM, "cobblestone")),
            new int[]{48, 64, 64, 64, 72, 78},
            Blocks.BLACKSTONE,
            new int[]{48, 52, 52, 64, 74},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.INFESTATION, LostPage.Punishment.WRATH, LostPage.Punishment.GREED},
            Component.translatable("beneath.lost_page.cobble")
        );

        add("gold_chunk", BeneathItems.GOLD_CHUNK,
            new int[]{16, 24, 32, 32, 48},
            TFCItems.GRADED_ORES.get(Ore.NATIVE_GOLD).get(Ore.Grade.RICH),
            new int[]{4, 6, 6, 8, 10},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.DRUNKENNESS, LostPage.Punishment.BLAZE_INFERNO}
        );

        add("crackrack", BeneathItems.CRACKRACK_ROCK,
            new int[]{48, 64, 64, 96},
            TFCItems.GRADED_ORES.get(Ore.CASSITERITE).get(Ore.Grade.RICH),
            new int[]{8, 12, 14, 16},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.INFESTATION, LostPage.Punishment.SLIME}
        );

        add("cursed_hide_ore", BeneathItems.CURSED_HIDE,
            new int[]{6, 8, 10},
            TFCItems.GRADED_ORES.get(Ore.SPHALERITE).get(Ore.Grade.RICH),
            new int[]{10, 14, 18},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.WITHERING, LostPage.Punishment.UNKNOWN}
        );

        add("cursecoal", BeneathItems.CURSECOAL,
            new int[]{32, 48, 64, 64, 80},
            TFCItems.ORES.get(Ore.BITUMINOUS_COAL),
            new int[]{12, 16, 20, 20, 24},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.INFESTATION}
        );

        add("sulfur_pact", BeneathItems.CURSECOAL,
            new int[]{40, 56, 72},
            TFCItems.ORE_POWDERS.get(Ore.SULFUR),
            new int[]{6, 8, 10},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.BLAZE_INFERNO, LostPage.Punishment.WITHERING}
        );

        add("crimson_straw", BeneathItems.CRIMSON_STRAW,
            new int[]{16, 24, 32},
            TFCItems.STRAW,
            new int[]{8, 12, 16},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION}
        );

        add("warped_straw", BeneathItems.WARPED_STRAW,
            new int[]{16, 24, 32},
            TFCItems.COMPOST,
            new int[]{4, 6, 8},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.LEVITATION}
        );

        add("netherrack", Items.NETHERRACK,
            new int[]{48, 64, 64, 72},
            BeneathItems.BLACKSTONE_BRICK,
            new int[]{16, 24, 24, 32},
            new LostPage.Punishment[]{LostPage.Punishment.NONE, LostPage.Punishment.INFESTATION}
        );

        add("slime_gem", BeneathItems.RAW_SLIME,
            new int[]{200, 300, 400, 400, 500},
            TFCItems.GEMS.get(Ore.DIAMOND),
            new int[]{1, 1, 2, 2, 3},
            new LostPage.Punishment[]{LostPage.Punishment.WITHERING, LostPage.Punishment.BLAZE_INFERNO, LostPage.Punishment.UNKNOWN}
        );

        add("flesh_star", Items.ROTTEN_FLESH,
            new int[]{96, 128, 160},
            TFCItems.GEMS.get(Ore.RUBY),
            new int[]{1, 2, 2, 3},
            new LostPage.Punishment[]{LostPage.Punishment.WITHERING, LostPage.Punishment.UNKNOWN}
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
