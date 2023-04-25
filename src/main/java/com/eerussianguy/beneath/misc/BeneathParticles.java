package com.eerussianguy.beneath.misc;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BeneathParticles
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Beneath.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DECAY = register("decay");
    public static final RegistryObject<SimpleParticleType> DESTRUCTION = register("destruction");
    public static final RegistryObject<SimpleParticleType> SORROW = register("sorrow");
    public static final RegistryObject<SimpleParticleType> FLAME = register("flame");
    public static final RegistryObject<SimpleParticleType> DEATH = register("death");
    public static final RegistryObject<SimpleParticleType> SULFURIC_SMOKE = register("sulfuric_smoke");

    private static RegistryObject<SimpleParticleType> register(String name)
    {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(false));
    }
}
