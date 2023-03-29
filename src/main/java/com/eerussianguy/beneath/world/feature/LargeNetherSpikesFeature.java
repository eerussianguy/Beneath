package com.eerussianguy.beneath.world.feature;

import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LargeNetherSpikesFeature extends NetherSpikesFeature
{
    public LargeNetherSpikesFeature(Codec<NetherSpikeConfig> codec)
    {
        super(codec);
    }

    @Override
    public void place(WorldGenLevel level, BlockPos pos, BlockState spike, BlockState raw, Direction direction, Random random)
    {
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        final int height = 6 + random.nextInt(11);
        final int radius = 2 + random.nextInt(1);
        int maxHeightReached = 0;
        for (int y = -3; y <= height; y++)
        {
            float radiusSquared = radius * (1 - 1.5f * Math.abs(y) / height);
            if (radiusSquared < 0)
            {
                continue;
            }
            radiusSquared *= radiusSquared;
            for (int x = -radius; x <= radius; x++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    mutablePos.set(pos).move(x, y * direction.getStepY(), z);
                    float actualRadius = ((x * x) + (z * z)) / radiusSquared;
                    if (actualRadius < 0.7)
                    {
                        // Fill in actual blocks
                        replaceBlockWithoutFluid(level, mutablePos, raw);
                        if (x == 0 && z == 0)
                        {
                            maxHeightReached = y;
                        }
                    }
                    else if (actualRadius < 0.85 && random.nextBoolean())
                    {
                        // Only fill in if continuing downwards
                        if (level.getBlockState(mutablePos.offset(0, -direction.getStepY(), 0)) == raw)
                        {
                            replaceBlockWithoutFluid(level, mutablePos, raw);
                        }
                    }
                    else if (actualRadius < 1 && random.nextInt(3) == 0 && y > 0)
                    {
                        placeSmallSpike(level, mutablePos, spike, raw, direction, random);
                    }
                }
            }
        }
        mutablePos.set(pos).move(direction, maxHeightReached - 1);
        placeSmallSpike(level, mutablePos, spike, raw, direction, random, 1.0f);
    }
}
