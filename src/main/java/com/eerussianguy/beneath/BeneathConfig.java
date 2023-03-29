package com.eerussianguy.beneath;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import com.eerussianguy.beneath.client.BeneathClientConfig;
import net.dries007.tfc.util.Helpers;

public class BeneathConfig
{
    public static final BeneathClientConfig CLIENT = register(ModConfig.Type.CLIENT, BeneathClientConfig::new);
    public static final BeneathServerConfig SERVER = register(ModConfig.Type.SERVER, BeneathServerConfig::new);

    public static void init() {}

    private static <C> C register(ModConfig.Type type, Function<ForgeConfigSpec.Builder, C> factory)
    {
        Pair<C, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(factory);
        if (!Helpers.BOOTSTRAP_ENVIRONMENT) ModLoadingContext.get().registerConfig(type, specPair.getRight());
        return specPair.getLeft();
    }
}
