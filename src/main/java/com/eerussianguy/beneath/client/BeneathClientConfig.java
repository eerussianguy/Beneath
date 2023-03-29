package com.eerussianguy.beneath.client;

import java.util.function.Function;

import net.minecraftforge.common.ForgeConfigSpec;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class BeneathClientConfig
{
    public final ForgeConfigSpec.BooleanValue enableTFCDecayDisplay;
    public final ForgeConfigSpec.BooleanValue enableFoodShowsStackCount;
    public final ForgeConfigSpec.IntValue maxOunces;
    public final ForgeConfigSpec.BooleanValue enableFoodWeightRender;
    public final ForgeConfigSpec.BooleanValue enableFoodDecayRender;
    public final ForgeConfigSpec.BooleanValue enableEuropeanMode;

    public BeneathClientConfig(ForgeConfigSpec.Builder innerBuilder)
    {
        Function<String, ForgeConfigSpec.Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.client." + name);

        innerBuilder.push("general");

        enableTFCDecayDisplay = builder.apply("enableTFCDecayDisplay").comment("If false, automatically hide the 'rotten in X days' tooltip.").define("enableTFCDecayDisplay", false);
        enableFoodShowsStackCount = builder.apply("enableFoodShowsStackCount").comment("If true, food stack counts will render behind the weight bar").define("enableFoodShowsStackCount", false);
        maxOunces = builder.apply("maxOunces").comment("The value that should be displayed as the maximum ounces (or whatever kind of weight type you choose) a food item could weigh. By default, a full stack is 160 ounces.").defineInRange("maxOunces", 16, 1, Integer.MAX_VALUE);
        enableFoodWeightRender = builder.apply("enableFoodWeightRender").comment("Enable rendering a food weight bar").define("enableFoodWeightRender", true);
        enableFoodDecayRender = builder.apply("enableFoodDecayRender").comment("Enable rendering a food weight bar").define("enableFoodDecayRender", true);
        enableEuropeanMode = builder.apply("enableEuropeanMode").comment("If true, SI units will be used for food weight instead of the traditional oz. The name of this config option is a joke, I know SI units are used outside of Europe.").define("enableEuropeanMode", false);

        innerBuilder.pop();
    }
}
