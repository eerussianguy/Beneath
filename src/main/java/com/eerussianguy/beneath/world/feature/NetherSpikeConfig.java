package com.eerussianguy.beneath.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import net.dries007.tfc.world.Codecs;

public record NetherSpikeConfig(BlockState raw, BlockState spike) implements FeatureConfiguration
{
    public static final Codec<NetherSpikeConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.BLOCK_STATE.fieldOf("raw").forGetter(NetherSpikeConfig::raw),
        Codecs.BLOCK_STATE.fieldOf("spike").forGetter(NetherSpikeConfig::spike)
    ).apply(instance, NetherSpikeConfig::new));
}
