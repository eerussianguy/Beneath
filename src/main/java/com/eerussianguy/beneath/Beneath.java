package com.eerussianguy.beneath;


import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.eerussianguy.beneath.common.network.BeneathPackets;
import com.eerussianguy.beneath.misc.BeneathParticles;
import com.eerussianguy.beneath.world.BeneathConfiguredFeatures;
import com.eerussianguy.beneath.world.BeneathFeatures;
import com.eerussianguy.beneath.world.BeneathPlacementModifiers;
import com.eerussianguy.beneath.world.BeneathPlacements;
import com.mojang.logging.LogUtils;
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
        BeneathConfiguredFeatures.CONFIGURED_FEATURES.register(bus);
        BeneathPlacements.PLACED_FEATURES.register(bus);
        BeneathParticles.PARTICLE_TYPES.register(bus);
    }

    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(Beneath.MOD_ID, path);
    }

}
