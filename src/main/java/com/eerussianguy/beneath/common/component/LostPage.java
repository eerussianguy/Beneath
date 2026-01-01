package com.eerussianguy.beneath.common.component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlockTags;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.items.LostPageItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
import net.neoforged.neoforge.event.EventHooks;
import org.apache.logging.log4j.util.TriConsumer;

import net.dries007.tfc.common.player.IPlayerInfo;
import net.dries007.tfc.network.StreamCodecs;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.data.DataManager;

public record LostPage(Ingredient cost, List<Integer> costs, Holder<Item> reward, List<Integer> rewards, List<Punishment> punishments, Optional<Component> translation)
{
    public static final Codec<LostPage> CODEC = RecordCodecBuilder.create(i -> i.group(
        Ingredient.CODEC.fieldOf("cost").forGetter(c -> c.cost),
        Codec.INT.listOf().fieldOf("costs").forGetter(c -> c.costs),
        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("reward").forGetter(c -> c.reward),
        Codec.INT.listOf().fieldOf("rewards").forGetter(c -> c.rewards),
        Punishment.CODEC.listOf().fieldOf("punishments").forGetter(c -> c.punishments),
        ComponentSerialization.CODEC.optionalFieldOf("translation_key").forGetter(c -> c.translation)
    ).apply(i, LostPage::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LostPage> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, c -> c.cost,
        ByteBufCodecs.INT.apply(ByteBufCodecs.list()), c -> c.costs,
        ByteBufCodecs.holderRegistry(Registries.ITEM), c -> c.reward,
        ByteBufCodecs.INT.apply(ByteBufCodecs.list()), c -> c.rewards,
        Punishment.STREAM_CODEC.apply(ByteBufCodecs.list()), c -> c.punishments,
        ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC), c -> c.translation,
        LostPage::new
    );

    public static final DataManager<LostPage> MANAGER = new DataManager<>(Beneath.identifier("nether_fertilizer"), CODEC, STREAM_CODEC);

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
            stack.set(BeneathComponents.LOST_PAGE.get(), LostPageComponent.init(stack, random, list.get(random.nextInt(list.size()))));
            return true;
        }
        return false;
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
            IPlayerInfo.get(player).addIntoxication(ICalendar.CALENDAR_TICKS_IN_DAY);
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

        public static final Codec<Punishment> CODEC = StringRepresentable.fromEnum(Punishment::values);
        public static final StreamCodec<ByteBuf, Punishment> STREAM_CODEC = StreamCodecs.forEnum(Punishment::values);

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
            Helpers.playSound(level, pos, SoundEvents.GENERIC_EXPLODE.value());
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
                    EventHooks.finalizeMobSpawn(entity, access, access.getCurrentDifficultyAt(newPos), MobSpawnType.EVENT, null);
                }
            }
        }
    }
}
