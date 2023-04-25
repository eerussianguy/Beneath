package com.eerussianguy.beneath.client;

import java.util.Arrays;
import java.util.stream.Stream;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.client.model.LeviathanFireballModel;
import com.eerussianguy.beneath.client.render.BlazeLeviathanRenderer;
import com.eerussianguy.beneath.client.render.HellforgeRenderer;
import com.eerussianguy.beneath.client.render.LeviathanFireballRenderer;
import com.eerussianguy.beneath.client.screen.HellforgeScreen;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.container.BeneathContainerTypes;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.misc.BeneathParticles;
import com.eerussianguy.beneath.misc.ColoredSmokeParticleProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.model.entity.DeerModel;
import net.dries007.tfc.client.particle.GlintParticleProvider;
import net.dries007.tfc.client.render.entity.SimpleMobRenderer;
import net.dries007.tfc.client.render.entity.TFCBoatRenderer;
import net.dries007.tfc.util.Helpers;

import static net.dries007.tfc.common.blocks.wood.Wood.BlockType.*;

public class ClientModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientModEvents::setup);
        bus.addListener(ClientModEvents::onEntityRenderers);
        bus.addListener(ClientModEvents::onLayers);
        bus.addListener(ClientModEvents::onBlockColors);
        bus.addListener(ClientModEvents::onItemColors);
        bus.addListener(ClientModEvents::onTextureStitch);
        bus.addListener(ClientModEvents::onParticlesRegister);

    }

    private static void setup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            BeneathBlocks.WOODS.values().forEach(map -> ItemProperties.register(map.get(BARREL).get().asItem(), Helpers.identifier("sealed"), (stack, level, entity, unused) -> stack.hasTag() ? 1.0f : 0f));

            MenuScreens.register(BeneathContainerTypes.HELLFORGE_CONTAINER.get(), HellforgeScreen::new);
        });

        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final RenderType translucent = RenderType.translucent();

        Stream.of(BeneathBlocks.GLEAMFLOWER, BeneathBlocks.BURPFLOWER)
            .map(RegistryObject::get).forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, cutout));

        BeneathBlocks.WOODS.values().forEach(map -> {
            Stream.of(SAPLING, DOOR, TRAPDOOR, FENCE, FENCE_GATE, BUTTON, PRESSURE_PLATE, SLAB, STAIRS, TWIG, BARREL, SCRIBING_TABLE, POTTED_SAPLING).forEach(type -> ItemBlockRenderTypes.setRenderLayer(map.get(type).get(), cutout));
            Stream.of(LEAVES, FALLEN_LEAVES).forEach(type -> ItemBlockRenderTypes.setRenderLayer(map.get(type).get(), layer -> Minecraft.useFancyGraphics() ? layer == cutoutMipped : layer == solid));
        });

        BeneathBlocks.CROPS.values().forEach(crop -> ItemBlockRenderTypes.setRenderLayer(crop.get(), cutout));

        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.SULFUR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.NETHER_PEBBLE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.BLACKSTONE_PEBBLE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.BLACKSTONE_AQUEDUCT.get(), cutout);



    }

    private static final ResourceLocation RED_ELK_LOCATION = Beneath.identifier("textures/entity/red_elk.png");

    private static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        for (Stem wood : Stem.VALUES)
        {
            event.registerEntityRenderer(BeneathEntities.BOATS.get(wood).get(), ctx -> new TFCBoatRenderer(ctx, wood.getSerializedName()));
        }

        event.registerEntityRenderer(BeneathEntities.BLAZE_LEVIATHAN.get(), BlazeLeviathanRenderer::new);
        event.registerEntityRenderer(BeneathEntities.LEVIATHAN_FIREBALL.get(), LeviathanFireballRenderer::new);
        event.registerEntityRenderer(BeneathEntities.RED_ELK.get(), ctx -> new SimpleMobRenderer.Builder<>(ctx, DeerModel::new, "red_elk").shadow(0.6f).texture(p -> RED_ELK_LOCATION).build());

        event.registerBlockEntityRenderer(BeneathBlockEntities.HELLFORGE.get(), ctx -> new HellforgeRenderer());

    }

    private static void onLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        final LayerDefinition boatLayer = BoatModel.createBodyModel();
        final LayerDefinition signLayer = SignRenderer.createSignLayer();
        for (Stem wood : Stem.VALUES)
        {
            event.registerLayerDefinition(TFCBoatRenderer.boatName(wood.getSerializedName()), () -> boatLayer);
            event.registerLayerDefinition(RenderHelpers.modelIdentifier("sign/" + wood.getSerializedName()), () -> signLayer);
        }

        event.registerLayerDefinition(RenderHelpers.modelIdentifier("leviathan_fireball"), LeviathanFireballModel::createBodyLayer);
        event.registerLayerDefinition(RenderHelpers.modelIdentifier("blaze_leviathan"), BlazeModel::createBodyLayer);
        event.registerLayerDefinition(RenderHelpers.modelIdentifier("red_elk"), DeerModel::createBodyLayer);
    }

    private static void onBlockColors(ColorHandlerEvent.Block event)
    {

    }

    private static void onItemColors(ColorHandlerEvent.Item event)
    {

    }

    private static void onParticlesRegister(ParticleFactoryRegisterEvent event)
    {
        final ParticleEngine r = Minecraft.getInstance().particleEngine;

        r.register(BeneathParticles.DEATH.get(), set -> new GlintParticleProvider(set, ChatFormatting.DARK_GRAY));
        r.register(BeneathParticles.DESTRUCTION.get(), set -> new GlintParticleProvider(set, ChatFormatting.GOLD));
        r.register(BeneathParticles.DECAY.get(), set -> new GlintParticleProvider(set, ChatFormatting.YELLOW));
        r.register(BeneathParticles.SORROW.get(), set -> new GlintParticleProvider(set, ChatFormatting.DARK_BLUE));
        r.register(BeneathParticles.FLAME.get(), set -> new GlintParticleProvider(set, ChatFormatting.RED));
        r.register(BeneathParticles.SULFURIC_SMOKE.get(), set -> new ColoredSmokeParticleProvider(set, 1f, 1f, 0f));
    }

    private static void onTextureStitch(TextureStitchEvent.Pre event)
    {
        final ResourceLocation sheet = event.getAtlas().location();
        if (sheet.equals(Sheets.CHEST_SHEET))
        {
            Arrays.stream(Stem.VALUES).map(Stem::getSerializedName).forEach(name -> {
                event.addSprite(Helpers.identifier("entity/chest/normal/" + name));
                event.addSprite(Helpers.identifier("entity/chest/normal_left/" + name));
                event.addSprite(Helpers.identifier("entity/chest/normal_right/" + name));
                event.addSprite(Helpers.identifier("entity/chest/trapped/" + name));
                event.addSprite(Helpers.identifier("entity/chest/trapped_left/" + name));
                event.addSprite(Helpers.identifier("entity/chest/trapped_right/" + name));
            });
        }
        else if (sheet.equals(Sheets.SIGN_SHEET))
        {
            Arrays.stream(Stem.VALUES).map(Stem::getSerializedName).forEach(name -> event.addSprite(Helpers.identifier("entity/signs/" + name)));
        }
    }
}
