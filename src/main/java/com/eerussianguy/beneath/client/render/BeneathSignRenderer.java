package com.eerussianguy.beneath.client.render;

import com.eerussianguy.beneath.client.BeneathClientUtil;
import com.eerussianguy.beneath.common.blocks.Stem;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import net.dries007.tfc.client.render.blockentity.TFCSignBlockEntityRenderer;

public class BeneathSignRenderer extends TFCSignBlockEntityRenderer
{
    static
    {
        for (Stem wood : Stem.VALUES)
            TFCSignBlockEntityRenderer.MODELS.put(
                wood.getVanillaWoodType(),
                context -> new SignModel(context.bakeLayer(BeneathClientUtil.layerId("sign/" + wood.getSerializedName())))
            );
    }
    public BeneathSignRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }
}
