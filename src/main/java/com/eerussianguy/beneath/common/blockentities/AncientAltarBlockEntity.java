package com.eerussianguy.beneath.common.blockentities;

import java.util.ArrayList;
import java.util.List;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.items.LostPageItem;
import com.eerussianguy.beneath.common.items.TomeItem;
import com.eerussianguy.beneath.common.component.LostPage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
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
import org.jetbrains.annotations.Nullable;

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
                case 2 -> ParticleTypes.MYCELIUM;
                default -> ParticleTypes.ENCHANT;
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

    public void playEffect(int successType, @Nullable SoundEvent sound)
    {
        assert level != null;
        tick = 40;
        success = successType;
        if (sound != null)
            Helpers.playSound(level, worldPosition, sound);
        markForSync();
    }

    public void playSuccess()
    {
        playEffect(1, SoundEvents.ENCHANTMENT_TABLE_USE);
    }

    public void playFail()
    {
        playEffect(0, SoundEvents.GLASS_BREAK);
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
            else if (stack.getItem() instanceof TomeItem tome && !tome.hasEnchantment(stack))
            {
                if (tome.roll(stack, level.registryAccess(), level.getRandom(), gatherTools(pos)))
                {
                    held.shrink(1);
                    playEffect(3, SoundEvents.ENCHANTMENT_TABLE_USE);
                    markForSync();
                }
                else
                {
                    playFail();
                    player.displayClientMessage(Component.translatable("beneath.enchant.no_enchantments"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            else if (stack.isEnchantable())
            {
                return enchant(player, held, stack, pos);
            }
            else if (stack.getItem() instanceof LostPageItem page && page.hasInitialized(stack))
            {
                int found = 0;
                final Ingredient cost = page.getCost(stack);
                final int costAmount = page.getCostAmount(stack);
                final List<AncientAltarBlockEntity> altars = new ArrayList<>();
                for (AncientAltarBlockEntity otherAltar : neighborAltars(pos))
                {
                    final ItemStack foundStack = otherAltar.getStack();
                    if (cost.test(foundStack))
                    {
                        found += foundStack.getCount();
                        altars.add(otherAltar);
                        if (found >= costAmount)
                            break;
                    }
                }
                if (found >= page.getCostAmount(stack))
                {
                    int toCost = page.getCostAmount(stack);
                    for (AncientAltarBlockEntity otherAltar : altars)
                    {
                        otherAltar.playEffect(2, null);
                        final int toExtract = Math.min(toCost, otherAltar.getStack().getCount());
                        if (toExtract > 0)
                        {
                            otherAltar.consume(toExtract);
                            toCost -= toExtract;
                        }
                        if (toCost <= 0)
                            break;
                    }
                    final LostPage.Punishment punishment = page.getPunishment(stack);
                    int rewardAmount = page.getRewardAmount(stack);
                    if (!level.dimensionType().ultraWarm())
                        rewardAmount = Math.max(1, rewardAmount / 2);
                    if (punishment == LostPage.Punishment.BLESSING)
                        rewardAmount *= 2;
                    final ItemStack reward = page.getReward(stack);
                    if (punishment == LostPage.Punishment.GREED)
                    {
                        // Greed destroys the page on the altar and yields no reward
                        Helpers.playSound(level, pos, SoundEvents.GHAST_SCREAM);
                        inv.extractItem(0, 64, false);
                        markForSync();
                    }
                    else
                    {
                        while (rewardAmount > 0)
                        {
                            int count = Math.min(reward.getMaxStackSize(), rewardAmount);
                            Helpers.spawnItem(level, pos, reward.copyWithCount(count));
                            rewardAmount -= count;
                        }
                    }
                    held.shrink(1);
                    if (punishment != LostPage.Punishment.GREED)
                    {
                        playSuccess();
                    }
                    else
                    {
                        playFail();
                    }
                    punishment.administer(player, level, pos);
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

    public ItemStack getStack()
    {
        return getInventory().getStackInSlot(0);
    }

    private void consume(int amount)
    {
        assert level != null;
        getInventory().extractItem(0, amount, false);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    private List<AncientAltarBlockEntity> neighborAltars(BlockPos pos)
    {
        assert level != null;
        final List<AncientAltarBlockEntity> altars = new ArrayList<>();
        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-5, 0, -5), pos.offset(5, 0, 5)))
        {
            if (level.getBlockEntity(checkPos) instanceof AncientAltarBlockEntity otherAltar && this != otherAltar)
                altars.add(otherAltar);
        }
        return altars;
    }

    private List<ItemStack> gatherTools(BlockPos pos)
    {
        final List<ItemStack> tools = new ArrayList<>();
        for (AncientAltarBlockEntity otherAltar : neighborAltars(pos))
        {
            final ItemStack toolStack = otherAltar.getStack();
            if (toolStack.isEnchantable())
            {
                tools.add(toolStack);
                if (tools.size() >= 2)
                    break;
            }
        }
        return tools;
    }

    /**
     * Applies the enchantments of any rolled tomes on the surrounding altars to the tool held in this altar. Each tome
     * that can validly enchant the tool is consumed. Consumes the catalyst gem only if at least one enchant was applied.
     */
    private ItemInteractionResult enchant(Player player, ItemStack held, ItemStack tool, BlockPos pos)
    {
        assert level != null;
        final List<AncientAltarBlockEntity> tomeAltars = new ArrayList<>();
        final List<AncientAltarBlockEntity> offeringAltars = new ArrayList<>();
        for (AncientAltarBlockEntity otherAltar : neighborAltars(pos))
        {
            final ItemStack otherStack = otherAltar.getStack();
            if (otherStack.getItem() instanceof TomeItem tome && tome.hasEnchantment(otherStack))
            {
                tomeAltars.add(otherAltar);
            }
            else if (!otherStack.isEmpty())
            {
                offeringAltars.add(otherAltar);
            }
        }
        if (tomeAltars.isEmpty())
        {
            playFail();
            player.displayClientMessage(Component.translatable("beneath.enchant.no_tomes"), true);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        int applied = 0;
        boolean unaffordable = false;
        for (AncientAltarBlockEntity tomeAltar : tomeAltars)
        {
            final ItemStack tomeStack = tomeAltar.getStack();
            final TomeItem tome = (TomeItem) tomeStack.getItem();
            final Holder<Enchantment> enchantment = tome.getEnchantment(tomeStack);
            final Holder<Item> offering = tome.getOffering(tomeStack);
            if (enchantment == null || offering == null || !tool.supportsEnchantment(enchantment) || !isCompatible(tool, enchantment))
                continue;
            if (!payCost(offeringAltars, offering.value(), tome.getCost(tomeStack)))
            {
                unaffordable = true;
                continue;
            }
            tool.enchant(enchantment, tome.getLevel(tomeStack));
            final LostPage.Punishment punishment = tome.getPunishment(tomeStack);
            if (punishment != null && punishment != LostPage.Punishment.NONE)
                punishment.administer(player, level, pos);
            applied++;
            if (tome.spendUse(tomeStack))
                tomeAltar.consume(1);
            tomeAltar.playEffect(2, null);
        }

        if (applied > 0)
        {
            held.shrink(1);
            playSuccess();
            markForSync();
        }
        else
        {
            playFail();
            player.displayClientMessage(Component.translatable(unaffordable ? "beneath.enchant.no_offering" : "beneath.enchant.incompatible"), true);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * Whether {@code candidate} may coexist with every enchantment already on the tool. This rejects mutually exclusive
     * enchantments (and, since {@link Enchantment#areCompatible} treats an enchantment as incompatible with itself,
     * duplicates of an already-applied enchantment). As {@code tool.enchant} mutates the tool in place, this also guards
     * against conflicts between tomes applied earlier in the same batch.
     */
    private static boolean isCompatible(ItemStack tool, Holder<Enchantment> candidate)
    {
        for (Holder<Enchantment> existing : tool.getEnchantments().keySet())
        {
            if (!Enchantment.areCompatible(existing, candidate))
                return false;
        }
        return true;
    }

    private boolean payCost(List<AncientAltarBlockEntity> offeringAltars, Item costItem, int amount)
    {
        assert level != null;
        final List<AncientAltarBlockEntity> matching = new ArrayList<>();
        int found = 0;
        for (AncientAltarBlockEntity altar : offeringAltars)
        {
            final ItemStack offering = altar.getStack();
            if (Helpers.isItem(offering, costItem))
            {
                found += offering.getCount();
                matching.add(altar);
            }
        }
        if (found < amount)
            return false;
        int remaining = amount;
        for (AncientAltarBlockEntity altar : matching)
        {
            if (remaining <= 0)
                break;
            final int take = Math.min(remaining, altar.getStack().getCount());
            altar.consume(take);
            remaining -= take;
            altar.playEffect(2, null);
        }
        return true;
    }

}
