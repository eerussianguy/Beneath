package com.eerussianguy.beneath.client;


import java.util.List;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public final class ClientForgeEvents
{
    public static void init(IEventBus bus)
    {
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
