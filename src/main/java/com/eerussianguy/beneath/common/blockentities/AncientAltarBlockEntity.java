package com.eerussianguy.beneath.common.blockentities;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;

public class AncientAltarBlockEntity extends InventoryBlockEntity<ItemStackHandler>
{

    public AncientAltarBlockEntity(BlockPos pos, BlockState state)
    {
        super(BeneathBlockEntities.ANCIENT_ALTAR.get(), pos, state, defaultInventory(1), Beneath.blockEntityName("ancient_altar"));
    }

    public IItemHandler getInventory()
    {
        return inventory;
    }
}
