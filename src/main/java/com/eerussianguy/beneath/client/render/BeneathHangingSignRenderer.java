package com.eerussianguy.beneath.client.render;

import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

import net.dries007.tfc.client.render.blockentity.TFCHangingSignBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.TFCSignBlockEntityRenderer;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

public class BeneathHangingSignRenderer extends TFCHangingSignBlockEntityRenderer
{
    static
    {
        BeneathBlocks.CEILING_HANGING_SIGNS.forEach((wood, map) -> map.forEach((metal, reg) -> registerData(reg.get(), createModelData(metal, reg))));
        BeneathBlocks.WALL_HANGING_SIGNS.forEach((wood, map) -> map.forEach((metal, reg) -> registerData(reg.get(), createModelData(metal, reg))));
    }

    private static HangingSignModelData createModelData(Metal.Default metal, Supplier<? extends SignBlock> reg)
    {
        final WoodType type = reg.get().type();
        final ResourceLocation woodName = new ResourceLocation(type.name());
        final ResourceLocation metalName = Helpers.identifier(metal.getSerializedName());

        return new HangingSignModelData(
            new Material(Sheets.SIGN_SHEET, new ResourceLocation(woodName.getNamespace(), "entity/signs/hanging/" + metalName.getPath() + "/" + woodName.getPath())),
            new ResourceLocation(type.name() + ".png").withPrefix("textures/gui/hanging_signs/" + metalName.getPath() + "/")
        );
    }

    public BeneathHangingSignRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context, BeneathBlocks.WOODS.keySet()
            .stream()
            .map(map -> new TFCSignBlockEntityRenderer.SignModelData(
                Beneath.MOD_ID,
                map.getSerializedName(),
                map.getVanillaWoodType()
            )));
    }
}
