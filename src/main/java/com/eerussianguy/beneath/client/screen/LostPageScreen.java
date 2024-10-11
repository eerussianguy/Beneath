package com.eerussianguy.beneath.client.screen;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.container.LostPageContainer;
import com.eerussianguy.beneath.common.items.LostPageItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.client.screen.TFCContainerScreen;

public class LostPageScreen extends TFCContainerScreen<LostPageContainer>
{
    private static final ResourceLocation TEXTURE = Beneath.identifier("textures/gui/lost_page.png");

    public LostPageScreen(LostPageContainer container, Inventory playerInventory, Component name)
    {
        super(container, playerInventory, name, TEXTURE);
        imageHeight += 48;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
    {
        final ItemStack stack = menu.getTargetStack();
        if (stack.getItem() instanceof LostPageItem item)
        {
            final Ingredient costIngredient = item.getCost(stack);
            final ItemStack reward = item.getReward(stack);

            final ClientLevel level = Minecraft.getInstance().level;
            if (level == null)
                return;
            final ItemStack[] items = costIngredient.getItems();
            final ItemStack cost = items[(int) (level.getGameTime() / 20) % items.length];

            drawCenteredLine(graphics, Component.translatable("beneath.screen.lost_page.cost"), 16);
            drawCenteredLine(graphics, Component.literal(item.getCostAmount(stack) + "x ").append(cost.getHoverName()), 26);
            graphics.renderItem(cost, imageWidth / 2 - 8, 32);


            drawCenteredLine(graphics, Component.translatable("beneath.screen.lost_page.reward"), 64);
            drawCenteredLine(graphics, Component.literal(item.getRewardAmount(stack) + "x ").append(reward.getHoverName()), 74);
            graphics.renderItem(reward, imageWidth / 2 - 8, 82);

            drawCenteredLine(graphics, Component.translatable("beneath.screen.lost_page.punishment"), 112);
            drawCenteredLine(graphics, Beneath.translateEnum(item.getPunishment(stack)), 122);
        }
    }
}
