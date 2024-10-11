package com.eerussianguy.beneath;


import java.util.Locale;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.container.BeneathContainerTypes;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.eerussianguy.beneath.common.network.BeneathPackets;
import com.eerussianguy.beneath.misc.BeneathCreativeTabs;
import com.eerussianguy.beneath.misc.BeneathParticles;
import com.eerussianguy.beneath.world.BeneathFeatures;
import com.eerussianguy.beneath.world.BeneathPlacementModifiers;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import com.eerussianguy.beneath.client.ClientForgeEvents;
import com.eerussianguy.beneath.client.ClientModEvents;
import org.slf4j.Logger;


@Mod(Beneath.MOD_ID)
public class Beneath
{
    public static final String MOD_ID = "beneath";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Beneath()
    {

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientForgeEvents.init();
            ClientModEvents.init();
        }

        ForgeEvents.init();
        ModEvents.init();
        BeneathConfig.init();
        BeneathPackets.init();

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BeneathBlocks.BLOCKS.register(bus);
        BeneathBlockEntities.BLOCK_ENTITIES.register(bus);
        BeneathItems.ITEMS.register(bus);
        BeneathEntities.ENTITIES.register(bus);
        BeneathFeatures.FEATURES.register(bus);
        BeneathPlacementModifiers.MODIFIERS.register(bus);
        BeneathParticles.PARTICLE_TYPES.register(bus);
        BeneathContainerTypes.CONTAINERS.register(bus);
        BeneathCreativeTabs.TABS.register(bus);
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
        return new ResourceLocation(Beneath.MOD_ID, path);
    }

    public static Component blockEntityName(String path)
    {
        return Component.translatable("beneath.block_entity." + path);
    }

}
