package com.eerussianguy.beneath.misc;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class BeneathDamageSources
{
    public static final ResourceKey<DamageType> SULFUR = ResourceKey.create(Registries.DAMAGE_TYPE, Beneath.identifier("sulfur"));

    public static DamageSource sulfur(Entity entity)
    {
        return new DamageSource(fetch(SULFUR, entity.level()));
    }

    private static Holder<DamageType> fetch(ResourceKey<DamageType> type, Level level)
    {
        return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type);
    }


}
