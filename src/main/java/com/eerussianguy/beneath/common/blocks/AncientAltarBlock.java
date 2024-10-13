package com.eerussianguy.beneath.common.blocks;

import java.util.ArrayList;
import java.util.List;
import com.eerussianguy.beneath.common.blockentities.AncientAltarBlockEntity;
import com.eerussianguy.beneath.common.items.LostPageItem;
import com.eerussianguy.beneath.misc.LostPage;
import com.ibm.icu.impl.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.util.Helpers;

public class AncientAltarBlock extends DeviceBlock
{
    private static final VoxelShape SHAPE = box(1, 0, 1, 15, 15, 15);

    public AncientAltarBlock(ExtendedProperties properties)
    {
        super(properties, InventoryRemoveBehavior.DROP);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (level.getBlockEntity(pos) instanceof AncientAltarBlockEntity altar)
        {
            final IItemHandler inv = altar.getInventory();
            final ItemStack held = player.getItemInHand(hand);
            if (Helpers.isItem(held, Tags.Items.GEMS))
            {
                final ItemStack stack = inv.getStackInSlot(0);
                if (LostPage.choose(stack, level.getRandom()))
                {
                    held.shrink(1);
                    Helpers.playSound(level, pos, SoundEvents.ENCHANTMENT_TABLE_USE);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                else if (stack.getItem() instanceof LostPageItem page && page.hasInitialized(stack))
                {
                    int found = 0;
                    final Ingredient cost = page.getCost(stack);
                    final int costAmount = page.getCostAmount(stack);
                    final List<AncientAltarBlockEntity> altars = new ArrayList<>();
                    for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-5, 0, -5), pos.offset(5, 0, 5)))
                    {
                        if (level.getBlockEntity(checkPos) instanceof AncientAltarBlockEntity otherAltar && altar != otherAltar)
                        {
                            final ItemStack foundStack = otherAltar.getInventory().getStackInSlot(0);
                            if (cost.test(foundStack))
                            {
                                found += foundStack.getCount();
                                altars.add(otherAltar);
                                if (found >= costAmount)
                                    break;
                            }
                        }
                    }
                    if (found >= page.getCostAmount(stack))
                    {
                        for (AncientAltarBlockEntity otherAltar : altars)
                        {
                            otherAltar.getInventory().extractItem(0, 64, false);
                        }
                        int rewardAmount = page.getRewardAmount(stack);
                        if (!level.dimensionType().ultraWarm())
                            rewardAmount /= 2;
                        final ItemStack reward = page.getReward(stack);
                        while (rewardAmount > 0)
                        {
                            int count = Math.min(reward.getMaxStackSize(), rewardAmount);
                            Helpers.spawnItem(level, pos, reward.copyWithCount(count));
                            rewardAmount -= count;
                        }
                        held.shrink(1);
                        Helpers.playSound(level, pos, SoundEvents.ENCHANTMENT_TABLE_USE);
                        page.getPunishment(stack).administer(player, level, pos);
                    }
                    else
                    {
                        Helpers.playSound(level, pos, SoundEvents.GLASS_BREAK);
                        player.hurt(level.damageSources().magic(), 1f);
                        player.displayClientMessage(Component.translatable("beneath.sacrifice.error"), true);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            if (!inv.getStackInSlot(0).isEmpty())
            {
                ItemHandlerHelper.giveItemToPlayer(player, inv.extractItem(0, 64, false));
            }
            ItemHandlerHelper.giveItemToPlayer(player, inv.insertItem(0, held.split(64), false));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }
}
