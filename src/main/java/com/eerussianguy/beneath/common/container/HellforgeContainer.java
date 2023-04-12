package com.eerussianguy.beneath.common.container;

import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.CallbackSlot;

public class HellforgeContainer extends BlockEntityContainer<HellforgeBlockEntity>
{
    public static HellforgeContainer create(HellforgeBlockEntity forge, Inventory playerInventory, int windowId)
    {
        return new HellforgeContainer(forge, windowId).init(playerInventory, 20);
    }

    private HellforgeContainer(HellforgeBlockEntity blockEntity, int windowId)
    {
        super(BeneathContainerTypes.HELLFORGE_CONTAINER.get(), windowId, blockEntity);

        addDataSlots(blockEntity.getSyncableData());
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex))
            {
                case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, HellforgeBlockEntity.SLOT_EXTRA_MIN,  HellforgeBlockEntity.SLOT_EXTRA_MAX + 1, false)
                    && !moveItemStackTo(stack, 0, HellforgeBlockEntity.ITEM_SLOTS, false);
                case CONTAINER -> !moveItemStackTo(stack, containerSlots, slots.size(), false);
            };
    }

    @Override
    protected void addContainerSlots()
    {
        blockEntity.getCapability(Capabilities.ITEM).ifPresent(handler -> {
            int index = 0;
            for (int x = 27; x <= 117; x += 18)
            {
                for (int y = 21; y <= 57; y += 18)
                {
                    addSlot(new CallbackSlot(blockEntity, handler, index, x, y));
                    index += 1;
                }
            }

            for (int y = 16; y <= 70; y += 18)
            {
                addSlot(new CallbackSlot(blockEntity, handler, index, 151, y));
                index += 1;
            }
        });


    }
}
