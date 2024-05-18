package com.eerussianguy.beneath.mixin;

import com.eerussianguy.beneath.common.blocks.WartLeavesBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.client.particle.FallingLeafParticle;

@Mixin(FallingLeafParticle.class)
public abstract class FallingLeafParticleMixin extends TextureSheetParticle
{
    protected FallingLeafParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ)
    {
        super(pLevel, pX, pY, pZ);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void inject$init(ClientLevel level, double x, double y, double z, SpriteSet set, boolean tinted, BlockState state, CallbackInfo ci)
    {
        final Block block = state.getBlock();
        if (block instanceof WartLeavesBlock wart)
        {
            final int color = wart.getColor();
            setColor((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F);
        }
    }
}
