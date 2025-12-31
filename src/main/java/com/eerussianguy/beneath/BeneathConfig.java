package com.eerussianguy.beneath;

import java.util.function.Function;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import net.dries007.tfc.config.BaseConfig;
import net.dries007.tfc.config.ConfigBuilder;

public class BeneathConfig
{
    public static final BeneathServerConfig SERVER = register(BeneathServerConfig::new, ConfigBuilder.ServerValue::new, "server");

    private static <C extends BaseConfig> C register(Function<ConfigBuilder, C> factory, ConfigBuilder.Factory value, String prefix)
    {
        final Pair<C, ModConfigSpec> pair = new ModConfigSpec.Builder()
            .configure(builder -> factory.apply(new ConfigBuilder(builder, value, prefix)));
        pair.getKey().updateSpec(pair.getValue());
        return pair.getKey();
    }
}
