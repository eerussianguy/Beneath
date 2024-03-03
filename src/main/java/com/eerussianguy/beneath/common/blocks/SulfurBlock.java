package com.eerussianguy.beneath.common.blocks;

import com.eerussianguy.beneath.common.items.BeneathItemTags;
import com.eerussianguy.beneath.misc.BeneathDamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;

public class SulfurBlock extends BottomSupportedBlock
{
    public SulfurBlock(Properties properties)
    {
        super(properties, GroundcoverBlock.FLAT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void attack(BlockState state, Level level, BlockPos pos, Player player)
    {
        if (Helpers.isItem(player.getMainHandItem(), BeneathItemTags.SPARKS_ON_SULFUR))
        {
            level.removeBlock(pos, false);
            level.explode(null, BeneathDamageSources.sulfur(player), null, pos.getX() + 0.5, pos.getY() + 0.125, pos.getZ() + 0.5, 3f, true, Level.ExplosionInteraction.BLOCK);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        return TFCItems.POWDERS.get(Powder.SULFUR).get().getDefaultInstance();
    }
}
