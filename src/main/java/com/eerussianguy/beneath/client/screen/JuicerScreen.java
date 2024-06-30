package com.eerussianguy.beneath.client.screen;

import com.eerussianguy.beneath.common.container.JuicerContainer;
import com.eerussianguy.beneath.common.items.JuicerItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import net.dries007.tfc.client.screen.TFCContainerScreen;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Tooltips;

public class JuicerScreen extends TFCContainerScreen<JuicerContainer>
{
    public JuicerScreen(JuicerContainer container, Inventory playerInventory, Component name)
    {
        super(container, playerInventory, name, INVENTORY_1x1);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y)
    {
        super.renderLabels(graphics, x, y);
        menu.getInventory().getStackInSlot(0).getCapability(Capabilities.FLUID_ITEM).ifPresent(cap -> {
            if (!cap.getFluidInTank(0).isEmpty())
            {
                drawCenteredLine(graphics, Tooltips.fluidUnitsAndCapacityOf(cap.getFluidInTank(0), JuicerItem.CAPACITY), 14);
            }
        });
    }
}
