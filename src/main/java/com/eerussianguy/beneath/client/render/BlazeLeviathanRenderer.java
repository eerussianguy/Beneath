package com.eerussianguy.beneath.client.render;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.client.model.LeviathanFireballModel;
import com.eerussianguy.beneath.common.entities.blaze_leviathan.BlazeLeviathanBoss;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import net.dries007.tfc.client.RenderHelpers;

public class BlazeLeviathanRenderer extends MobRenderer<BlazeLeviathanBoss, BlazeModel<BlazeLeviathanBoss>>
{
    private static final ResourceLocation TEXTURE = Beneath.identifier("textures/entity/blaze_leviathan.png");
    private static final ResourceLocation TEXTURE_2 = Beneath.identifier("textures/entity/blaze_leviathan_2.png");
    private static final ResourceLocation TEXTURE_3 = Beneath.identifier("textures/entity/blaze_leviathan_3.png");

    private final LeviathanFireballModel ball;

    public BlazeLeviathanRenderer(EntityRendererProvider.Context context)
    {
        super(context, new BlazeModel<>(RenderHelpers.bakeSimple(context, "blaze_leviathan")), 1f);
        this.ball = new LeviathanFireballModel(RenderHelpers.bakeSimple(context, "leviathan_fireball"));
    }

    @Override
    public void render(BlazeLeviathanBoss entity, float yaw, float pitch, PoseStack poseStack, MultiBufferSource buffer, int light)
    {
        super.render(entity, yaw, pitch, poseStack, buffer, light);
        model.setupAnim(entity, 0f, 0f, entity.tickCount, yaw, pitch);
        final RenderType rt = RenderType.entityCutout(LeviathanFireballRenderer.TEXTURE);
        for (int i = 0; i < entity.getFireballs(); i++)
        {
            final Vec3 pos = entity.getFireballPos(i);
            poseStack.pushPose();
            poseStack.translate(pos.x - entity.getX(), pos.y - entity.getY(), pos.z - entity.getZ());
            final MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
            final VertexConsumer consumer = buffers.getBuffer(rt);
            this.ball.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1f);
            buffers.endBatch();
            poseStack.popPose();
        }
    }

    @Override
    protected int getBlockLightLevel(BlazeLeviathanBoss entity, BlockPos pos)
    {
        return 15;
    }

    @Override
    protected void scale(BlazeLeviathanBoss entity, PoseStack poseStack, float partialTicks)
    {
        final float scale = 5f;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(BlazeLeviathanBoss entity)
    {
        final float health = entity.getHealth() / entity.getMaxHealth();
        if (health > BlazeLeviathanBoss.STAGE_2)
        {
            return TEXTURE;
        }
        if (health > BlazeLeviathanBoss.STAGE_3)
        {
            return TEXTURE_2;
        }
        return TEXTURE_3;
    }
}
