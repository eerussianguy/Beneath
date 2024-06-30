package com.eerussianguy.beneath.misc;

import java.util.Map;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.BeneathOre;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.SelfTests;

@SuppressWarnings("unused")
public class BeneathCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Beneath.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BENEATH = TABS.register("beneath", () -> CreativeModeTab.builder()
        .title(Component.translatable("beneath.creative_tab.beneath"))
        .icon(() -> new ItemStack(BeneathItems.CURSECOAL.get()))
        .displayItems(BeneathCreativeTabs::fillTab)
        .build());

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        accept(out, BeneathItems.BLACKSTONE_BRICK);
        accept(out, BeneathItems.CRACKRACK_ROCK);
        accept(out, BeneathItems.CRIMSON_STRAW);
        accept(out, BeneathItems.WARPED_STRAW);
        accept(out, BeneathItems.CURSED_HIDE);
        accept(out, BeneathItems.GOLD_CHUNK);
        accept(out, BeneathItems.CURSECOAL);
        accept(out, BeneathItems.RED_ELK_EGG);
        accept(out, BeneathItems.GHOST_PEPPER);
        BeneathItems.SHROOMS.values().forEach(shroom -> accept(out, shroom));
        BeneathItems.SEEDS.values().forEach(seed -> accept(out, seed));
        accept(out, BeneathItems.AGONIZING_FERTILIZER);
        BeneathItems.PURE_NUTRIENTS.values().forEach(fertilizer -> accept(out, fertilizer));
        accept(out, BeneathItems.JUICER);
        accept(out, BeneathBlocks.HAUNTED_SPIKE);
        accept(out, BeneathBlocks.GLOWSTONE_SPIKE);
        accept(out, BeneathBlocks.NETHER_PEBBLE);
        accept(out, BeneathBlocks.BLACKSTONE_PEBBLE);
        accept(out, BeneathBlocks.COBBLERACK);
        accept(out, BeneathBlocks.FUNGAL_COBBLERACK);
        accept(out, BeneathBlocks.BLACKSTONE_AQUEDUCT);
        accept(out, BeneathBlocks.SOUL_FARMLAND);
        accept(out, BeneathBlocks.CRIMSON_THATCH);
        accept(out, BeneathBlocks.WARPED_THATCH);
        accept(out, BeneathBlocks.SOUL_CLAY);
        accept(out, BeneathBlocks.CRACKRACK);
        accept(out, BeneathBlocks.HELLBRICKS);
        accept(out, BeneathBlocks.GLEAMFLOWER);
        accept(out, BeneathBlocks.BURPFLOWER);

        for (Stem wood : Stem.VALUES)
        {
            BeneathBlocks.WOODS.get(wood).values().forEach(reg -> {
                if (reg.get().asItem() != Items.AIR)
                {
                    accept(out, reg);
                }
            });
            accept(out, BeneathItems.LUMBER, wood);
            accept(out, BeneathItems.SUPPORTS, wood);
            accept(out, BeneathItems.BOATS, wood);
            accept(out, BeneathItems.CHEST_MINECARTS, wood);
            accept(out, BeneathItems.SIGNS, wood);
            for (Metal.Default metal : Metal.Default.values())
            {
                if (metal.hasUtilities())
                {
                    accept(out, BeneathItems.HANGING_SIGNS.get(wood).get(metal));
                }
            }
        }
        for (BeneathOre ore : BeneathOre.values())
        {
            for (Ore.Grade grade : Ore.Grade.values())
            {
                accept(out, BeneathBlocks.GRADED_ORES.get(ore).get(grade));
            }
        }
        BeneathBlocks.MINERALS.values().forEach(reg -> accept(out, reg));
    }

    private static <T extends ItemLike, R extends Supplier<T>, K1, K2> void accept(CreativeModeTab.Output out, Map<K1, Map<K2, R>> map, K1 key1, K2 key2)
    {
        if (map.containsKey(key1) && map.get(key1).containsKey(key2))
        {
            out.accept(map.get(key1).get(key2).get());
        }
    }

    private static <T extends ItemLike, R extends Supplier<T>, K> void accept(CreativeModeTab.Output out, Map<K, R> map, K key)
    {
        if (map.containsKey(key))
        {
            out.accept(map.get(key).get());
        }
    }

    private static <T extends ItemLike, R extends Supplier<T>> void accept(CreativeModeTab.Output out, R reg)
    {
        if (reg.get().asItem() == Items.AIR)
        {
            TerraFirmaCraft.LOGGER.error("BlockItem with no Item added to creative tab: " + reg.get().toString());
            SelfTests.reportExternalError();
            return;
        }
        out.accept(reg.get());
    }
}
