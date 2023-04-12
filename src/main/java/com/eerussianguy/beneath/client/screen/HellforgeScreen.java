package com.eerussianguy.beneath.client.screen;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import com.eerussianguy.beneath.common.container.HellforgeContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.screen.BlockEntityScreen;
import net.dries007.tfc.common.capabilities.heat.Heat;

public class HellforgeScreen extends BlockEntityScreen<HellforgeBlockEntity, HellforgeContainer>
{
    private static final ResourceLocation HELLFORGE = Beneath.identifier("textures/gui/hellforge.png");

    public HellforgeScreen(HellforgeContainer container, Inventory playerInventory, Component name)
    {
        super(container, playerInventory, name, HELLFORGE);
        this.inventoryLabelY += 20;
        this.imageHeight += 20;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);
        int width = (int) (105 * blockEntity.getTemperature() / Heat.maxVisibleTemperature());
        if (width > 0)
        {
            final TextureAtlasSprite sprite = RenderHelpers.getAndBindFluidSprite(new FluidStack(Fluids.LAVA, 100));
            RenderHelpers.fillAreaWithSprite(poseStack, sprite, leftPos + 27, topPos + 77, width, 19, 16, 16);
        }
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
    {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        //this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

}
