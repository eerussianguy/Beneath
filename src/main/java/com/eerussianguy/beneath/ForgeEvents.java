package com.eerussianguy.beneath;

import java.util.List;
import java.util.function.Predicate;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import com.eerussianguy.beneath.common.blocks.BeneathBlockTags;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.CursecoalPileBlock;
import com.eerussianguy.beneath.common.blocks.HellforgeBlock;
import com.eerussianguy.beneath.common.blocks.HellforgeSideBlock;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.common.network.BeneathPackets;
import com.eerussianguy.beneath.misc.NetherClimateModel;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import com.eerussianguy.beneath.world.BeneathPlacements;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.devices.CharcoalForgeBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.dries007.tfc.util.events.StartFireEvent;

public class ForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEvents::onBreakSpeed);
        bus.addListener(ForgeEvents::onMobGriefing);
        bus.addListener(ForgeEvents::onToolUse);
        bus.addListener(ForgeEvents::onLogin);
        bus.addListener(ForgeEvents::onSelectClimateModel);
        bus.addListener(ForgeEvents::onDataSync);
        bus.addListener(ForgeEvents::onReloadListeners);
        bus.addListener(ForgeEvents::onEntityJoinLevel);
        bus.addListener(ForgeEvents::onSpawnCheck);
        bus.addListener(ForgeEvents::onFireStart);
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

    private static void onFireStart(StartFireEvent event)
    {
        final Level level = event.getLevel();
        final BlockPos pos = event.getPos();
        final BlockState state = event.getState();
        final Block block = state.getBlock();

        final boolean hfSide = block instanceof HellforgeSideBlock;
        final boolean hf = block instanceof HellforgeBlock;
        if (hf || hfSide)
        {
            BlockPos forgePos = pos;
            if (hfSide)
            {
                forgePos = HellforgeSideBlock.getCenterPos(level, pos);
            }
            if (forgePos != null && level.getBlockEntity(forgePos) instanceof HellforgeBlockEntity forge && HellforgeBlock.HELLFORGE_MULTIBLOCK.test(level, forgePos) && state.getValue(CharcoalForgeBlock.HEAT) == 0 && forge.light())
            {
                event.setCanceled(true);
            }
        }
        else if (block == BeneathBlocks.CURSECOAL_PILE.get() && state.getValue(CursecoalPileBlock.LAYERS) >= 7)
        {
            final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (HellforgeBlock.PRE_HELLFORGE_MULTIBLOCK.test(level, cursor.setWithOffset(pos, x, 0, z)))
                    {
                        HellforgeBlockEntity.createFromCharcoalPile(level, cursor);
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }

    private static void onEntityJoinLevel(EntityJoinLevelEvent event)
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

    private static void onSpawnCheck(MobSpawnEvent.FinalizeSpawn event)
    {
        if (event.getEntity() instanceof Strider)
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

    private static void onBiomeLoad(Object event)
    {
        final ResourceLocation name = event.getName();
        final BiomeGenerationSettingsBuilder gen = event.getGeneration();
        final MobSpawnSettingsBuilder spawns = event.getSpawns();
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.QUARTZ_VEIN));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.SYLVITE_VEIN));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.NORMAL_GOLD_VEIN));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.DEEP_GOLD_VEIN));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.CURSECOAL_VEIN));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(BeneathPlacements.CRACKRACK_PIPE_VEIN));
        BeneathPlacements.MAGMA_ORES.values().forEach(ore -> gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, get(ore)));

        gen.getFeatures(GenerationStep.Decoration.UNDERGROUND_DECORATION).removeIf(removeIf(REMOVED_UNDER));
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
                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, get(BeneathPlacements.DELTA));
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.BLACKSTONE_PEBBLE_PATCH));
            }
            else if (name.equals(Biomes.SOUL_SAND_VALLEY.location()))
            {
                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, get(BeneathPlacements.SOUL_CLAY_DISC));
            }

            if (!name.equals(Biomes.BASALT_DELTAS.location()))
            {
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.NETHER_PEBBLE_PATCH));
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.BLACKSTONE_BOULDER));
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.COBBLE_BOULDER));
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, get(BeneathPlacements.SULFUR_PATCH));
            }

            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, get(BeneathPlacements.AMETHYST_GEODE));
        }
    }

    private static void onToolUse(BlockEvent.BlockToolModificationEvent event)
    {
        if (event.getToolAction() == ToolActions.HOE_TILL)
        {
            final UseOnContext context = event.getContext();
            if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() == Blocks.SOUL_SOIL)
            {
                event.setFinalState(BeneathBlocks.SOUL_FARMLAND.get().defaultBlockState());
            }
        }
    }

    private static void onMobGriefing(EntityMobGriefingEvent event)
    {
        if (event.getEntity() instanceof LargeFireball && event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
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
        new ResourceLocation("warped_fungi"),
        new ResourceLocation("brown_mushroom_normal"),
        new ResourceLocation("red_mushroom_normal")
    );

    private static final List<ResourceLocation> REMOVED_UNDER = List.of(
        new ResourceLocation("ore_quartz_nether"),
        new ResourceLocation("ore_quartz_deltas"),
        new ResourceLocation("ore_gold_nether"),
        new ResourceLocation("ore_gold_deltas"),
        new ResourceLocation("ore_ancient_debris_large"),
        new ResourceLocation("ore_ancient_debris_small"),
        new ResourceLocation("ore_magma"),
        new ResourceLocation("red_mushroom_nether"),
        new ResourceLocation("brown_mushroom_nether")

    );

    private static final List<ResourceLocation> REMOVED_STRUCTURES = List.of(
        new ResourceLocation("delta")
    );
}
