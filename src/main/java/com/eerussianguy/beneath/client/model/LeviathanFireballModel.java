package com.eerussianguy.beneath.client.model;

import com.eerussianguy.beneath.common.entities.blaze_leviathan.BasicFireball;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LeviathanFireballModel extends EntityModel<BasicFireball>
{
    private final ModelPart bone;
    private final ModelPart bone2;
    private final ModelPart bb_main;

    public LeviathanFireballModel(ModelPart root)
    {
        this.bone = root.getChild("bone");
        this.bone2 = root.getChild("bone2");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 58).addBox(-3.0F, 2.0F, -10.0F, 6.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
            .texOffs(44, 12).addBox(-3.0F, -19.0F, -10.0F, 6.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
            .texOffs(0, 80).addBox(-3.0F, -19.0F, 10.0F, 6.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(76, 0).addBox(-3.0F, -19.0F, -12.0F, 6.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(36, 34).addBox(-3.0F, 4.0F, -12.0F, 6.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
            .texOffs(0, 32).addBox(-3.0F, -21.0F, -12.0F, 6.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
            .texOffs(68, 60).addBox(-3.0F, -19.0F, 10.0F, 6.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(52, 60).addBox(-3.0F, -19.0F, -12.0F, 6.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(BasicFireball entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        bb_main.yRot = ageInTicks * 0.1f;
        bone.yRot = ageInTicks * 0.1f;
        bone2.yRot = ageInTicks * 0.1f;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
