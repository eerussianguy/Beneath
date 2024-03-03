package com.eerussianguy.beneath.world.feature;

import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.world.noise.Metaballs3D;

public class NetherBouldersFeature extends Feature<WeightedStateConfig>
{
    public NetherBouldersFeature(Codec<WeightedStateConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<WeightedStateConfig> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();
        final RandomSource random = context.random();
        final IWeighted<BlockState> weighted = context.config().weighted();
        return place(level, pos, weighted, random);
    }

    private boolean place(WorldGenLevel level, BlockPos pos, IWeighted<BlockState> states, RandomSource random)
    {
        final BlockState stateAt = level.getBlockState(pos);
        if (Helpers.isBlock(stateAt, BlockTags.LEAVES) || Helpers.isBlock(stateAt, BlockTags.LOGS))
        {
            return false;
        }
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        final int size = 6 + random.nextInt(4);
        final Metaballs3D noise = new Metaballs3D(Helpers.fork(random), 6, 8, -0.12f * size, 0.3f * size, 0.3f * size);

        if (pos.getY() + size > 127)
        {
            return false;
        }
        for (int x = -size; x <= size; x++)
        {
            for (int y = -size; y <= size; y++)
            {
                for (int z = -size; z <= size; z++)
                {
                    if (noise.inside(x, y, z))
                    {
                        mutablePos.setWithOffset(pos, x, y, z);
                        setBlock(level, mutablePos, states.get(random));
                    }
                }
            }
        }
        return true;
    }
}
