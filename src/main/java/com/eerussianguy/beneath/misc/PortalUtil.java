package com.eerussianguy.beneath.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.dries007.tfc.common.blockentities.IngotPileBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.dries007.tfc.common.items.ScytheItem;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

public final class PortalUtil
{
    public static void onLivingDeath(LivingDeathEvent event)
    {
        if (event.getSource().getEntity() instanceof Player player && player.getMainHandItem().getItem() instanceof ScytheItem)
        {
            final LivingEntity deadEntity = event.getEntity();
            final Level level = player.level();
            final BlockPos pos = player.blockPosition();
            if (Helpers.isEntity(deadEntity, BeneathEntityTags.CAN_BE_SACRIFICED) && scanForOfferings(level, deadEntity.blockPosition()))
            {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
                level.globalLevelEvent(1038, pos, 0);
                level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 10, true, Level.ExplosionInteraction.BLOCK);
                if (level instanceof ServerLevel server)
                {
                    for (int i = 0; i < 8; i++)
                    {
                        final Zoglin zoglin = EntityType.ZOGLIN.create(level);
                        if (zoglin != null)
                        {
                            zoglin.moveTo(deadEntity.position());
                            ForgeEventFactory.onFinalizeSpawn(zoglin, server, level.getCurrentDifficultyAt(pos), MobSpawnType.SPAWN_EGG, null, null);
                            server.tryAddFreshEntityWithPassengers(zoglin);
                            if (zoglin.isAddedToWorld())
                            {
                                zoglin.setAggressive(true);
                                zoglin.setCanPickUpLoot(true);
                                zoglin.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10000));
                            }
                        }
                    }
                    final LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                    if (lightning != null)
                    {
                        lightning.moveTo(deadEntity.position());
                        lightning.setVisualOnly(false);
                        level.addFreshEntity(lightning);
                    }
                    new PortalForcer(server).createPortal(deadEntity.blockPosition(), Direction.Axis.Z);
                }
            }
        }
    }


    public static final int OFFERINGS_NEEDED = 6;

    public static boolean scanForOfferings(LevelAccessor level, BlockPos pos)
    {
        int offerings = 0;
        for (BlockPos testPos : BlockPos.betweenClosed(pos.offset(-5, -2, -5), pos.offset(5, 3, 5)))
        {
            if (isGoodOffering(level, testPos, level.getBlockState(testPos)) && offerings++ >= OFFERINGS_NEEDED)
            {
                return true;
            }
        }
        return offerings >= OFFERINGS_NEEDED;
    }

    public static boolean isGoodOffering(LevelAccessor level, BlockPos pos, BlockState state)
    {
        final Block block = state.getBlock();
        if (block instanceof AbstractSkullBlock || block instanceof AnvilBlock)
        {
            return true;
        }
        if (block == TFCBlocks.METALS.get(Metal.Default.GOLD).get(Metal.BlockType.BLOCK).get() ||
            block == TFCBlocks.METALS.get(Metal.Default.BLACK_STEEL).get(Metal.BlockType.BLOCK).get()
        )
        {
            return true;
        }
        if (level.getBlockEntity(pos) instanceof IngotPileBlockEntity pile)
        {
            final Item item = pile.getPickedItemStack().getItem();
            return item == TFCItems.METAL_ITEMS.get(Metal.Default.GOLD).get(Metal.ItemType.INGOT).get() ||
                item == TFCItems.METAL_ITEMS.get(Metal.Default.PIG_IRON).get(Metal.ItemType.INGOT).get() ||
                item == TFCItems.METAL_ITEMS.get(Metal.Default.BLACK_STEEL).get(Metal.ItemType.INGOT).get();
        }
        return false;
    }

}
