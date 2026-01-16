package com.eerussianguy.beneath.providers;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.util.data.Support;

public class BuiltinSupports extends DataManagerProvider<Support>
{
    public BuiltinSupports(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Support.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add("horizontal_support_beam", new Support(
            BlockIngredient.of(Stream.of(
                BeneathBlocks.WOODS.get(Stem.CRIMSON).get(Wood.BlockType.HORIZONTAL_SUPPORT).get(),
                BeneathBlocks.WOODS.get(Stem.WARPED).get(Wood.BlockType.HORIZONTAL_SUPPORT).get()
            )
        ), 2, 2, 4));
    }
}
