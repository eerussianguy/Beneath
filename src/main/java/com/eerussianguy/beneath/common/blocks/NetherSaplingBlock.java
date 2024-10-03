package com.eerussianguy.beneath.common.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.world.feature.tree.TFCTreeGrower;

public class NetherSaplingBlock extends TFCSaplingBlock
{
    public NetherSaplingBlock(TFCTreeGrower tree, ExtendedProperties properties, Supplier<Integer> days, boolean sand)
    {
        super(tree, properties, days, sand);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(7) == 0)
        {
            if (!level.isAreaLoaded(pos, 1))
            {
                return;
            }
            if (level.getBlockEntity(pos) instanceof TickCounterBlockEntity counter)
            {
                if (counter.getTicksSinceUpdate() > ICalendar.TICKS_IN_DAY *  getDaysToGrow() * TFCConfig.SERVER.globalSaplingGrowthModifier.get())
                {
                    this.advanceTree(level, pos, state.setValue(STAGE, 1), random);
                }
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        return Helpers.isBlock(state.getBlock(), BeneathBlockTags.NETHER_BUSH_PLANTABLE_ON);
    }
}
