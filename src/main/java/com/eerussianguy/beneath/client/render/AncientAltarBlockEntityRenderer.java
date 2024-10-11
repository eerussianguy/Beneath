package com.eerussianguy.beneath.client.render;

import com.eerussianguy.beneath.common.blockentities.AncientAltarBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.dries007.tfc.client.RenderHelpers;

public class AncientAltarBlockEntityRenderer implements BlockEntityRenderer<AncientAltarBlockEntity>
{
    @Override
    public void render(AncientAltarBlockEntity altar, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        final ItemStack stack = altar.getInventory().getStackInSlot(0);
        final Level level = altar.getLevel();
        if (stack.isEmpty() || level == null)
            return;
        poseStack.pushPose();

        poseStack.translate(0.5f, 1.25f, 0.5f);
        poseStack.scale(0.6f, 0.6f, 0.6f);
        poseStack.mulPose(Axis.YP.rotationDegrees(RenderHelpers.itemTimeRotation()));
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, level, 0);

        poseStack.popPose();
    }
}
