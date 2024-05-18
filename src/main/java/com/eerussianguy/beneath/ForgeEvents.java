package com.eerussianguy.beneath;

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
import com.eerussianguy.beneath.misc.PortalUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

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
        bus.addListener(BeneathEntities::onSpawnPlacement);
        bus.addListener(PortalUtil::onLivingDeath);
        bus.addListener(EventPriority.LOWEST, true, ForgeEvents::onPlayerRightClickBlockLowestPriority);
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

    public static void onPlayerRightClickBlockLowestPriority(PlayerInteractEvent.RightClickBlock event)
    {
        if (NetherFertilizer.get(event.getItemStack()) != null)
        {
            event.setUseBlock(Event.Result.ALLOW);
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

}
