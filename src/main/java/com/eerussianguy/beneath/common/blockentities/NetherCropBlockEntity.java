package com.eerussianguy.beneath.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.CropBlockEntity;

public class NetherCropBlockEntity extends CropBlockEntity
{
    public NetherCropBlockEntity(BlockPos pos, BlockState state)
    {
        this(BeneathBlockEntities.NETHER_CROP.get(), pos, state);
    }

    public NetherCropBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

}
