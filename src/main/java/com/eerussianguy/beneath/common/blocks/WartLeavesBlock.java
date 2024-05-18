package com.eerussianguy.beneath.common.blocks;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.client.ClimateRenderCache;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;

public class WartLeavesBlock extends TFCLeavesBlock
{
    private final int color;

    public WartLeavesBlock(ExtendedProperties properties, int autumnIndex, @Nullable Supplier<? extends Block> fallenLeaves, @Nullable Supplier<? extends Block> fallenTwig, int color)
    {
        super(properties, autumnIndex, fallenLeaves, fallenTwig);
        this.color = color;
    }

    public int getColor()
    {
        return color;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (!state.getValue(PERSISTENT) && random.nextInt(30) == 0)
        {
            if (ClimateRenderCache.INSTANCE.getWind().lengthSquared() > 0.42f * 0.42f)
            {
                final BlockState belowState = level.getBlockState(pos.below());
                if (belowState.isAir())
                {
                    ParticleUtils.spawnParticleBelow(level, pos, random, new BlockParticleOption(TFCParticles.FALLING_LEAF.get(), state));
                }
            }
        }
    }
}
