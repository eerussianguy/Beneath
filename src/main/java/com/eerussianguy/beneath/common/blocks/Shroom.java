package com.eerussianguy.beneath.common.blocks;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum Shroom implements StringRepresentable
{
    BUTTON,
    CHANTRELLE,
    DEATH_CAP(true),
    DESTROYING_ANGELS(true),
    FOOLS_FUNNEL(true),
    OYSTER,
    PARASOL,
    PORTOBELLO,
    SHITTAKE,
    SULFUR_TUFT(true);

    private final String name = name().toLowerCase(Locale.ROOT);
    private final boolean isPoison;

    Shroom()
    {
        isPoison = false;
    }

    Shroom(boolean isPoison)
    {
        this.isPoison = isPoison;
    }

    public boolean isPoison()
    {
        return isPoison;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
