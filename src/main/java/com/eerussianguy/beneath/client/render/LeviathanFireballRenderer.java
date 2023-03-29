package com.eerussianguy.beneath.client.render;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.client.model.LeviathanFireballModel;
import com.eerussianguy.beneath.common.entities.blaze_leviathan.BasicFireball;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import net.dries007.tfc.client.RenderHelpers;

public class LeviathanFireballRenderer extends EntityRenderer<BasicFireball>
{
    public static final ResourceLocation TEXTURE = Beneath.identifier("textures/entity/leviathan_fireball.png");

    private final LeviathanFireballModel model;

    public LeviathanFireballRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        this.model = new LeviathanFireballModel(RenderHelpers.bakeSimple(context, "leviathan_fireball"));
    }

    @Override
    protected int getBlockLightLevel(BasicFireball entity, BlockPos pose)
    {
        return 15;
    }

    @Override
    public void render(BasicFireball entity, float yaw, float pitch, PoseStack poseStack, MultiBufferSource buffer, int light)
    {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0f, -1f, 0f);
        final VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.setupAnim(entity, 0f, 0f, entity.tickCount, yaw, pitch);
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, yaw, pitch, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(BasicFireball entity)
    {
        return TEXTURE;
    }
}
