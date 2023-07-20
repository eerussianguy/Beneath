package com.eerussianguy.beneath.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.TFCSignBlockEntity;

public class BeneathSignBlockEntity extends TFCSignBlockEntity
{
    public BeneathSignBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return BeneathBlockEntities.SIGN.get();
    }
}
