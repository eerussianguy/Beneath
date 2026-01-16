package com.eerussianguy.beneath.providers;

import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.data.Fuel;

public class BuiltinFuels extends DataManagerProvider<Fuel> implements Accessors
{
    public BuiltinFuels(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Fuel.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add("cursecoal", BeneathItems.CURSECOAL, 1800, 1350, 1f);

        for (Stem wood : Stem.values())
        {
            add(wood.name().toLowerCase() + "_log",
                Ingredient.of(
                    BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.LOG).get(),
                    BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.WOOD).get(),
                    BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.STRIPPED_WOOD).get(),
                    BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.STRIPPED_LOG).get()
                ),
                800, 1000, 0.6f
            );
        }
    }

    private void add(String name, ItemLike item, int duration, float temperature, float purity)
    {
        add(name, Ingredient.of(item), duration, temperature, purity);
    }

    private void add(String name, Ingredient ingredient, int duration, float temperature, float purity)
    {
        add(name, new Fuel(ingredient, duration, temperature, purity));
    }
}
