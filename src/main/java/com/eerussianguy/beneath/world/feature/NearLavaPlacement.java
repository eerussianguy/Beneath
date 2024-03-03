package com.eerussianguy.beneath.world.feature;

import java.util.Random;
import java.util.stream.Stream;
import com.eerussianguy.beneath.world.BeneathPlacementModifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.Codecs;

public class NearLavaPlacement extends PlacementModifier
{
    public static final Codec<NearLavaPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.optionalFieldOf("radius", 2).forGetter(c -> c.radius)
    ).apply(instance, NearLavaPlacement::new));

    private final int radius;

    public NearLavaPlacement(int radius)
    {
        this.radius = radius;
    }

    @Override
    public PlacementModifierType<?> type()
    {
        return BeneathPlacementModifiers.NEAR_LAVA.get();
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos)
    {
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int x = -radius; x <= radius; x++)
        {
            for (int z = -radius; z <= radius; z++)
            {
                for (int y = 0; y >= -radius; y--)
                {
                    mutablePos.set(pos).move(x, y, z);

                    final BlockState state = context.getBlockState(mutablePos);
                    if (Helpers.isFluid(state.getFluidState(), FluidTags.LAVA))
                    {
                        return Stream.of(pos);
                    }
                }
            }
        }
        return Stream.empty();
    }
}
