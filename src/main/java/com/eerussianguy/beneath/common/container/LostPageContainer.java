package com.eerussianguy.beneath.common.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import net.dries007.tfc.common.container.Container;

public class LostPageContainer extends Container
{
    private final ItemStack item;

    public LostPageContainer(int windowId, FriendlyByteBuf buf)
    {
        super(BeneathContainerTypes.LOST_PAGE_CONTAINER.get(), windowId);
        item = buf.readItem();
    }

    public ItemStack getTargetStack()
    {
        return item;
    }
}
