package com.eerussianguy.beneath.common.entities.blaze_leviathan;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;

public class BasicFireball extends AbstractHurtingProjectile
{
    private int explosionPower = 1;

    public BasicFireball(EntityType<? extends AbstractHurtingProjectile> type, Level level)
    {
        super(type, level);
    }

    public BasicFireball(EntityType<? extends AbstractHurtingProjectile> type, double x, double y, double z, double dx, double dy, double dz, Level level)
    {
        super(type, x, y, z, dx, dy, dz, level);
    }

    public BasicFireball(EntityType<? extends AbstractHurtingProjectile> type, LivingEntity owner, double x, double y, double z, Level level)
    {
        super(type, owner, x, y, z, level);
    }

    @Override
    public void tick()
    {
        super.tick();
    }

    @Override
    protected void onHit(HitResult result)
    {
        super.onHit(result);
        if (!this.level.isClientSide)
        {
            final boolean grief = ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
            this.level.explode(null, this.getX(), this.getY(), this.getZ(), (float) this.explosionPower, grief, grief ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result)
    {
        super.onHitEntity(result);
        if (!this.level.isClientSide)
        {
            final Entity hitEntity = result.getEntity();
            final Entity ownerEntity = getOwner();
            hitEntity.hurt(BasicFireball.fireball(this, ownerEntity), getDamage());
            if (canPerformThorns() && ownerEntity instanceof LivingEntity livingOwner)
            {
                this.doEnchantDamageEffects(livingOwner, hitEntity);
            }
        }
    }

    public boolean canPerformThorns()
    {
        return true;
    }

    public float getDamage()
    {
        return 6f;
    }

    public void setExplosionPower(int power)
    {
        explosionPower = power;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putByte("ExplosionPower", (byte) this.explosionPower);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        this.explosionPower = tag.contains("ExplosionPower", Tag.TAG_ANY_NUMERIC) ? tag.getByte("ExplosionPower") : 1;
    }

    @Override
    public boolean isPickable()
    {
        return true;
    }

    public static DamageSource fireball(BasicFireball fireball, @Nullable Entity indirect)
    {
        return indirect == null ? (new IndirectEntityDamageSource("onFire", fireball, fireball)).setIsFire().setProjectile() : (new IndirectEntityDamageSource("fireball", fireball, indirect)).setIsFire().setProjectile();
    }
}
