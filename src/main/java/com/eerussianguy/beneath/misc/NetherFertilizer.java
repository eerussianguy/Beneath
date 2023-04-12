package com.eerussianguy.beneath.misc;

import java.util.List;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.ItemDefinition;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;

public class NetherFertilizer extends ItemDefinition
{
    public static final DataManager<NetherFertilizer> MANAGER = new DataManager<>(Beneath.identifier("nether_fertilizers"), "nether_fertilizer", NetherFertilizer::new, NetherFertilizer::new, NetherFertilizer::encode, NetherFertilizer.Packet::new);
    public static final IndirectHashCollection<Item, NetherFertilizer> CACHE = IndirectHashCollection.create(NetherFertilizer::getValidItems, MANAGER::getValues);

    @Nullable
    public static NetherFertilizer get(ItemStack stack)
    {
        for (NetherFertilizer def : CACHE.getAll(stack.getItem()))
        {
            if (def.matches(stack))
            {
                return def;
            }
        }
        return null;
    }

    private final float[] values = new float[SoulFarmlandBlockEntity.NutrientType.VALUES.length];

    private NetherFertilizer(ResourceLocation id, FriendlyByteBuf buffer)
    {
        super(id, Ingredient.fromNetwork(buffer));

        for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
        {
            values[type.ordinal()] = buffer.readFloat();
        }
    }

    private NetherFertilizer(ResourceLocation id, JsonObject json)
    {
        super(id, Ingredient.fromJson(JsonHelpers.get(json, "ingredient")));

        for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
        {
            values[type.ordinal()] = JsonHelpers.getAsFloat(json, type.getName(), 0);
        }
    }

    public void encode(FriendlyByteBuf buffer)
    {
        ingredient.toNetwork(buffer);
        for (float value : values)
        {
            buffer.writeFloat(value);
        }
    }

    public float getNutrient(SoulFarmlandBlockEntity.NutrientType type)
    {
        return values[type.ordinal()];
    }

    public void addTooltipInfo(List<Component> tooltip)
    {
        for (SoulFarmlandBlockEntity.NutrientType type : SoulFarmlandBlockEntity.NutrientType.VALUES)
        {
            final float amount = getNutrient(type);
            if (amount > 0)
            {
                tooltip.add(Helpers.translatable("beneath.nutrient." + type.getName(), format(amount)));
            }
        }
    }

    private String format(float value)
    {
        return String.format("%.2f", value * 100);
    }

    public static class Packet extends DataManagerSyncPacket<NetherFertilizer> {}
}
