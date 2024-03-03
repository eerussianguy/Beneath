package com.eerussianguy.beneath.common.blocks;

import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.misc.BeneathParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Helpers;

public class BurpingFlowerBlock extends FacingFlowerBlock
{
    public static final BooleanProperty MATURE = TFCBlockStateProperties.MATURE;

    public BurpingFlowerBlock(ExtendedProperties properties)
    {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(MATURE, false).setValue(FACING, Direction.NORTH));
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
    {
        if (meetsConditions(level, pos, state))
        {
            if (!state.getValue(MATURE))
            {
                level.setBlockAndUpdate(pos, state.setValue(MATURE, true));
            }
            else
            {
                performAction(level, pos, state, rand);
                Helpers.playSound(level, pos, SoundEvents.PLAYER_BURP);
                level.setBlockAndUpdate(pos, state.setValue(MATURE, false));
            }
        }
    }

    public boolean meetsConditions(Level level, BlockPos pos, BlockState state)
    {
        return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).getBlock() instanceof SulfurBlock;
    }

    public void performAction(ServerLevel level, BlockPos pos, BlockState state, RandomSource random)
    {
        final Direction dir = state.getValue(FACING);
        final BlockPos resultPos = pos.relative(dir);
        final BlockState stateAtResult = level.getBlockState(resultPos);
        level.sendParticles(BeneathParticles.SULFURIC_SMOKE.get(), resultPos.getX() + 0.5, resultPos.getY() + 0.5, resultPos.getZ() + 0.5, 5, 0, 0, 0, 1);

        boolean worked = true;
        if (level.getBlockEntity(resultPos.below()) instanceof SoulFarmlandBlockEntity farmland)
        {
            farmland.addUpToMinimumNutrient(SoulFarmlandBlockEntity.NutrientType.DECAY, 0.2f, 0.05f);
        }
        else if (Helpers.isBlock(stateAtResult, Blocks.NETHER_BRICKS))
        {
            level.setBlockAndUpdate(resultPos, BeneathBlocks.HELLBRICKS.get().defaultBlockState());
        }
        else if (Helpers.isBlock(stateAtResult, TFCBlocks.CHARCOAL_PILE.get()))
        {
            level.setBlockAndUpdate(resultPos, Helpers.copyProperties(BeneathBlocks.CURSECOAL_PILE.get().defaultBlockState(), stateAtResult));
        }
        else
        {
            worked = false;
        }
        if (worked && random.nextFloat() < 0.02f && meetsConditions(level, pos, state))
        {
            final BlockPos sulfur = pos.relative(state.getValue(FACING).getOpposite());
            level.setBlockAndUpdate(sulfur, level.getBlockState(sulfur).getFluidState().createLegacyBlock());
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(MATURE));
    }
}
