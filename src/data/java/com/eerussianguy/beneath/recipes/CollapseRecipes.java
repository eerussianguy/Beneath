package com.eerussianguy.beneath.recipes;

import java.util.Optional;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.recipes.CollapseRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;

public interface CollapseRecipes extends Recipes
{
    default void craftingRecipes()
    {
    }

}
