package com.eerussianguy.beneath.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.fluids.FluidProperty;

public class LavaAqueductBlock extends AqueductBlock
{
    public static final FluidProperty FLUID_PROPERTY = BeneathStateProperties.LAVA;

    public LavaAqueductBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
    {
        if (state.getValue(getFluidProperty()).getFluid().isSame(Fluids.LAVA) && !entity.fireImmune() && entity instanceof LivingEntity living && !EnchantmentHelper.hasFrostWalker(living))
        {
            entity.hurt(level.damageSources().hotFloor(), 1.0F);
        }
        super.stepOn(level, pos, state, entity);
    }


    @Nullable
    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity)
    {
        return state.getValue(FLUID_PROPERTY).getFluid().isSame(Fluids.LAVA) ? BlockPathTypes.DAMAGE_FIRE : null;
    }

    @Override
    public FluidProperty getFluidProperty()
    {
        return FLUID_PROPERTY;
    }
}
