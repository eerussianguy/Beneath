package com.eerussianguy.beneath.world.feature;

import java.util.stream.Stream;
import com.eerussianguy.beneath.world.BeneathPlacementModifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import net.dries007.tfc.world.Codecs;

public class HeightLimitPlacement extends PlacementModifier
{
    public static final Codec<HeightLimitPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("min").forGetter(c -> c.min),
        Codecs.POSITIVE_INT.fieldOf("max").forGetter(c -> c.max))
        .apply(instance, HeightLimitPlacement::new));

    private final int min;
    private final int max;

    private HeightLimitPlacement(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext ctx, RandomSource random, BlockPos pos)
    {
        return pos.getY() >= min && pos.getY() <= max ? Stream.of(pos) : Stream.empty();
    }

    @Override
    public PlacementModifierType<?> type()
    {
        return BeneathPlacementModifiers.HEIGHT_LIMIT.get();
    }
}
