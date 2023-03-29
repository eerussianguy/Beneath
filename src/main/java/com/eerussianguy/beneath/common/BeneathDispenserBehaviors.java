package com.eerussianguy.beneath.common;

import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.world.level.block.DispenserBlock;

import static net.dries007.tfc.util.DispenserBehaviors.*;

public final class BeneathDispenserBehaviors
{
    public static void registerDispenseBehaviors()
    {
        BeneathItems.CHEST_MINECARTS.values().forEach(reg -> DispenserBlock.registerBehavior(reg.get(), MINECART_BEHAVIOR));
    }
}
