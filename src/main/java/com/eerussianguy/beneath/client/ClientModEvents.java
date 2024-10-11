package com.eerussianguy.beneath.client;

import java.util.stream.Stream;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.client.models.RedElkModel;
import com.eerussianguy.beneath.client.render.AncientAltarBlockEntityRenderer;
import com.eerussianguy.beneath.client.render.BeneathHangingSignRenderer;
import com.eerussianguy.beneath.client.render.BeneathSignRenderer;
import com.eerussianguy.beneath.client.render.HellforgeBlockEntityRenderer;
import com.eerussianguy.beneath.client.screen.HellforgeScreen;
import com.eerussianguy.beneath.client.screen.JuicerScreen;
import com.eerussianguy.beneath.client.screen.LostPageScreen;
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
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.particle.GlintParticleProvider;
import net.dries007.tfc.client.render.entity.SimpleMobRenderer;
import net.dries007.tfc.client.render.entity.TFCBoatRenderer;
import net.dries007.tfc.client.render.entity.TFCChestBoatRenderer;
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
        bus.addListener(ClientModEvents::onParticlesRegister);
    }

    @SuppressWarnings("deprecation")
    private static void setup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            BeneathBlocks.WOODS.values().forEach(map -> ItemProperties.register(map.get(BARREL).get().asItem(), Helpers.identifier("sealed"), (stack, level, entity, unused) -> stack.hasTag() ? 1.0f : 0f));

            MenuScreens.register(BeneathContainerTypes.HELLFORGE_CONTAINER.get(), HellforgeScreen::new);
            MenuScreens.register(BeneathContainerTypes.JUICER_CONTAINER.get(), JuicerScreen::new);
            MenuScreens.register(BeneathContainerTypes.LOST_PAGE_CONTAINER.get(), LostPageScreen::new);

            for (Stem stem : Stem.VALUES)
            {
                Sheets.addWoodType(stem.getVanillaWoodType());
            }
        });

        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final RenderType translucent = RenderType.translucent();

        Stream.of(BeneathBlocks.GLEAMFLOWER, BeneathBlocks.BURPFLOWER)
            .map(RegistryObject::get).forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, cutout));
        BeneathBlocks.SHROOMS.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        BeneathBlocks.WOODS.values().forEach(map -> {
            Stream.of(SAPLING, DOOR, TRAPDOOR, FENCE, FENCE_GATE, BUTTON, PRESSURE_PLATE, SLAB, STAIRS, TWIG, BARREL, SCRIBING_TABLE, POTTED_SAPLING).forEach(type -> ItemBlockRenderTypes.setRenderLayer(map.get(type).get(), cutout));
            Stream.of(LEAVES, FALLEN_LEAVES).forEach(type -> ItemBlockRenderTypes.setRenderLayer(map.get(type).get(), layer -> Minecraft.useFancyGraphics() ? layer == cutoutMipped : layer == solid));
        });

        BeneathBlocks.CROPS.values().forEach(crop -> ItemBlockRenderTypes.setRenderLayer(crop.get(), cutout));

        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.SULFUR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.NETHER_PEBBLE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.BLACKSTONE_PEBBLE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.BLACKSTONE_AQUEDUCT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.UNPOSTER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.ANCIENT_ALTAR.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(BeneathBlocks.SLIMED_NETHERRACK.get(), translucent);

    }

    private static final ResourceLocation RED_ELK_LOCATION = Beneath.identifier("textures/entity/nether_deer.png");
    private static final ResourceLocation RED_ELK_F_LOCATION = Beneath.identifier("textures/entity/nether_deer_fawn.png");

    private static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        for (Stem wood : Stem.VALUES)
        {
            event.registerEntityRenderer(BeneathEntities.BOATS.get(wood).get(), ctx -> new TFCBoatRenderer(ctx, wood.getSerializedName()));
            event.registerEntityRenderer(BeneathEntities.CHEST_BOATS.get(wood).get(), ctx -> new TFCChestBoatRenderer(ctx, wood.getSerializedName()));
        }

        event.registerEntityRenderer(BeneathEntities.RED_ELK.get(), ctx -> new SimpleMobRenderer.Builder<>(ctx, RedElkModel::new, "red_elk").shadow(0.6f).texture(p -> p.isMale() ? RED_ELK_LOCATION : RED_ELK_F_LOCATION).build());

        event.registerBlockEntityRenderer(BeneathBlockEntities.HELLFORGE.get(), ctx -> new HellforgeBlockEntityRenderer());
        event.registerBlockEntityRenderer(BeneathBlockEntities.ANCIENT_ALTAR.get(), ctx -> new AncientAltarBlockEntityRenderer());
        event.registerBlockEntityRenderer(BeneathBlockEntities.SIGN.get(), BeneathSignRenderer::new);
        event.registerBlockEntityRenderer(BeneathBlockEntities.HANGING_SIGN.get(), BeneathHangingSignRenderer::new);

    }

    private static void onLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        final LayerDefinition boatLayer = BoatModel.createBodyModel();
        final LayerDefinition chestLayer = ChestBoatModel.createBodyModel();
        final LayerDefinition signLayer = SignRenderer.createSignLayer();
        for (Stem wood : Stem.VALUES)
        {
            event.registerLayerDefinition(TFCBoatRenderer.boatName(wood.getSerializedName()), () -> boatLayer);
            event.registerLayerDefinition(TFCChestBoatRenderer.chestBoatName(wood.getSerializedName()), () -> chestLayer);
            event.registerLayerDefinition(RenderHelpers.modelIdentifier("sign/" + wood.getSerializedName()), () -> signLayer);
        }

        event.registerLayerDefinition(RenderHelpers.modelIdentifier("red_elk"), RedElkModel::createBodyLayer);
    }

    private static void onParticlesRegister(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(BeneathParticles.DEATH.get(), set -> new GlintParticleProvider(set, ChatFormatting.DARK_GRAY));
        event.registerSpriteSet(BeneathParticles.DESTRUCTION.get(), set -> new GlintParticleProvider(set, ChatFormatting.GOLD));
        event.registerSpriteSet(BeneathParticles.DECAY.get(), set -> new GlintParticleProvider(set, ChatFormatting.YELLOW));
        event.registerSpriteSet(BeneathParticles.SORROW.get(), set -> new GlintParticleProvider(set, ChatFormatting.DARK_BLUE));
        event.registerSpriteSet(BeneathParticles.FLAME.get(), set -> new GlintParticleProvider(set, ChatFormatting.RED));
        event.registerSpriteSet(BeneathParticles.SULFURIC_SMOKE.get(), set -> new ColoredSmokeParticleProvider(set, 1f, 1f, 0f));
    }

}
