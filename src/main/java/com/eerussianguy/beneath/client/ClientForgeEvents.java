package com.eerussianguy.beneath.client;


import java.util.List;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ClientForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ClientForgeEvents::onTooltip);
    }

    private static void onTooltip(ItemTooltipEvent event)
    {
        final List<Component> tooltip = event.getToolTip();
        final ItemStack item = event.getItemStack();

        final NetherFertilizer fert = NetherFertilizer.get(item);
        if (fert != null)
        {
            fert.addTooltipInfo(tooltip);
        }
    }
}
