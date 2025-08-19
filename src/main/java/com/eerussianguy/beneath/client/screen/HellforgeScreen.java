package com.eerussianguy.beneath.client.screen;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import com.eerussianguy.beneath.common.container.HellforgeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.screen.BlockEntityScreen;
import net.dries007.tfc.common.blockentities.CharcoalForgeBlockEntity;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.config.TemperatureDisplayStyle;

public class HellforgeScreen extends BlockEntityScreen<HellforgeBlockEntity, HellforgeContainer>
{
    private static final ResourceLocation HELLFORGE = Beneath.identifier("textures/gui/hellforge.png");

    public HellforgeScreen(HellforgeContainer container, Inventory playerInventory, Component name)
    {
        super(container, playerInventory, name, HELLFORGE);
        this.inventoryLabelY += 20;
        this.imageHeight += 20;
        this.titleLabelY += 1;
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);
        final int width = (int) Mth.clamp(blockEntity.getTemperature() / Heat.maxVisibleTemperature() * 105f, 0, 105);
        if (width > 0)
        {
            final TextureAtlasSprite sprite = RenderHelpers.getAndBindFluidSprite(new FluidStack(Fluids.LAVA, 100));
            RenderHelpers.fillAreaWithSprite(poseStack, sprite, leftPos + 27, topPos + 77, width, 19, 16, 16);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
    {
        graphics.drawString(Minecraft.getInstance().font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        //this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY)
    {
        super.renderTooltip(graphics, mouseX, mouseY);
        if (RenderHelpers.isInside(mouseX, mouseY, leftPos + 27, topPos + 77, 105, 19))
        {
            final MutableComponent text = TFCConfig.CLIENT.heatTooltipStyle.get().formatColored(this.blockEntity.getTemperature());
            if (text != null)
            {
                graphics.renderTooltip(this.font, text, mouseX, mouseY);
            }
        }

    }

}
