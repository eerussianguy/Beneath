package com.eerussianguy.beneath;


import java.util.Locale;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.container.BeneathMenuTypes;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.eerussianguy.beneath.misc.BeneathClimateModels;
import com.eerussianguy.beneath.common.component.BeneathComponents;
import com.eerussianguy.beneath.misc.BeneathCreativeTabs;
import com.eerussianguy.beneath.misc.BeneathDataManagers;
import com.eerussianguy.beneath.misc.BeneathParticles;
import com.eerussianguy.beneath.world.BeneathFeatures;
import com.eerussianguy.beneath.world.BeneathPlacementModifiers;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import com.eerussianguy.beneath.client.ClientForgeEvents;
import com.eerussianguy.beneath.client.ClientModEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import net.dries007.tfc.compat.theoneprobe.TheOneProbeIntegration;


@Mod(Beneath.MOD_ID)
public class Beneath
{
    public static final String MOD_ID = "beneath";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean JEI = ModList.get().isLoaded("jei");
    public static final boolean THE_ONE_PROBE = ModList.get().isLoaded("theoneprobe");

    public Beneath(ModContainer mod, IEventBus bus)
    {
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientForgeEvents.init(NeoForge.EVENT_BUS);
            ClientModEvents.init(bus);
        }

        mod.registerConfig(ModConfig.Type.SERVER, BeneathConfig.SERVER.spec());

        ForgeEvents.init(NeoForge.EVENT_BUS);
        ModEvents.init(bus);

        BeneathBlocks.BLOCKS.register(bus);
        BeneathBlockEntities.BLOCK_ENTITIES.register(bus);
        BeneathItems.ITEMS.register(bus);
        BeneathEntities.ENTITIES.register(bus);
        BeneathFeatures.FEATURES.register(bus);
        BeneathPlacementModifiers.MODIFIERS.register(bus);
        BeneathParticles.PARTICLE_TYPES.register(bus);
        BeneathMenuTypes.MENU.register(bus);
        BeneathCreativeTabs.TABS.register(bus);
        BeneathClimateModels.TYPES.register(bus);
        BeneathDataManagers.MANAGERS.register(bus);
        BeneathComponents.COMPONENT.register(bus);

        if (THE_ONE_PROBE) TheOneProbeIntegration.init(bus);

    }

    public static MutableComponent translateEnum(Enum<?> anEnum) {
        return Component.translatable(getEnumTranslationKey(anEnum));
    }

    public static MutableComponent translateEnum(Enum<?> anEnum, String enumName) {
        return Component.translatable(getEnumTranslationKey(anEnum, enumName));
    }

    public static String getEnumTranslationKey(Enum<?> anEnum) {
        return getEnumTranslationKey(anEnum, anEnum.getDeclaringClass().getSimpleName());
    }

    public static String getEnumTranslationKey(Enum<?> anEnum, String enumName) {
        return String.join(".", MOD_ID, "enum", enumName, anEnum.name()).toLowerCase(Locale.ROOT);
    }

    public static ResourceLocation identifier(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(Beneath.MOD_ID, path);
    }

    public static Component blockEntityName(String path)
    {
        return Component.translatable("beneath.block_entity." + path);
    }

}
