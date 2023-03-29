package com.eerussianguy.beneath.common.entities.blaze_leviathan;

import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.common.entities.BossMonster;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BlazeLeviathanBoss extends BossMonster implements RangedAttackMob
{
    public static final float STAGE_2 = 0.5f;
    public static final float STAGE_3 = 0.2f;

    private static final EntityDataAccessor<Integer> FIREBALLS = SynchedEntityData.defineId(BlazeLeviathanBoss.class, EntityDataSerializers.INT);

    private float allowedHeightDifference = 0.5F;
    private int nextHeightGoalTick;

    public BlazeLeviathanBoss(EntityType<? extends BossMonster> type, Level level)
    {
        super(type, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0F);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0D).add(Attributes.MOVEMENT_SPEED, 0.6F).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.ARMOR, 4.0D);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(FIREBALLS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putInt("fireballs", getFireballs());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        setFireballs(tag.getInt("fireballs"));
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(2, new LeviathanRangedAttackGoal(this, 1.0D, 120, 20.0F));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    protected void customServerAiStep()
    {
        final float health = getHealth() / getMaxHealth();
        if (getInvulnerableTicks() > 0)
        {
            setInvulnerableTicks(getInvulnerableTicks() - 1);
        }
        if (health > 0.5f && getInvulnerableTicks() <= 0)
        {
            setInvulnerableTicks(100);
        }

        --this.nextHeightGoalTick;
        if (this.nextHeightGoalTick <= 0)
        {
            this.nextHeightGoalTick = 100;
            this.allowedHeightDifference = 0.5F + (float) this.random.nextGaussian() * 3.0F;
        }

        final LivingEntity target = getTarget();
        if (target != null && target.getEyeY() > getEyeY() + allowedHeightDifference && canAttack(target))
        {
            final Vec3 delta = getDeltaMovement();
            setDeltaMovement(getDeltaMovement().add(0.0D, (0.3F - delta.y) * 0.3F, 0.0D));
            hasImpulse = true;
        }

        super.customServerAiStep();

        if (tickCount % 20 == 0)
        {
            heal(1f);
        }
        if (tickCount % 200 == 0 && getFireballs() < 8)
        {
            addFireballs(1);
        }
        bossEvent.setProgress(health);
    }

    public Vec3 getFireballOffset(int id)
    {
        final float radians = id / 8f * Mth.TWO_PI;
        final double x = Mth.cos(radians) * 5;
        final double z = Mth.sin(radians) * 5;
        final double y = 7 + Mth.sin(tickCount * 0.02f);
        return new Vec3(x, y, z);
    }

    public Vec3 getFireballPos(int id)
    {
        return getFireballOffset(id).add(position());
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect)
    {
        return false;
    }

    @Override
    public boolean addEffect(MobEffectInstance instance, @Nullable Entity entity)
    {
        return false;
    }

    @Override
    public float getBrightness()
    {
        return 1f;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source)
    {
        return false;
    }

    @Override
    public boolean isOnFire()
    {
        return getHealth() < 10f;
    }

    @Override
    public boolean bypassInvulnerable(DamageSource source)
    {
        return source instanceof IndirectEntityDamageSource indirect && indirect.getDirectEntity() != null && indirect.getDirectEntity().getType().equals(BeneathEntities.LEVIATHAN_FIREBALL.get());
    }

    @Override
    public void aiStep()
    {
        if (!onGround && getDeltaMovement().y < 0.0D)
        {
            setDeltaMovement(getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }
        if (level.isClientSide)
        {
            if (random.nextInt(24) == 0 && !isSilent())
            {
                level.playLocalSound(getX() + 0.5D, getY() + 0.5D, getZ() + 0.5D, SoundEvents.BLAZE_BURN, getSoundSource(), 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
            for (int i = 0; i < 2; ++i)
            {
                level.addParticle(ParticleTypes.LARGE_SMOKE, getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }

        super.aiStep();
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor)
    {
        final int balls = getFireballs();
        if (balls > 0)
        {
            double minDist = Double.MAX_VALUE;
            Vec3 pos = Vec3.ZERO;
            for (int i = 0; i < balls; i++)
            {
                final Vec3 tryPos = getFireballPos(i);
                if (target.distanceToSqr(tryPos) < minDist)
                {
                    pos = tryPos;
                }
            }
            final double dx = target.getX() - pos.x;
            final double dy = target.getY() - pos.y;
            final double dz = target.getZ() - pos.z;
            final Vec3 delta = new Vec3(dx, dy, dz);
            final Vec3 m = delta.normalize().scale(1.5f);
            final BasicFireball projectile = new BasicFireball(BeneathEntities.LEVIATHAN_FIREBALL.get(), pos.x, pos.y, pos.z, m.x, m.y, m.z, level);
            projectile.setOwner(this);
            level.addFreshEntity(projectile);
            addFireballs(-1);
        }
    }

    public int getFireballs()
    {
        return entityData.get(FIREBALLS);
    }

    public void setFireballs(int amount)
    {
        entityData.set(FIREBALLS, amount);
    }

    public void addFireballs(int amount)
    {
        setFireballs(getFireballs() + amount);
    }

    static class LeviathanRangedAttackGoal extends RangedAttackGoal
    {
        private final BlazeLeviathanBoss mob;

        public LeviathanRangedAttackGoal(BlazeLeviathanBoss mob, double speedMod, int interval, float radius)
        {
            super(mob, speedMod, interval, radius);
            this.mob = mob;
        }

        @Override
        public boolean canUse()
        {
            return super.canUse() && mob.getFireballs() > 0;
        }
    }
}
