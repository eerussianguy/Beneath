package com.eerussianguy.beneath.misc;

import java.util.function.Function;
import com.eerussianguy.beneath.Beneath;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.client.particle.TFCParticles.Id;

public final class BeneathParticles
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Beneath.MOD_ID);

    public static final Id<SimpleParticleType> DECAY = register("decay");
    public static final Id<SimpleParticleType> DESTRUCTION = register("destruction");
    public static final Id<SimpleParticleType> SORROW = register("sorrow");
    public static final Id<SimpleParticleType> FLAME = register("flame");
    public static final Id<SimpleParticleType> DEATH = register("death");
    public static final Id<SimpleParticleType> SULFURIC_SMOKE = register("sulfuric_smoke");

    private static Id<SimpleParticleType> register(String name)
    {
        return new Id<>(PARTICLE_TYPES.register(name, () -> new SimpleParticleType(false)));
    }

    private static <O extends ParticleOptions> Id<ParticleType<O>> register(
        final String name,
        final Function<ParticleType<O>, MapCodec<O>> codec,
        final Function<ParticleType<O>, StreamCodec<? super RegistryFriendlyByteBuf, O>> streamCodec)
    {
        return new Id<>(PARTICLE_TYPES.register(name, () -> new ParticleType<O>(false)
        {
            @Override
            public MapCodec<O> codec()
            {
                return codec.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, O> streamCodec()
            {
                return streamCodec.apply(this);
            }
        }));
    }
}
