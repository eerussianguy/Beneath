package com.eerussianguy.beneath.common.blockentities;

import java.util.ArrayList;
import java.util.List;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.items.LostPageItem;
import com.eerussianguy.beneath.common.component.LostPage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.util.Helpers;

public class AncientAltarBlockEntity extends InventoryBlockEntity<ItemStackHandler>
{
    public static void tickBothSides(Level level, BlockPos pos, BlockState state, AncientAltarBlockEntity altar)
    {
        if (altar.tick < 0)
            return;

        if (level.isClientSide)
        {
            final double x = pos.getX() + 0.5;
            final double y = pos.getY() + 1;
            final double z = pos.getZ() + 0.5;

            final float partial = altar.tick / 20f * Mth.TWO_PI;
            final float r = 0.5f + (0.01f * altar.tick);

            final ParticleOptions particle = switch (altar.success)
            {
                case 0 -> ParticleTypes.WHITE_ASH;
                case 1 -> ParticleTypes.CHERRY_LEAVES;
                default -> ParticleTypes.MYCELIUM;
            };
            level.addParticle(particle, x + Mth.sin(partial) * r, y + ((altar.success == 2 ? 1 : 2) * altar.tick / 20f), z + Mth.cos(partial) * r, Helpers.triangle(level.random, 0.2f), -0.2f, Helpers.triangle(level.random, 0.2f));
            if (altar.tick == 0)
            {
                for (int i = 0; i < 10; i++)
                {
                    level.addParticle(ParticleTypes.SMOKE, x, y + 0.125, z, level.random.nextGaussian() * 0.05, 0.005, level.random.nextGaussian() * 0.05);
                }
            }
        }
        if (altar.tick == 0)
        {
            Helpers.playSound(level, pos, SoundEvents.FIREWORK_ROCKET_TWINKLE);
        }

        altar.tick--;
    }

    private int tick = -1;
    private int success = 0;

    public AncientAltarBlockEntity(BlockPos pos, BlockState state)
    {
        super(BeneathBlockEntities.ANCIENT_ALTAR.get(), pos, state, defaultInventory(1), Beneath.MOD_ID);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider access)
    {
        super.loadAdditional(nbt, access);
        tick = nbt.getInt("tick");
        success = nbt.getInt("success");
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider access)
    {
        super.saveAdditional(nbt, access);
        nbt.putInt("tick", tick);
        nbt.putInt("success", success);
    }

    public void playSuccess()
    {
        assert level != null;
        tick = 40;
        success = 1;
        Helpers.playSound(level, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE);
        markForSync();
    }

    public void playFail()
    {
        assert level != null;
        tick = 40;
        success = 0;
        Helpers.playSound(level, worldPosition, SoundEvents.GLASS_BREAK);
        markForSync();
    }

    public void playSecondarySuccess()
    {
        assert level != null;
        tick = 40;
        success = 2;
        markForSync();
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        super.setAndUpdateSlots(slot);
    }

    public ItemInteractionResult use(Player player, InteractionHand hand)
    {
        if (tick > 0)
            return ItemInteractionResult.FAIL;
        assert level != null;
        final BlockPos pos = worldPosition;
        final IItemHandler inv = getInventory();
        final ItemStack held = player.getItemInHand(hand);
        if (Helpers.isItem(held, Tags.Items.GEMS))
        {
            final ItemStack stack = inv.getStackInSlot(0);
            if (LostPage.choose(stack, level.getRandom()))
            {
                held.shrink(1);
                playSuccess();
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            else if (stack.getItem() instanceof LostPageItem page && page.hasInitialized(stack))
            {
                int found = 0;
                final Ingredient cost = page.getCost(stack);
                final int costAmount = page.getCostAmount(stack);
                final List<AncientAltarBlockEntity> altars = new ArrayList<>();
                for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-5, 0, -5), pos.offset(5, 0, 5)))
                {
                    if (level.getBlockEntity(checkPos) instanceof AncientAltarBlockEntity otherAltar && this != otherAltar)
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
                    int toCost = page.getCostAmount(stack);
                    for (AncientAltarBlockEntity otherAltar : altars)
                    {
                        otherAltar.playSecondarySuccess();
                        final int toExtract = Math.min(toCost, otherAltar.getInventory().getStackInSlot(0).getCount());
                        if (toExtract > 0)
                        {
                            otherAltar.getInventory().extractItem(0, toExtract, false);
                            toCost -= toExtract;
                            level.sendBlockUpdated(otherAltar.getBlockPos(), otherAltar.getBlockState(), otherAltar.getBlockState(), Block.UPDATE_CLIENTS);
                        }
                        if (toCost <= 0)
                            break;
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
                    playSuccess();
                    page.getPunishment(stack).administer(player, level, pos);
                }
                else
                {
                    player.hurt(level.damageSources().magic(), 1f);
                    playFail();
                    player.displayClientMessage(Component.translatable("beneath.sacrifice.error"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // regular interaction
        if (!inv.getStackInSlot(0).isEmpty())
        {
            ItemHandlerHelper.giveItemToPlayer(player, inv.extractItem(0, 64, false));
        }
        ItemHandlerHelper.giveItemToPlayer(player, inv.insertItem(0, held.split(64), false));
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

}
