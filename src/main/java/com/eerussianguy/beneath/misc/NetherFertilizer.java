package com.eerussianguy.beneath.misc;

import java.util.List;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.data.DataManager;

public record NetherFertilizer(Ingredient ingredient, float death, float destruction, float decay, float sorrow, float flame)
{
    public static final Codec<NetherFertilizer> CODEC = RecordCodecBuilder.create(i -> i.group(
        Ingredient.CODEC.fieldOf("ingredient").forGetter(c -> c.ingredient),
        Codec.FLOAT.optionalFieldOf("death", 0f).forGetter(c -> c.death),
        Codec.FLOAT.optionalFieldOf("destruction", 0f).forGetter(c -> c.destruction),
        Codec.FLOAT.optionalFieldOf("decay", 0f).forGetter(c -> c.decay),
        Codec.FLOAT.optionalFieldOf("sorrow", 0f).forGetter(c -> c.sorrow),
        Codec.FLOAT.optionalFieldOf("flame", 0f).forGetter(c -> c.flame)
    ).apply(i, NetherFertilizer::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NetherFertilizer> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, c -> c.ingredient,
        ByteBufCodecs.FLOAT, c -> c.death,
        ByteBufCodecs.FLOAT, c -> c.destruction,
        ByteBufCodecs.FLOAT, c -> c.decay,
        ByteBufCodecs.FLOAT, c -> c.sorrow,
        ByteBufCodecs.FLOAT, c -> c.flame,
        NetherFertilizer::new
    );

    public static final DataManager<NetherFertilizer> MANAGER = new DataManager<>(Beneath.identifier("nether_fertilizer"), CODEC, STREAM_CODEC);
    public static final IndirectHashCollection<Item, NetherFertilizer> CACHE = IndirectHashCollection.create(c -> RecipeHelpers.itemKeys(c.ingredient), MANAGER::getValues);


    @Nullable
    public static NetherFertilizer get(ItemStack stack)
    {
        for (NetherFertilizer def : CACHE.getAll(stack.getItem()))
        {
            if (def.ingredient.test(stack))
            {
                return def;
            }
        }
        return null;
    }

    public float getNutrient(SoulFarmlandBlockEntity.NutrientType type)
    {
        return switch (type)
        {
            case DEATH -> death;
            case DESTRUCTION -> destruction;
            case DECAY -> decay;
            case SORROW -> sorrow;
            case FLAME -> flame;
        };
    }

    public void addTooltipInfo(List<Component> tooltip)
    {
        for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
        {
            final float amount = getNutrient(type);
            if (amount > 0)
            {
                tooltip.add(Component.translatable("beneath.nutrient." + type.getName(), format(amount)));
            }
        }
    }

    private String format(float value)
    {
        return String.format("%.2f", value * 100);
    }
}
