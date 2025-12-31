package com.eerussianguy.beneath.client.screen;

import com.eerussianguy.beneath.common.container.JuicerContainer;
import com.eerussianguy.beneath.common.items.JuicerItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.dries007.tfc.client.screen.TFCContainerScreen;
import net.dries007.tfc.util.tooltip.Tooltips;

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
        drawLine(graphics, Component.translatable("beneath.screen.juicer.mushrooms"), TextAlignment.CENTER, 16);

        final var cap = menu.getTargetStack().getCapability(Capabilities.FluidHandler.ITEM);
        if (cap != null)
        {
            if (!cap.getFluidInTank(0).isEmpty())
            {
                drawLine(graphics, Tooltips.fluidUnitsAndCapacityOf(cap.getFluidInTank(0), JuicerItem.CAPACITY), TextAlignment.CENTER, 55);
            }
        }
    }
}
