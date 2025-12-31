package com.eerussianguy.beneath.client.render;

import java.util.function.Function;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.client.BeneathClientUtil;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;

import net.dries007.tfc.client.render.blockentity.TFCHangingSignBlockEntityRenderer;

public class BeneathHangingSignRenderer extends TFCHangingSignBlockEntityRenderer
{
    static
    {
        final var map = TFCHangingSignBlockEntityRenderer.MODELS;
        BeneathBlocks.CEILING_HANGING_SIGNS.forEach((wood, m) -> m.forEach((metal, block) -> {
            final var model = new Provider<Function<BlockEntityRendererProvider.Context, HangingSignModel>>(
                new Material(
                    Sheets.SIGN_SHEET,
                    Beneath.identifier("entity/signs/hanging/" + metal.getSerializedName() + "/" + wood.getSerializedName())
                ),
                Beneath.identifier(wood.getSerializedName() + ".png").withPrefix("textures/gui/hanging_signs/" + metal.getSerializedName() + "/"),
                context -> new HangingSignModel(context.bakeLayer(BeneathClientUtil.layerId("hanging_sign/" + wood.getSerializedName())))
            );

            map.put(block.get(), model);
            map.put(BeneathBlocks.WALL_HANGING_SIGNS.get(wood).get(metal).get(), model);
        }));}

    public BeneathHangingSignRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }
}
