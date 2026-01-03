package com.eerussianguy.beneath.providers;

import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.Shroom;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.FoodDefinition;

public class BuiltinFoods extends DataManagerProvider<FoodDefinition> implements Accessors
{
    public BuiltinFoods(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(FoodCapability.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        // Ghost pepper: hunger=4, saturation=1, water=0, decay=2.5, veg=1
        add(BeneathItems.GHOST_PEPPER, new FoodData(4, 0, 1, 0, new float[]{0, 1, 0, 0, 0}, 2.5f));

        // Mushrooms: hunger=4, saturation=1, water=2, decay=1.5, veg=1
        for (Shroom mushroom : Shroom.values())
        {
            add(BeneathItems.MUSHROOMS.get(mushroom), new FoodData(4, 2, 1, 0, new float[]{0, 1, 0, 0, 0}, 1.5f));
        }
    }

    private void add(ItemLike item, FoodData food)
    {
        add(item, food, true);
    }

    private void add(ItemLike item, FoodData food, boolean edible)
    {
        add(nameOf(item).replace("food/", ""), new FoodDefinition(Ingredient.of(item), food, edible));
    }

    private void add(TagKey<Item> tag, FoodData food, boolean edible)
    {
        add(tag.location().getPath().replace("foods/", ""), new FoodDefinition(Ingredient.of(tag), food, edible));
    }
}
