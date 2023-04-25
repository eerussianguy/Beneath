package com.eerussianguy.beneath.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;

public class BeneathTickCounterBlockEntity extends TickCounterBlockEntity
{
    public static void restart(LevelAccessor level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof TickCounterBlockEntity counter)
        {
            counter.resetCounter();
        }
    }

    public BeneathTickCounterBlockEntity(BlockPos pos, BlockState state)
    {
        super(BeneathBlockEntities.TICK_COUNTER.get(), pos, state);
    }

}
