package com.eerussianguy.beneath.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.common.entities.TFCFallingBlockEntity;
import net.dries007.tfc.util.Helpers;

@Mixin(TFCFallingBlockEntity.class)
public abstract class TFCFallingBlockEntityMixin extends FallingBlockEntity
{
    public TFCFallingBlockEntityMixin(EntityType<? extends FallingBlockEntity> type, Level level)
    {
        super(type, level);
    }

    @Inject(method = "placeAsBlockOrDropAsItem", at = @At("TAIL"), remap = false)
    private void inject$placeAsBlockOrDropAsItem(BlockState hitBlockState, BlockPos posAt, BlockState fallingBlockState, CallbackInfo ci)
    {
        if (Helpers.isBlock(fallingBlockState, Blocks.GLOWSTONE))
        {
            level.setBlock(posAt, level.getBlockState(posAt).getFluidState().createLegacyBlock(), 3);
            Helpers.playSound(level, posAt, SoundType.GLASS.getBreakSound());
            final int count = random.nextInt(0, 3);
            if (count > 0)
            {
                Helpers.spawnItem(level, posAt, new ItemStack(Items.GLOWSTONE_DUST, count));
            }
        }
    }
}
