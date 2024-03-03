package com.eerussianguy.beneath.common.blocks;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.util.Helpers;

public class SoulClayBlock extends SoulSandBlock
{
    public static final BooleanProperty UP = BlockStateProperties.UP;

    public SoulClayBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(UP, false));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos)
    {
        if (facing == Direction.UP)
        {
            return state.setValue(UP, Helpers.isBlock(facingState, this));
        }
        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(UP, Helpers.isBlock(context.getLevel().getBlockState(context.getClickedPos().above()), this));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(UP));
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
    {
        final RandomSource r = level.random;
        if (r.nextFloat() < 0.05f)
        {
            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SOUL_FIRE.defaultBlockState()), pos.getX() + r.nextFloat(), pos.getY() + 1 + (0.5 * r.nextFloat()), pos.getZ() + r.nextFloat(), 0, 0, 0);
        }
    }
}
