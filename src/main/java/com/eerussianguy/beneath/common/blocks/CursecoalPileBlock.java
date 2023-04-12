package com.eerussianguy.beneath.common.blocks;

import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import net.dries007.tfc.common.blocks.CharcoalPileBlock;

public class CursecoalPileBlock extends CharcoalPileBlock
{
    public CursecoalPileBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        return new ItemStack(BeneathItems.CURSECOAL.get());
    }
}
