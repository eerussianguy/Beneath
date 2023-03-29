package com.eerussianguy.beneath.world;

import java.util.function.Function;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.world.feature.LargeNetherSpikesFeature;
import com.eerussianguy.beneath.world.feature.NetherBouldersFeature;
import com.eerussianguy.beneath.world.feature.NetherSpikeConfig;
import com.eerussianguy.beneath.world.feature.NetherSpikesFeature;
import com.eerussianguy.beneath.world.feature.WeightedStateConfig;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeneathFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Beneath.MOD_ID);

    public static final RegistryObject<NetherSpikesFeature> NETHER_SPIKES = register("nether_spikes", NetherSpikesFeature::new, NetherSpikeConfig.CODEC);
    public static final RegistryObject<LargeNetherSpikesFeature> LARGE_NETHER_SPIKES = register("large_nether_spikes", LargeNetherSpikesFeature::new, NetherSpikeConfig.CODEC);
    public static final RegistryObject<NetherBouldersFeature> NETHER_BOULDERS = register("nether_boulders", NetherBouldersFeature::new, WeightedStateConfig.CODEC);

    private static <F extends Feature<NoneFeatureConfiguration>> RegistryObject<F> register(String name, Function<Codec<NoneFeatureConfiguration>, F> factory)
    {
        return register(name, factory, NoneFeatureConfiguration.CODEC);
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<F> register(String name, Function<Codec<FC>, F> factory, Codec<FC> codec)
    {
        return FEATURES.register(name, () -> factory.apply(codec));
    }
}
