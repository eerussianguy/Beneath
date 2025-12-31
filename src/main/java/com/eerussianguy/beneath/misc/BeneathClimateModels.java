package com.eerussianguy.beneath.misc;

import com.eerussianguy.beneath.Beneath;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.ClimateModels;

public class BeneathClimateModels
{
    public static final DeferredRegister<ClimateModelType<?>> TYPES = DeferredRegister.create(ClimateModels.KEY, Beneath.MOD_ID);

    public static final ClimateModels.Id<NetherClimateModel> NETHER = register("nether", StreamCodec.unit(NetherClimateModel.INSTANCE));

    public static void registerModels()
    {
        NETHER.get();
    }

    private static <T extends ClimateModel> ClimateModels.Id<T> register(String id, StreamCodec<ByteBuf, T> codec)
    {
        return new ClimateModels.Id<>(TYPES.register(id, () -> new ClimateModelType<>(codec)));
    }

}
