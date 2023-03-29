package com.eerussianguy.beneath.common.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class BossMonster extends Monster
{
    private static final EntityDataAccessor<Integer> DATA_INVULNERABLE = SynchedEntityData.defineId(BossMonster.class, EntityDataSerializers.INT);

    protected final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public BossMonster(EntityType<? extends Monster> type, Level level)
    {
        super(type, level);
        setHealth(getMaxHealth());
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(DATA_INVULNERABLE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putInt("Invul", getInvulnerableTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        setInvulnerableTicks(tag.getInt("Invul"));
        if (hasCustomName())
        {
            bossEvent.setName(getDisplayName());
        }
    }

    public int getInvulnerableTicks()
    {
        return entityData.get(DATA_INVULNERABLE);
    }

    public void setInvulnerableTicks(int ticks)
    {
        entityData.set(DATA_INVULNERABLE, ticks);
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        if (bypassInvulnerable(source))
        {
            return super.hurt(source, amount);
        }
        if (isInvulnerableTo(source) || (getInvulnerableTicks() > 0 && !source.equals(DamageSource.OUT_OF_WORLD)))
        {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player)
    {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player)
    {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    public boolean bypassInvulnerable(DamageSource source)
    {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return super.isInvulnerableTo(source) || source.equals(DamageSource.DROWN);
    }

    @Override
    public boolean canChangeDimensions()
    {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity)
    {
        return false;
    }

    @Override
    public MobType getMobType()
    {
        return MobType.UNDEAD;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    public void checkDespawn()
    {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful())
        {
            this.discard();
        }
        else
        {
            this.noActionTime = 0;
        }
    }
}
