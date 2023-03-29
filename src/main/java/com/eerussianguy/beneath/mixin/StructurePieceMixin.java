package com.eerussianguy.beneath.mixin;

import java.util.Random;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;

@Mixin(StructurePiece.class)
public class StructurePieceMixin
{

    @Inject(method = "createChest(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Ljava/util/Random;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    private void inject$createChest(ServerLevelAccessor level, BoundingBox box, Random random, BlockPos pos, ResourceLocation lootTable, @Nullable BlockState state, CallbackInfoReturnable<Boolean> cir)
    {
        if (level.dimensionType().ultraWarm() && (state == null || Helpers.isBlock(state, Blocks.CHEST)))
        {
            if (box.isInside(pos) && !(level.getBlockState(pos).getBlock() instanceof ChestBlock))
            {
                BlockState toPlace = BeneathBlocks.WOODS.get(random.nextBoolean() ? Stem.CRIMSON : Stem.WARPED).get(Wood.BlockType.CHEST).get().defaultBlockState();
                toPlace = StructurePiece.reorient(level, pos, toPlace);

                level.setBlock(pos, toPlace, 2);
                if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest)
                {
                    chest.setLootTable(lootTable, random.nextLong());
                }
                cir.cancel();
            }
        }
    }

}
