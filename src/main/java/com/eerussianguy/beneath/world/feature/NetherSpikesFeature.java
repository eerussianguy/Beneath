package com.eerussianguy.beneath.world.feature;

import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.util.Helpers;

public class NetherSpikesFeature extends Feature<NetherSpikeConfig>
{
    public NetherSpikesFeature(Codec<NetherSpikeConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NetherSpikeConfig> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();
        final RandomSource random = context.random();
        final NetherSpikeConfig config = context.config();

        // The direction that the spike is pointed
        Direction direction = random.nextBoolean() ? Direction.UP : Direction.DOWN;
        BlockState wallState = level.getBlockState(pos.relative(direction.getOpposite()));
        if (isValidWallRock(wallState))
        {
            this.place(level, pos, config.spike(), config.raw(), direction, random);
        }
        else
        {
            // Switch directions and try again
            direction = direction.getOpposite();
            wallState = level.getBlockState(pos.relative(direction));
            if (isValidWallRock(wallState))
            {
                this.place(level, pos, config.spike(), config.raw(), direction, random);
            }
        }
        return true;
    }

    protected void place(WorldGenLevel level, BlockPos pos, BlockState spike, BlockState raw, Direction direction, RandomSource random)
    {
        this.placeSmallSpike(level, pos, spike, raw, direction, random);
    }

    protected void replaceBlock(WorldGenLevel level, BlockPos pos, BlockState state)
    {
        final Block block = level.getBlockState(pos).getBlock();
        if (block == Blocks.AIR)
        {
            this.setBlock(level, pos, state);
        }
        else if (block != Blocks.WATER && block != TFCBlocks.RIVER_WATER.get())
        {
            if (block == Blocks.LAVA)
            {
                this.setBlock(level, pos, state.setValue(RockSpikeBlock.FLUID, RockSpikeBlock.FLUID.keyFor(Fluids.LAVA)));
            }
        }
        else
        {
            this.setBlock(level, pos, state.setValue(RockSpikeBlock.FLUID, RockSpikeBlock.FLUID.keyFor(Fluids.WATER)));
        }

    }

    protected void replaceBlockWithoutFluid(WorldGenLevel level, BlockPos pos, BlockState state)
    {
        Block block = level.getBlockState(pos).getBlock();
        if (block == Blocks.AIR || block == Blocks.WATER || block == TFCBlocks.RIVER_WATER.get() || block == Blocks.LAVA)
        {
            this.setBlock(level, pos, state);
        }
    }

    protected void placeSmallSpike(WorldGenLevel level, BlockPos pos, BlockState spike, BlockState raw, Direction direction, RandomSource random)
    {
        this.placeSmallSpike(level, pos, spike, raw, direction, random, random.nextFloat());
    }

    protected void placeSmallSpike(WorldGenLevel level, BlockPos pos, BlockState spike, BlockState raw, Direction direction, RandomSource random, float sizeWeight)
    {
        BlockPos above = pos.above();
        BlockState stateAbove = level.getBlockState(pos.above());
        if (Helpers.isBlock(stateAbove, BlockTags.BASE_STONE_NETHER))
        {
            level.setBlock(above, raw, 2);
        }

        if (sizeWeight < 0.2F)
        {
            this.replaceBlock(level, pos, spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.MIDDLE));
            this.replaceBlock(level, pos.relative(direction, 1), spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.TIP));
        }
        else if (sizeWeight < 0.7F)
        {
            this.replaceBlock(level, pos, spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.BASE));
            this.replaceBlock(level, pos.relative(direction, 1), spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.MIDDLE));
            this.replaceBlock(level, pos.relative(direction, 2), spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.TIP));
        }
        else
        {
            this.replaceBlockWithoutFluid(level, pos, raw);
            this.replaceBlock(level, pos.relative(direction, 1), spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.BASE));
            this.replaceBlock(level, pos.relative(direction, 2), spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.MIDDLE));
            this.replaceBlock(level, pos.relative(direction, 3), spike.setValue(RockSpikeBlock.PART, RockSpikeBlock.Part.TIP));
        }

    }

    private boolean isValidWallRock(BlockState state)
    {
        return Helpers.isBlock(state, Blocks.NETHERRACK);
    }
}
