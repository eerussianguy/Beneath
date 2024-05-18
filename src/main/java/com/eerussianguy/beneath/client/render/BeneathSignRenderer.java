package com.eerussianguy.beneath.client.render;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import net.dries007.tfc.client.render.blockentity.TFCSignBlockEntityRenderer;
import net.dries007.tfc.common.blocks.wood.Wood;

public class BeneathSignRenderer extends TFCSignBlockEntityRenderer
{
    public BeneathSignRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context, BeneathBlocks.WOODS.keySet().stream().map(wood -> new SignModelData(Beneath.MOD_ID, wood.getSerializedName(), wood.getVanillaWoodType())));
    }
}
