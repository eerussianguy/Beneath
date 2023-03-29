package com.eerussianguy.beneath.common.blocks;

import java.util.stream.Stream;
import net.minecraft.world.level.material.Fluids;

import net.dries007.tfc.common.fluids.FluidProperty;

public class BeneathStateProperties
{
    public static final FluidProperty LAVA = FluidProperty.create("fluid", Stream.of(Fluids.EMPTY, Fluids.LAVA));
}
