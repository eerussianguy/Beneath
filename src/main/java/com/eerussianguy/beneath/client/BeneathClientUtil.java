package com.eerussianguy.beneath.client;

import com.eerussianguy.beneath.client.screen.LostPageScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public final class BeneathClientUtil
{
    public static void openLostPageScreen(ItemStack stack)
    {
        Minecraft.getInstance().setScreen(new LostPageScreen(stack));
    }
}
