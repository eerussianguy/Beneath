package com.eerussianguy.beneath;

import net.minecraftforge.common.ForgeConfigSpec;

import net.dries007.tfc.config.ConfigBuilder;

public class BeneathServerConfig
{
    public final ForgeConfigSpec.BooleanValue allowSacrifice;

    public BeneathServerConfig(ConfigBuilder builder)
    {
        builder.push("general");

        allowSacrifice = builder.comment("If the sacrifice method should be allowed for getting to the nether, rather than building a portal (requires black steel pickaxe)").define("allowSacrifice", true);

        builder.pop();
    }
}
