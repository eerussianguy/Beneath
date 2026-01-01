package com.eerussianguy.beneath.common.component;

import com.eerussianguy.beneath.Beneath;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.component.TFCComponents.Id;

public final class BeneathComponents
{
    public static final DeferredRegister<DataComponentType<?>> COMPONENT = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Beneath.MOD_ID);

    public static final Id<LostPageComponent> LOST_PAGE = register("lost_page", LostPageComponent.CODEC, LostPageComponent.STREAM_CODEC);

    private static <T> Id<T> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec)
    {
        return new Id<>(COMPONENT.register(name, () -> new DataComponentType.Builder<T>()
            .persistent(codec)
            .networkSynchronized(streamCodec)
            .build()));
    }
}
