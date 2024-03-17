package com.eerussianguy.beneath.mixin;

import com.eerussianguy.beneath.client.render.BeneathHangingSignRenderer;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.client.render.blockentity.TFCHangingSignBlockEntityRenderer;

@Mixin(TFCHangingSignBlockEntityRenderer.class)
public abstract class TFCHangingSignBlockEntityRendererMixin
{
    @Inject(method = "getData", at = @At("HEAD"), remap = false, cancellable = true)
    private static void inject$getData(Block block, CallbackInfoReturnable<TFCHangingSignBlockEntityRenderer.HangingSignModelData> cir)
    {
        if (cir.getReturnValue() == null)
        {
            final var data = BeneathHangingSignRenderer.getData(block);
            if (data != null)
            {
                cir.setReturnValue(data);
            }
        }
    }
}
