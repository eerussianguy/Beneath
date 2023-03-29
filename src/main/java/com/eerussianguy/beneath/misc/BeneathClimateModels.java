package com.eerussianguy.beneath.misc;

import java.util.function.Supplier;

import com.eerussianguy.beneath.Beneath;
import net.minecraftforge.common.util.Lazy;

import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;

public class BeneathClimateModels
{
    public static final Supplier<ClimateModelType> NETHER = register("nether", NetherClimateModel::new);

    public static void registerModels()
    {
        NETHER.get();
    }

    private static Supplier<ClimateModelType> register(String id, Supplier<ClimateModel> model)
    {
        return Lazy.of(() -> Climate.register(Beneath.identifier(id), model));
    }
}
