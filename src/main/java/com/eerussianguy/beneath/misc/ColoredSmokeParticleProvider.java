package com.eerussianguy.beneath.misc;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.LargeSmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class ColoredSmokeParticleProvider extends LargeSmokeParticle.Provider
{
    private final float r;
    private final float g;
    private final float b;

    public ColoredSmokeParticleProvider(SpriteSet set, float r, float g, float b)
    {
        super(set);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public LargeSmokeParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz)
    {
        final LargeSmokeParticle part = (LargeSmokeParticle) super.createParticle(type, level, x, y, z, dx, dy, dz);
        if (part != null)
        {
            part.setColor(r, g, b);
        }
        return part;
    }
}
