package com.eerussianguy.beneath.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlockTags;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.items.LostPageItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.capabilities.player.PlayerData;
import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.calendar.ICalendar;

public class LostPage
{
    public static final DataManager<LostPage> MANAGER = new DataManager<>(Beneath.identifier("lost_pages"), "lost_page", LostPage::new, LostPage::new, LostPage::encode, LostPage.Packet::new);

    public static boolean choose(ItemStack stack, RandomSource random)
    {
        if (stack.getItem() instanceof LostPageItem page && !page.hasInitialized(stack))
        {
            final List<LostPage> list = MANAGER.getValues().stream().toList();
            if (list.isEmpty())
            {
                Beneath.LOGGER.error("No lost pages loaded... skipping");
                return false;
            }
            page.init(stack, random, list.get(random.nextInt(list.size())));
            return true;
        }
        return false;
    }

    private final ResourceLocation id;
    private final Ingredient cost;
    private final List<Integer> costs;
    private final Item reward;
    private final List<Integer> rewards;
    private final List<Punishment> punishments;
    @Nullable private final String ingredientTranslation;

    private LostPage(ResourceLocation id, JsonObject json)
    {
        this.id = id;
        cost = Ingredient.fromJson(json.get("cost"));
        reward = JsonHelpers.getAsItem(json, "reward");
        costs = JsonHelpers.getAsJsonArray(json, "costs").asList().stream().map(JsonElement::getAsInt).toList();
        rewards = JsonHelpers.getAsJsonArray(json, "rewards").asList().stream().map(JsonElement::getAsInt).toList();
        punishments = JsonHelpers.getAsJsonArray(json, "punishments").asList().stream().map(el -> JsonHelpers.getEnum(el, LostPage.Punishment.class)).toList();
        ingredientTranslation = JsonHelpers.getAsString(json, "ingredient_translation", null);
    }

    private LostPage(ResourceLocation id, FriendlyByteBuf buffer)
    {
        this.id = id;
        cost = Ingredient.fromNetwork(buffer);
        reward = buffer.readRegistryIdUnsafe(ForgeRegistries.ITEMS);

        costs = new ArrayList<>();
        rewards = new ArrayList<>();
        punishments = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            costs.add(buffer.readVarInt());
        size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            rewards.add(buffer.readVarInt());
        size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            punishments.add(buffer.readEnum(LostPage.Punishment.class));
        ingredientTranslation = Helpers.decodeNullable(buffer, FriendlyByteBuf::readUtf);
    }

