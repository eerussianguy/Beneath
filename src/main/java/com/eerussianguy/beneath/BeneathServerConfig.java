package com.eerussianguy.beneath;

import java.util.function.Supplier;

import net.dries007.tfc.config.BaseConfig;
import net.dries007.tfc.config.ConfigBuilder;

public class BeneathServerConfig extends BaseConfig
{
    public final Supplier<Boolean> allowSacrifice;

    public BeneathServerConfig(ConfigBuilder builder)
    {
        builder.push("general");

        allowSacrifice = builder.comment("If the sacrifice method should be allowed for getting to the nether, rather than building a portal (requires black steel pickaxe)").define("allowSacrifice", true);

        builder.pop();
    }
}
