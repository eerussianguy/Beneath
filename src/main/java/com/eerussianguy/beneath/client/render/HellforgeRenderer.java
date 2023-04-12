package com.eerussianguy.beneath.client.render;

import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.blockentities.CharcoalForgeBlockEntity;
import net.dries007.tfc.common.blocks.devices.CharcoalForgeBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;

public class HellforgeRenderer implements BlockEntityRenderer<HellforgeBlockEntity>
{
    @Override
    public void render(HellforgeBlockEntity forge, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        final BlockState state = forge.getBlockState();

        if (state.hasProperty(CharcoalForgeBlock.HEAT) && forge.getLevel() != null)
        {
            final float heat = (float) state.getValue(CharcoalForgeBlock.HEAT);
            final float partialTime = forge.getLevel().getGameTime() + partialTick;
            final ItemRenderer render = Minecraft.getInstance().getItemRenderer();

            forge.getCapability(Capabilities.ITEM).ifPresent(inv -> {
                for (int idx = 0; idx < HellforgeBlockEntity.ITEM_SLOTS; idx++)
                {
                    final ItemStack stack = inv.getStackInSlot(idx);
                    if (!stack.isEmpty())
                    {
                        final float input = partialTime * (heat / 35) + (idx * 360f / HellforgeBlockEntity.ITEM_SLOTS);
                        final double x = 0.5 + Mth.cos(input);
                        final double y = 15f / 16 + (0.0625 * Mth.cos(input));
                        final double z = 0.5 + Mth.sin(input);
                        poseStack.pushPose();

                        poseStack.translate(x, y, z);
                        poseStack.scale(0.3f, 0.3f, 0.3f);
                        poseStack.mulPose(RenderHelpers.rotateDegreesY(RenderHelpers.itemTimeRotation()));
                        render.renderStatic(stack, ItemTransforms.TransformType.NONE, packedLight, packedOverlay, poseStack, bufferSource, 0);

                        poseStack.popPose();
                    }
                    idx++;
                }

                for (int idx = HellforgeBlockEntity.SLOT_EXTRA_MIN; idx <= HellforgeBlockEntity.SLOT_EXTRA_MAX; idx++)
                {
                    final ItemStack stack = inv.getStackInSlot(idx);
                    if (!stack.isEmpty())
                    {
                        int i = idx - HellforgeBlockEntity.SLOT_EXTRA_MIN;
                        poseStack.pushPose();
                        poseStack.translate(0.5f + (i % 2 == 0 ? 0.25f : -0.25f), 15f / 16, 0.5f + (i < 2 ? 0.25f : -0.25f));
                        poseStack.scale(0.3f, 0.3f, 0.3f);
                        poseStack.mulPose(RenderHelpers.rotateDegreesY(RenderHelpers.itemTimeRotation()));
                        render.renderStatic(stack, ItemTransforms.TransformType.NONE, packedLight, packedOverlay, poseStack, bufferSource, 0);

                        poseStack.popPose();
                    }
                }
            });
        }

    }
}
