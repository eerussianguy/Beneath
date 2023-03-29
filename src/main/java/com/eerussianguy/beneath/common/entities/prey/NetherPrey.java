package com.eerussianguy.beneath.common.entities.prey;

import java.util.Random;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.shapes.CollisionContext;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.entities.prey.Prey;
import net.dries007.tfc.util.Helpers;

public class NetherPrey extends Prey
{
    public static NetherPrey makeDeer(EntityType<? extends Prey> type, Level level)
    {
        return new NetherPrey(type, level, TFCSounds.DEER);
    }

    public static boolean checkSpawnRules(EntityType<? extends NetherPrey> type, LevelAccessor level, MobSpawnType spawn, BlockPos pos, Random rand)
    {
        final BlockPos.MutableBlockPos cursor = pos.mutable();
        do
        {
            cursor.move(0, 1, 0);
        }
        while(Helpers.isFluid(level.getFluidState(cursor), FluidTags.LAVA));

        return level.getBlockState(cursor).isAir();
    }

    public NetherPrey(EntityType<? extends Prey> type, Level level, TFCSounds.EntitySound sounds)
    {
        super(type, level, sounds);
        getNavigation().setCanFloat(false);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos)
    {
        this.checkInsideBlocks();
        if (this.isInLava())
        {
            this.resetFallDistance();
        }
        else
        {
            super.checkFallDamage(y, onGround, state, pos);
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level)
    {
        return level.isUnobstructed(this);
    }

    @Override
    public boolean canStandOnFluid(FluidState fluid)
    {
        return Helpers.isFluid(fluid, FluidTags.LAVA);
    }

    @Override
    public boolean isOnFire()
    {
        return false;
    }

    @Override
    public boolean isSensitiveToWater()
    {
        return true;
    }

    @Override
    protected PathNavigation createNavigation(Level level)
    {
        return new LavaPathNavigation(this, level);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level)
    {
        return Helpers.isFluid(level.getBlockState(pos).getFluidState(), FluidTags.LAVA) ? 10f : isInLava() ? Float.NEGATIVE_INFINITY : 0f;
    }

    @Override
    public void tick()
    {
        super.tick();
        floatOnFluid();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic)
    {
        return NetherPreyAi.makeBrain(brainProvider().makeBrain(dynamic));
    }

    private void floatOnFluid()
    {
        if (this.isInLava())
        {
            final CollisionContext context = CollisionContext.of(this);
            if (context.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !Helpers.isFluid(level.getFluidState(this.blockPosition().above()), FluidTags.LAVA))
            {
                this.onGround = true;
            }
            else
            {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
            }
        }
    }

    public static class LavaPathNavigation extends GroundPathNavigation
    {
        public LavaPathNavigation(Mob mob, Level level)
        {
            super(mob, level);
        }

        @Override
        protected PathFinder createPathFinder(int maxNodes)
        {
            this.nodeEvaluator = new WalkNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, maxNodes);
        }

        @Override
        protected boolean hasValidPathType(BlockPathTypes type)
        {
            return type == BlockPathTypes.LAVA || type == BlockPathTypes.DAMAGE_FIRE || type == BlockPathTypes.DANGER_FIRE || super.hasValidPathType(type);
        }

        @Override
        public boolean isStableDestination(BlockPos pos)
        {
            return Helpers.isFluid(level.getBlockState(pos).getFluidState(), FluidTags.LAVA) || super.isStableDestination(pos);
        }
    }
}