    private void encode(FriendlyByteBuf buffer)
    {
        cost.toNetwork(buffer);
        buffer.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, reward);
        buffer.writeVarInt(costs.size());
        for (int cost : costs)
            buffer.writeVarInt(cost);
        buffer.writeVarInt(rewards.size());
        for (int rew : rewards)
            buffer.writeVarInt(rew);
        buffer.writeVarInt(punishments.size());
        for (Punishment punish : punishments)
            buffer.writeEnum(punish);
        Helpers.encodeNullable(ingredientTranslation, buffer, (t, b) -> b.writeUtf(t));
    }

    public ResourceLocation getId()
    {
        return id;
    }

    public Ingredient getCost()
    {
        return cost;
    }

    public List<Integer> getCosts()
    {
        return costs;
    }

    public Item getReward()
    {
        return reward;
    }

    public List<Integer> getRewards()
    {
        return rewards;
    }

    public List<Punishment> getPunishments()
    {
        return punishments;
    }

    @Nullable
    public String getIngredientTranslation()
    {
        return ingredientTranslation;
    }

    public enum Punishment implements StringRepresentable
    {
        NONE((player, level, pos) -> {}),
        LEVITATION((player, level, pos) -> {
            level.getEntities(null, new AABB(pos).inflate(6)).forEach(entity -> {
                if (entity instanceof LivingEntity living)
                {
                    living.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 0));
                }
            });
        }),
        DRUNKENNESS((player, level, pos) -> {
            PlayerData.get(player).addIntoxicatedTicks(ICalendar.TICKS_IN_DAY);
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20, 1));
        }),
        BLAZE_INFERNO((player, level, pos) -> {
            final RandomSource random = level.random;
            final int count = Mth.nextInt(random, 3, 7);
            spawnEntitiesAround(level, pos, count, EntityType.BLAZE, 6);
            for (BlockPos checkPos : BlockPos.randomInCube(random, 10, pos, 5))
            {
                if (BaseFireBlock.canBePlacedAt(level, checkPos, Direction.UP))
                {
                    level.setBlockAndUpdate(checkPos, BaseFireBlock.getState(level, checkPos));
                }
            }
        }),
        INFESTATION((player, level, pos) -> {
            spawnEntitiesAround(level, pos, 14, EntityType.SILVERFISH, 5);
        }),
        WITHERING((player, level, pos) -> {
            spawnEntitiesAround(level, pos, 5, EntityType.WITHER_SKELETON, 7);
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 10, 1));
        }),
        SLIME((player, level, pos) -> {
            spawnEntitiesAround(level, pos, 6, EntityType.SLIME, 7);
            final BlockState rawSlime = BeneathBlocks.SLIMED_NETHERRACK.get().defaultBlockState();
            final int amount = Mth.nextInt(level.random, 4, 8);
            for (BlockPos checkPos : BlockPos.randomInCube(level.random, amount, pos, 5))
            {
                final BlockState state = level.getBlockState(checkPos);
                if (Helpers.isBlock(state, BeneathBlockTags.EVENT_REPLACEABLE))
                {
                    level.setBlockAndUpdate(checkPos, rawSlime);
                }
            }
            spawnItemsAround(level, pos, 10, 6, () -> new ItemStack(Items.SLIME_BALL), false);
        }),
        UNKNOWN((player, level, pos) -> {
            final Punishment[] values = values();
            values[level.random.nextInt(values.length)].consumer.accept(player, level, pos);
        }),
        ;

        public static Punishment valueOf(int id)
        {
            return id >= 0 && id < VALUES.length ? VALUES[id] : NONE;
        }

        private static final Punishment[] VALUES = values();

        private final TriConsumer<Player, Level, BlockPos> consumer;
        private final String serializedName;

        Punishment(TriConsumer<Player, Level, BlockPos> consumer)
        {
            this.consumer = consumer;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        public void administer(Player player, Level level, BlockPos pos)
        {
            if (level.isClientSide)
                return;
            consumer.accept(player, level, pos);
            Helpers.playSound(level, pos, SoundEvents.GENERIC_EXPLODE);
            player.displayClientMessage(Component.translatable("beneath.punishment", Beneath.translateEnum(this)).withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
        }

        @Override
        public String getSerializedName()
        {
            return serializedName;
        }
    }

    private static void spawnItemsAround(Level level, BlockPos pos, int amount, int radius, Supplier<ItemStack> supplier, boolean guaranteed)
    {
        for (BlockPos checkPos : BlockPos.randomInCube(level.random, amount, pos, radius))
        {
            if (level.getBlockState(checkPos).isAir())
            {
                final ItemEntity item = new ItemEntity(level, checkPos.getX(), checkPos.getY(), checkPos.getZ(), supplier.get());
                level.addFreshEntity(item);
            }
            else if (guaranteed)
            {
                Helpers.spawnItem(level, pos, supplier.get());
            }
        }
    }

    private static <T extends Mob> void spawnEntitiesAround(Level level, BlockPos pos, int count, EntityType<T> type, int radius)
    {
        final RandomSource random = level.getRandom();
        for (int i = 0; i < count; i++)
        {
            final T entity = type.create(level);
            if (entity != null)
            {
                final BlockPos newPos = pos.offset(Mth.nextInt(random, -radius, radius), Mth.nextInt(random, 2, radius), Mth.nextInt(random, -radius, radius));
                if (level.getBlockState(newPos).isAir() && level instanceof ServerLevel access)
                {
                    entity.moveTo(Vec3.atBottomCenterOf(newPos));
                    level.addFreshEntity(entity);
                    ForgeEventFactory.onFinalizeSpawn(entity, access, access.getCurrentDifficultyAt(newPos), MobSpawnType.EVENT, null, null);
                }
            }
        }
    }

    public static class Packet extends DataManagerSyncPacket<LostPage> {}

}
