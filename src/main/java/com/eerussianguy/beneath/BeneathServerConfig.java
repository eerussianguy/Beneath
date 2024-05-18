package com.eerussianguy.beneath;

import net.minecraftforge.common.ForgeConfigSpec;

import net.dries007.tfc.config.ConfigBuilder;

public class BeneathServerConfig
{
    private final ForgeConfigSpec.BooleanValue deathBan;

    public BeneathServerConfig(ConfigBuilder builder)
    {
        builder.push("general");

        deathBan = builder.comment("If on death, players should be banished to the Nether.").define("deathBan", true);

        builder.pop();
    }
}
