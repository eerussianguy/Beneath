package com.eerussianguy.beneath.providers;

import com.eerussianguy.beneath.misc.BeneathDamageSources;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public class BuiltinDamageTypes
{
    private final BootstrapContext<DamageType> context;

    public BuiltinDamageTypes(BootstrapContext<DamageType> context)
    {
        this.context = context;

        register(BeneathDamageSources.SULFUR);
    }

    private void register(ResourceKey<DamageType> type)
    {
        context.register(type, new DamageType(type.location().getNamespace() + "." + type.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, DeathMessageType.DEFAULT));
    }

}
