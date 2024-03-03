package com.eerussianguy.beneath.common.blocks;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.CharcoalForgeBlock;

public class HellforgeSideBlock extends CharcoalForgeBlock
{
    @Nullable
    public static BlockPos getCenterPos(LevelAccessor level, BlockPos pos)
    {
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                cursor.setWithOffset(pos, x, 0, z);
                if (level.getBlockState(cursor).getBlock() instanceof HellforgeBlock)
                {
                    return cursor.immutable();
                }
            }
        }
        return null;
    }

    public HellforgeSideBlock(ExtendedProperties properties)
    {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand)
    {
        if (getCenterPos(level, pos) == null)
        {
            level.setBlockAndUpdate(pos, state.setValue(HEAT, 0));
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos)
    {
        return state;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        final BlockPos center = getCenterPos(level, pos);
        if (center != null)
        {
            final BlockState centerState = level.getBlockState(center);
            return centerState.use(level, player, hand, result.withPosition(center));
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand)
    {
        if (rand.nextFloat() < 0.5f)
        {
            super.animateTick(state, level, pos, rand);
        }
    }

    @Override
    public void intakeAir(Level level, BlockPos pos, BlockState state, int amount)
    {
        final BlockPos center = getCenterPos(level, pos);
        if (center != null)
        {
            final BlockState centerState = level.getBlockState(center);
            if (centerState.getBlock() instanceof HellforgeBlock hellForge)
            {
                hellForge.intakeAir(level, center, centerState, amount);
            }
        }
    }
}
