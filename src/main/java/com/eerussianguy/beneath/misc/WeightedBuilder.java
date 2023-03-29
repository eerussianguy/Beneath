package com.eerussianguy.beneath.misc;

import net.dries007.tfc.util.collections.Weighted;

public class WeightedBuilder<E> extends Weighted<E>
{
    public static <E> WeightedBuilder<E> create(Class<E> typeClass)
    {
        return new WeightedBuilder<>();
    }

    public WeightedBuilder() { }

    public WeightedBuilder<E> and(double weight, E value)
    {
        add(weight, value);
        return this;
    }
}
