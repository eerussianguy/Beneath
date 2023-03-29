package com.eerussianguy.beneath;

import java.util.function.Function;

import net.minecraftforge.common.ForgeConfigSpec;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class BeneathServerConfig
{
    public BeneathServerConfig(ForgeConfigSpec.Builder innerBuilder)
    {
        Function<String, ForgeConfigSpec.Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.client." + name);

        innerBuilder.push("general");

        innerBuilder.pop();
    }
}
