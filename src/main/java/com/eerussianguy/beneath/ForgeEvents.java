package com.eerussianguy.beneath;

import java.util.List;
import java.util.function.Predicate;
import com.eerussianguy.beneath.common.blocks.BeneathBlockTags;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.common.network.BeneathPackets;
import com.eerussianguy.beneath.misc.NetherClimateModel;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import com.eerussianguy.beneath.world.BeneathPlacements;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.events.SelectClimateModelEvent;

public class ForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEvents::onBiomeLoad);
        bus.addListener(ForgeEvents::onBreakSpeed);
        bus.addListener(ForgeEvents::onMobGriefing);
        bus.addListener(ForgeEvents::onLogin);
        bus.addListener(ForgeEvents::onSelectClimateModel);
        bus.addListener(ForgeEvents::onDataSync);
        bus.addListener(ForgeEvents::onReloadListeners);
        bus.addListener(ForgeEvents::onEntityJoinLevel);
        bus.addListener(ForgeEvents::onSpawnCheck);
    }

    private static final EquipmentSlot[] SLOTS = EquipmentSlot.values();

    private static void onDataSync(OnDatapackSyncEvent event)
    {
        final ServerPlayer player = event.getPlayer();
        final PacketDistributor.PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);

        BeneathPackets.send(target, NetherFertilizer.MANAGER.createSyncPacket());
    }

    private static void onReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(NetherFertilizer.MANAGER);
    }

    private static void onEntityJoinLevel(EntityJoinWorldEvent event)
    {
        if (event.loadedFromDisk()) return;

        final Entity entity = event.getEntity();
        final EntityType<?> type = entity.getType();

        if (entity instanceof LivingEntity living)
        {
            final Item main = living.getMainHandItem().getItem();
            if (type == EntityType.PIGLIN || type == EntityType.PIGLIN_BRUTE)
            {
                if (main == Items.GOLDEN_SWORD)
                {
                    living.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TFCItems.METAL_ITEMS.get(Metal.Default.BLACK_BRONZE).get(Metal.ItemType.SWORD).get()));
                }
                else if (main == Items.GOLDEN_AXE)
                {
                    living.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TFCItems.METAL_ITEMS.get(Metal.Default.BLACK_BRONZE).get(Metal.ItemType.AXE).get()));
                }
                for (EquipmentSlot slot : SLOTS)
                {
                    if (!living.getItemBySlot(slot).isEmpty())
                    {
                        living.setItemSlot(slot, new ItemStack(Helpers.getRandomElement(ForgeRegistries.ITEMS, TFCTags.Items.mobEquipmentSlotTag(slot), ((LivingEntity) entity).getRandom()).orElse(Items.AIR)));
                    }
                }
            }
        }
    }

    private static void onSpawnCheck(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getEntityLiving() instanceof Strider)
        {
            event.setResult(Event.Result.DENY);
        }
    }

    private static void onSelectClimateModel(SelectClimateModelEvent event)
    {
        if (event.level().dimension().equals(Level.NETHER))
        {
            event.setModel(new NetherClimateModel());
        }
    }

    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Beneath.LOGGER.debug("Messing with TFC Server Config");
        TFCConfig.SERVER.enableNetherPortals.set(true);
    }

    private static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (Helpers.isBlock(event.getState(), BeneathBlockTags.BREAKS_SLOWLY))
        {
            event.setNewSpeed(event.getNewSpeed() * 0.1f);
        }
    }

    private static void onBiomeLoad(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.BiomeCategory.NETHER)
        {
            final ResourceLocation name = event.getName();

            Beneath.LOGGER.debug("Manipulating biome: " + name);
            final BiomeGenerationSettingsBuilder gen = event.getGeneration();
            final MobSpawnSettingsBuilder spawns = event.getSpawns();
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.QUARTZ_VEIN));
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.SYLVITE_VEIN));
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.NORMAL_GOLD_VEIN));
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.DEEP_GOLD_VEIN));
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.CURSECOAL_VEIN));

            gen.getFeatures(GenerationStep.Decoration.UNDERGROUND_DECORATION).removeIf(removeIf(REMOVED_ORES));
            gen.getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION).removeIf(removeIf(REMOVED_VEGETAL));
            gen.getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).removeIf(removeIf(REMOVED_STRUCTURES));

            if (name != null)
            {
                if (name.equals(Biomes.NETHER_WASTES.location()))
                {
                    gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, get(BeneathPlacements.NETHER_SPIKES));
                    gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, get(BeneathPlacements.GLOWSTONE_SPIKES));
                    spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(BeneathEntities.RED_ELK.get(), 4, 1, 4));
                }
                else if (name.equals(Biomes.CRIMSON_FOREST.location()))
                {
                    gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.CRIMSON_TREE));
                }
                else if (name.equals(Biomes.WARPED_FOREST.location()))
                {
                    gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.WARPED_TREE));
                }
                else if (name.equals(Biomes.BASALT_DELTAS.location()))
                {
                    gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.BLACKSTONE_PEBBLE_PATCH));
                }

                if (!name.equals(Biomes.BASALT_DELTAS.location()))
                {
                    gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.NETHER_PEBBLE_PATCH));
                    gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.BLACKSTONE_BOULDER));
                    gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.COBBLE_BOULDER));
                    gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.SULFUR_PATCH));
                    gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, get(BeneathPlacements.DELTA));
                }

                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, get(BeneathPlacements.AMETHYST_GEODE));
            }
        }
    }

    private static void onMobGriefing(EntityMobGriefingEvent event)
    {
        if (event.getEntity() instanceof LargeFireball)
        {
            event.setResult(Event.Result.ALLOW);
        }
    }

    private static Holder<PlacedFeature> get(RegistryObject<PlacedFeature> reg)
    {
        return reg.getHolder().orElseThrow();
    }

    private static Predicate<Holder<PlacedFeature>> removeIf(List<ResourceLocation> list)
    {
        return pf -> {
            for (ResourceLocation id : list)
            {
                if (pf.is(id))
                {
                    return true;
                }
            }
            return false;
        };
    }

    private static final List<ResourceLocation> REMOVED_VEGETAL = List.of(
        new ResourceLocation("crimson_fungi"),
        new ResourceLocation("warped_fungi")
    );

    private static final List<ResourceLocation> REMOVED_ORES = List.of(
        new ResourceLocation("ore_quartz_nether"),
        new ResourceLocation("ore_quartz_deltas"),
        new ResourceLocation("ore_gold_nether"),
        new ResourceLocation("ore_gold_deltas"),
        new ResourceLocation("ore_ancient_debris_large"),
        new ResourceLocation("ore_ancient_debris_small")
    );

    private static final List<ResourceLocation> REMOVED_STRUCTURES = List.of(
        new ResourceLocation("delta")
    );
}
