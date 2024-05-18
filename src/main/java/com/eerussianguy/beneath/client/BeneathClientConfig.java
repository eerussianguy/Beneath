package com.eerussianguy.beneath.client;


import net.dries007.tfc.config.ConfigBuilder;

public class BeneathClientConfig
{
    public BeneathClientConfig(ConfigBuilder builder)
    {
        builder.push("general");

        builder.pop();
    }
}
