package com.eerussianguy.beneath.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.world.Codecs;

public record WeightedStateConfig(IWeighted<BlockState> weighted) implements FeatureConfiguration
{
    public static final Codec<WeightedStateConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.weightedCodec(Codecs.BLOCK_STATE, "block").fieldOf("blocks").forGetter(WeightedStateConfig::weighted)
    ).apply(instance, WeightedStateConfig::new));
}
