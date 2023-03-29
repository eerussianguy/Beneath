package com.eerussianguy.beneath.misc;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.world.damagesource.DamageSource;

public class BeneathDamageSources
{
    public static final DamageSource SULFUR = new DamageSource("sulfur");

    private static DamageSource create(String key)
    {
        return new DamageSource(Beneath.MOD_ID + "." + key);
    }

}
