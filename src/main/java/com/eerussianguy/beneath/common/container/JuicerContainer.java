package com.eerussianguy.beneath.common.container;

import com.eerussianguy.beneath.common.items.BeneathItemTags;
import com.eerussianguy.beneath.common.items.JuicerItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.container.CallbackSlot;
import net.dries007.tfc.common.container.ISlotCallback;
import net.dries007.tfc.common.container.ItemStackContainer;
import net.dries007.tfc.util.Helpers;

public class JuicerContainer extends ItemStackContainer implements ISlotCallback
{
    public static JuicerContainer create(ItemStack stack, InteractionHand hand, int slot, Inventory playerInv, int windowId)
    {
        return new JuicerContainer(windowId, playerInv, stack, hand, slot).init(playerInv);
    }

    private final IItemHandlerModifiable inventory;

    public JuicerContainer(int windowId, Inventory playerInv, ItemStack stack, InteractionHand hand, int slot)
    {
        super(BeneathContainerTypes.JUICER_CONTAINER.get(), windowId, playerInv, stack, hand, slot);
        this.inventory = new InventoryItemHandler(this, 1);
    }

    @Override
    public void broadcastChanges()
    {
        stack.getCapability(Capabilities.FLUID_ITEM).ifPresent(cap -> {
            final ItemStack stack = inventory.getStackInSlot(0);
            if (!stack.isEmpty() && cap.getFluidInTank(0).getAmount() < JuicerItem.CAPACITY)
            {
                final int filled = cap.fill(new FluidStack(Fluids.WATER, stack.getCount() * 50), IFluidHandler.FluidAction.EXECUTE);
                stack.shrink(Mth.ceil(filled / 50f));
                player.playSound(SoundEvents.BUCKET_EMPTY);
            }
        });
        super.broadcastChanges();
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex))
            {
                case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, 0, 1, false);
                case CONTAINER -> !moveItemStackTo(stack, containerSlots, slots.size(), false);
            };
    }

    @Override
    public void removed(Player player)
    {
        if (!player.level().isClientSide())
        {
            final ItemStack stack = inventory.getStackInSlot(0);
            if (!stack.isEmpty())
            {
                giveItemStackToPlayerOrDrop(player, stack);
            }
        }
        super.removed(player);
    }

    @Override
    protected void addContainerSlots()
    {
        addSlot(new CallbackSlot(this, inventory, 0, 80, 34));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return Helpers.isItem(stack, BeneathItemTags.USABLE_IN_JUICER);
    }

    public IItemHandlerModifiable getInventory()
    {
        return inventory;
    }
}
