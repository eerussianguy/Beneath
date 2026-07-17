package com.eerussianguy.beneath.common.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import com.eerussianguy.beneath.common.items.BeneathItemTags;

/**
 * The rolled contents of a tome: the enchantment and level to apply, plus the offering cost that must be paid to apply
 * it. The cost is a concrete {@code offering} item (rolled from the enchantment's tier tag) and a {@code cost} count of
 * it. A tome carries a fixed number of {@code uses} before it is destroyed, and a {@code punishment} inflicted each
 * time it enchants. All of these values are fixed permanently when the tome is rolled.
 */
public record TomeComponent(Holder<Enchantment> enchantment, int level, Holder<Item> offering, int cost, int uses, int maxUses, LostPage.Punishment punishment)
{
    public static final Codec<TomeComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
        RegistryFixedCodec.create(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(TomeComponent::enchantment),
        Codec.INT.fieldOf("level").forGetter(TomeComponent::level),
        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("offering").forGetter(TomeComponent::offering),
        Codec.INT.fieldOf("cost").forGetter(TomeComponent::cost),
        Codec.INT.fieldOf("uses").forGetter(TomeComponent::uses),
        Codec.INT.fieldOf("max_uses").forGetter(TomeComponent::maxUses),
        LostPage.Punishment.CODEC.fieldOf("punishment").forGetter(TomeComponent::punishment)
    ).apply(i, TomeComponent::new));

    // Written by hand because StreamCodec.composite is limited to six fields.
    public static final StreamCodec<RegistryFriendlyByteBuf, TomeComponent> STREAM_CODEC = StreamCodec.of(
        (buf, tome) -> {
            ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT).encode(buf, tome.enchantment);
            buf.writeVarInt(tome.level);
            ByteBufCodecs.holderRegistry(Registries.ITEM).encode(buf, tome.offering);
            buf.writeVarInt(tome.cost);
            buf.writeVarInt(tome.uses);
            buf.writeVarInt(tome.maxUses);
            LostPage.Punishment.STREAM_CODEC.encode(buf, tome.punishment);
        },
        buf -> new TomeComponent(
            ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT).decode(buf),
            buf.readVarInt(),
            ByteBufCodecs.holderRegistry(Registries.ITEM).decode(buf),
            buf.readVarInt(),
            buf.readVarInt(),
            buf.readVarInt(),
            LostPage.Punishment.STREAM_CODEC.decode(buf)
        )
    );

    public static final int ENCHANT_POWER = 30;
    public static final int MIN_USES = 1;
    public static final int MAX_USES = 5;

    /**
     * The punishments a tome may inflict when it enchants. {@link LostPage.Punishment#NONE} is included so that some
     * tomes are harmless; the marker punishments (blessing, greed, unknown) are excluded as they only make sense for
     * lost pages.
     */
    private static final LostPage.Punishment[] PUNISHMENTS = {
        LostPage.Punishment.NONE, LostPage.Punishment.NONE, LostPage.Punishment.NONE,
        LostPage.Punishment.LEVITATION, LostPage.Punishment.DRUNKENNESS, LostPage.Punishment.BLAZE_INFERNO,
        LostPage.Punishment.INFESTATION, LostPage.Punishment.WITHERING, LostPage.Punishment.SLIME,
        LostPage.Punishment.CORRUPTION, LostPage.Punishment.WRATH, LostPage.Punishment.CHAMPION,
    };

    /**
     * Returns a copy of this tome with one use spent. Callers should destroy the tome instead of storing the result
     * when {@link #uses()} is already at or below one.
     */
    public TomeComponent spendUse()
    {
        return new TomeComponent(enchantment, level, offering, cost, uses - 1, maxUses, punishment);
    }

    public static Optional<TomeComponent> roll(RegistryAccess access, RandomSource random, List<ItemStack> tools)
    {
        final HolderSet.Named<Enchantment> pool = access.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.IN_ENCHANTING_TABLE);
        if (tools.isEmpty())
        {
            final Holder<Enchantment> enchantment = pool.getRandomElement(random).orElseThrow();
            final int level = Mth.nextInt(random, 1, Math.max(1, enchantment.value().getMaxLevel()));
            return withCost(enchantment, level, access, random);
        }

        Stream<Holder<Enchantment>> candidates = pool.stream();
        for (int i = 1; i < tools.size(); i++)
        {
            final ItemStack other = tools.get(i);
            candidates = candidates.filter(other::isPrimaryItemFor);
        }
        final List<EnchantmentInstance> selected = EnchantmentHelper.selectEnchantment(random, tools.getFirst(), ENCHANT_POWER, candidates);
        if (selected.isEmpty())
            return Optional.empty();
        final EnchantmentInstance instance = selected.get(random.nextInt(selected.size()));
        return withCost(instance.enchantment, instance.level, access, random);
    }

    /**
     * Determines the offering tier from the enchantment's min cost, then binds a specific offering item rolled from that
     * tier's tag along with a per-tier count. Returns empty if the tier's tag has no items.
     */
    private static Optional<TomeComponent> withCost(Holder<Enchantment> enchantment, int level, RegistryAccess access, RandomSource random)
    {
        final int tier = tierForMinCost(enchantment.value().getMinCost(level));
        final HolderSet.Named<Item> offerings = access.lookupOrThrow(Registries.ITEM).getOrThrow(tagForTier(tier));
        final int uses = Mth.nextInt(random, MIN_USES, MAX_USES);
        final LostPage.Punishment punishment = PUNISHMENTS[random.nextInt(PUNISHMENTS.length)];
        return offerings.getRandomElement(random).map(offering -> new TomeComponent(enchantment, level, offering, rollCost(tier, random), uses, uses, punishment));
    }

    public static int tierForMinCost(int minCost)
    {
        if (minCost <= 5) return 1;
        if (minCost <= 14) return 2;
        if (minCost <= 24) return 3;
        return 4;
    }

    private static int rollCost(int tier, RandomSource random)
    {
        return switch (tier)
        {
            case 1 -> Mth.nextInt(random, 16, 36);
            case 2 -> Mth.nextInt(random, 3, 10);
            case 3 -> Mth.nextInt(random, 8, 13);
            default -> 1;
        };
    }

    private static TagKey<Item> tagForTier(int tier)
    {
        return switch (tier)
        {
            case 1 -> BeneathItemTags.ENCHANTING_TIER_1;
            case 2 -> BeneathItemTags.ENCHANTING_TIER_2;
            case 3 -> BeneathItemTags.ENCHANTING_TIER_3;
            default -> BeneathItemTags.ENCHANTING_TIER_4;
        };
    }
}
