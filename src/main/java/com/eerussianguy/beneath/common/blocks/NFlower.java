package com.eerussianguy.beneath.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.plant.TFCBushBlock;
import net.dries007.tfc.util.Helpers;

public class NFlower extends TFCBushBlock
{
    public NFlower(ExtendedProperties properties)
    {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        return Helpers.isBlock(state.getBlock(), BeneathBlockTags.NETHER_BUSH_PLANTABLE_ON);
    }

}
