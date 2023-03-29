package com.eerussianguy.beneath.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.common.recipes.CollapseRecipe;

@Mixin(Slime.class)
public class SlimeMixin
{

    @Inject(method = "spawnCustomParticles", at = @At("HEAD"), remap = false)
    private void inject$spawnCustomParticles(CallbackInfoReturnable<Boolean> cir)
    {
        final Slime slime = (Slime) (Object) this;
        final int size = Mth.clamp(slime.getSize(), 1, 127);
        if (size > 1 && slime.getRandom().nextInt(128) < size)
        {
            CollapseRecipe.tryTriggerCollapse(slime.level, slime.blockPosition().below());
        }
    }

}
