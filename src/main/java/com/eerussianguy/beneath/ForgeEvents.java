package com.eerussianguy.beneath;

import java.util.Optional;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import com.eerussianguy.beneath.common.blocks.BeneathBlockTags;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.CursecoalPileBlock;
import com.eerussianguy.beneath.common.blocks.HellforgeBlock;
import com.eerussianguy.beneath.common.blocks.HellforgeSideBlock;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import com.eerussianguy.beneath.misc.NetherClimateModel;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import com.eerussianguy.beneath.misc.PortalUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
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

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.devices.CharcoalForgeBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.dries007.tfc.util.events.StartFireEvent;

public class ForgeEvents
{
    public static void init(IEventBus bus)
    {
        bus.addListener(ForgeEvents::onBreakSpeed);
        bus.addListener(ForgeEvents::onMobGriefing);
        bus.addListener(ForgeEvents::onToolUse);
        bus.addListener(ForgeEvents::onSelectClimateModel);
        bus.addListener(ForgeEvents::onEntityJoinLevel);
        bus.addListener(ForgeEvents::onSpawnCheck);
        bus.addListener(ForgeEvents::onFireStart);
        bus.addListener(ForgeEvents::onEntityInteract);
        bus.addListener(PortalUtil::onLivingDeath);
        bus.addListener(EventPriority.LOWEST, true, ForgeEvents::onPlayerRightClickBlockLowestPriority);
    }

    private static final EquipmentSlot[] SLOTS = EquipmentSlot.values();

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

    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
    {
        Entity target = event.getTarget();
        if ((target instanceof Strider || target instanceof Hoglin))
        {
            final Animal animal = (Animal) target;
            if (animal.isFood(event.getItemStack()))
            {
                event.setCanceled(true);
            }
        }
    }

    public static void onPlayerRightClickBlockLowestPriority(PlayerInteractEvent.RightClickBlock event)
    {
        if (NetherFertilizer.get(event.getItemStack()) != null)
        {
            event.setUseBlock(TriState.TRUE);
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
            if (type == EntityType.PIGLIN || type == EntityType.PIGLIN_BRUTE || type == EntityType.ZOMBIFIED_PIGLIN || type == EntityType.WITHER_SKELETON)
            {
                if (main == Items.GOLDEN_SWORD || main == Items.STONE_SWORD)
                {
                    living.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TFCItems.METAL_ITEMS.get(Metal.BLACK_BRONZE).get(Metal.ItemType.SWORD).get()));
                }
                else if (main == Items.GOLDEN_AXE)
                {
                    living.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TFCItems.METAL_ITEMS.get(Metal.BLACK_BRONZE).get(Metal.ItemType.AXE).get()));
                }
                for (EquipmentSlot slot : SLOTS)
                {
                    if (!living.getItemBySlot(slot).isEmpty())
                    {
                        final TagKey<Item> tag = switch (slot)
                        {
                            case FEET -> TFCTags.Items.MOB_FEET_ARMOR;
                            case LEGS -> TFCTags.Items.MOB_LEG_ARMOR;
                            case CHEST -> TFCTags.Items.MOB_CHEST_ARMOR;
                            case HEAD -> TFCTags.Items.MOB_HEAD_ARMOR;
                            default -> null;
                        };
                        if (tag != null)
                            living.setItemSlot(slot, new ItemStack(getRandomElement(BuiltInRegistries.ITEM, tag, entity.getRandom()).orElse(Items.AIR)));
                    }
                }
            }
        }
    }

    private static <T> Optional<T> getRandomElement(Registry<T> registry, TagKey<T> tag, RandomSource random)
    {
        return registry.getTag(tag).flatMap((set) -> set.getRandomElement(random)).map(Holder::value);
    }

    private static void onSpawnCheck(FinalizeSpawnEvent event)
    {
        if (event.getEntity() instanceof Strider)
        {
            event.setSpawnCancelled(true);
        }
    }

    private static void onSelectClimateModel(SelectClimateModelEvent event)
    {
        if (event.level().dimension().equals(Level.NETHER))
        {
            event.setModel(NetherClimateModel.INSTANCE);
        }
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
        if (event.getItemAbility() == ItemAbilities.HOE_TILL)
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
            event.setCanGrief(true);
        }
    }

}
