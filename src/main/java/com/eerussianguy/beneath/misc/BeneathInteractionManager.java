package com.eerussianguy.beneath.misc;

import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.blocks.CharcoalPileBlock;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.InteractionManager;

public final class BeneathInteractionManager
{
    public static void init()
    {
        InteractionManager.register(Ingredient.of(BeneathItems.CURSECOAL.get()), false, (stack, context) -> {
            Player player = context.getPlayer();
            if (player != null && !player.getAbilities().mayBuild)
            {
                return InteractionResult.PASS;
            }
            else
            {
                final Level level = context.getLevel();
                final BlockPos pos = context.getClickedPos();
                final BlockState stateAt = level.getBlockState(pos);
                final Block pile = BeneathBlocks.CURSECOAL_PILE.get();
                if (player != null && (player.blockPosition().equals(pos) || (player.blockPosition().equals(pos.above()) && Helpers.isBlock(stateAt, pile) && stateAt.getValue(CharcoalPileBlock.LAYERS) == 8)))
                {
                    return InteractionResult.FAIL;
                }
                if (Helpers.isBlock(stateAt, pile))
                {
                    int layers = stateAt.getValue(CharcoalPileBlock.LAYERS);
                    if (layers != 8)
                    {
                        stack.shrink(1);
                        level.setBlockAndUpdate(pos, stateAt.setValue(CharcoalPileBlock.LAYERS, layers + 1));
                        Helpers.playSound(level, pos, TFCSounds.CHARCOAL.getPlaceSound());
                        return InteractionResult.SUCCESS;
                    }
                }
                if (level.isEmptyBlock(pos.above()) && stateAt.isFaceSturdy(level, pos, Direction.UP))
                {
                    stack.shrink(1);
                    level.setBlockAndUpdate(pos.above(), pile.defaultBlockState());
                    Helpers.playSound(level, pos, TFCSounds.CHARCOAL.getPlaceSound());
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.FAIL;
            }
        });

        InteractionManager.register(new BlockItemPlacement(TFCItems.POWDERS.get(Powder.SULFUR), BeneathBlocks.SULFUR));

        InteractionManager.register(Ingredient.of(Items.NETHER_WART), false, (stack, context) -> InteractionResult.PASS);
    }
}
