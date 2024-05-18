package com.eerussianguy.beneath.mixin;

import com.eerussianguy.beneath.common.blocks.WartLeavesBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.client.particle.LeafParticle;

@Mixin(LeafParticle.class)
public abstract class LeafParticleMixin extends TextureSheetParticle
{
    protected LeafParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ)
    {
        super(pLevel, pX, pY, pZ);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void inject$init(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ, boolean tinted, CallbackInfo ci)
    {
        final BlockPos pos = BlockPos.containing(x, y, z);
        final Block block = level.getBlockState(pos).getBlock();
        if (block instanceof WartLeavesBlock wart)
        {
            final int color = wart.getColor();
            setColor((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F);
        }
    }

}
