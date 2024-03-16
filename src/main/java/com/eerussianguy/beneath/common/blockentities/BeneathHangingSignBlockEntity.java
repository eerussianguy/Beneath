package com.eerussianguy.beneath.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BeneathHangingSignBlockEntity extends HangingSignBlockEntity
{
    public BeneathHangingSignBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return BeneathBlockEntities.HANGING_SIGN.get();
    }
}
