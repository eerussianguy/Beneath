package com.eerussianguy.beneath.misc;

import java.util.Map;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
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
import net.dries007.tfc.util.SelfTests;

public class BeneathCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Beneath.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BENEATH = TABS.register("beneath", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.beneath.beneath"))
        .icon(() -> new ItemStack(BeneathItems.CURSECOAL.get()))
        .displayItems(BeneathCreativeTabs::fillTab)
        .build());

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        accept(out, BeneathItems.CURSECOAL);
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
            TerraFirmaCraft.LOGGER.error("BlockItem with no Item added to creative tab: " + reg);
            SelfTests.reportExternalError();
            return;
        }
        out.accept(reg.get());
    }
}
