package com.eerussianguy.beneath.common.blocks;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SoulClayBlock extends SoulSandBlock
{
    public SoulClayBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
    {
        final Random r = level.random;
        if (r.nextFloat() < 0.05f)
        {
            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SOUL_FIRE.defaultBlockState()), pos.getX() + r.nextFloat(), pos.getY() + 1 + (0.5 * r.nextFloat()), pos.getZ() + r.nextFloat(), 0, 0, 0);
        }
    }
}
