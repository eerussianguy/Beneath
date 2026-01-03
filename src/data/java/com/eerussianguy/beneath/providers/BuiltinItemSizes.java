package com.eerussianguy.beneath.providers;

import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;

public class BuiltinItemSizes extends DataManagerProvider<ItemSizeDefinition> implements Accessors
{
    public BuiltinItemSizes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(ItemSizeManager.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(BeneathItems.LOST_PAGE, Size.TINY, Weight.VERY_HEAVY);
    }

    private void add(ItemLike item, Size size, Weight weight)
    {
        add(nameOf(item), new ItemSizeDefinition(Ingredient.of(item), size, weight));
    }
}
