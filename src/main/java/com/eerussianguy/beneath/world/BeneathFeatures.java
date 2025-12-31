package com.eerussianguy.beneath.world;

import java.util.function.Function;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.world.feature.LargeNetherSpikesFeature;
import com.eerussianguy.beneath.world.feature.NetherBouldersFeature;
import com.eerussianguy.beneath.world.feature.NetherSpikeConfig;
import com.eerussianguy.beneath.world.feature.NetherSpikesFeature;
import com.eerussianguy.beneath.world.feature.WeightedStateConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.world.feature.TFCFeatures.Id;

public class BeneathFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Beneath.MOD_ID);

    public static final Id<NetherSpikesFeature> NETHER_SPIKES = register("nether_spikes", NetherSpikesFeature::new, NetherSpikeConfig.CODEC);
    public static final Id<LargeNetherSpikesFeature> LARGE_NETHER_SPIKES = register("large_nether_spikes", LargeNetherSpikesFeature::new, NetherSpikeConfig.CODEC);
    public static final Id<NetherBouldersFeature> NETHER_BOULDERS = register("nether_boulders", NetherBouldersFeature::new, WeightedStateConfig.CODEC);

    private static <C extends FeatureConfiguration, F extends Feature<C>> Id<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return new Id<>(FEATURES.register(name, () -> factory.apply(codec)));
    }
}
