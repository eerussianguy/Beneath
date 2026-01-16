package com.eerussianguy.beneath.providers;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.eerussianguy.beneath.recipes.CraftingRecipes;
import com.eerussianguy.beneath.recipes.Recipes;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.FluidStack;

import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.BarrelRecipe;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.QuernRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;

public class BuiltinRecipes extends RecipeProvider implements Recipes,
    CraftingRecipes
{
    final Set<ResourceLocation> removedRecipes = new HashSet<>();

    final Codec<Unit> emptyRecipeCodec = Codec.STRING.fieldOf("type")
        .codec()
        .listOf()
        .fieldOf("neoforge:conditions")
        .xmap(l -> Unit.INSTANCE, r -> List.of("neoforge:false"))
        .codec();

    private RecipeOutput output;
    private HolderLookup.Provider lookup;
    private final List<BuiltinItemHeat.MeltingRecipe> meltingRecipes;
    final CompletableFuture<?> before;

    public BuiltinRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, BuiltinItemHeat itemHeat)
    {
        super(output, registries);
        this.before = CompletableFuture.allOf(itemHeat.output());
        this.meltingRecipes = itemHeat.meltingRecipes;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider lookup)
    {
        this.lookup = lookup;
        return before.thenCompose(v -> CompletableFuture.allOf(
            super.run(output, lookup),
            CompletableFuture.allOf(removedRecipes
                .stream()
                .map(id -> DataProvider.saveStable(output, lookup, emptyRecipeCodec, Unit.INSTANCE, recipePathProvider.json(id)))
                .toArray(CompletableFuture[]::new))
        ));
    }

    @Override
    public void buildRecipes(RecipeOutput output)
    {
        this.output = output;
        craftingRecipes();

        add("slime_ball", new QuernRecipe(Ingredient.of(BeneathItems.RAW_SLIME), ItemStackProvider.of(Items.SLIME_BALL)));

        barrel()
            .input(Items.SOUL_SAND)
            .input(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.LIMEWATER).getSource(), 100)
            .output(ItemStackProvider.of(TFCItems.MORTAR, 16))
            .sealed(8000);

        // Heat Recipes from Melting
        for (BuiltinItemHeat.MeltingRecipe melt : meltingRecipes)
        {
            add(nameOf(melt.item()), new HeatingRecipe(
                Ingredient.of(melt.item()),
                ItemStackProvider.empty(),
                new FluidStack(fluidOf(melt.metal()), melt.units()),
                temperatureOf(melt.metal()),
                false
            ));
        }
    }

    private BarrelRecipe.Builder barrel()
    {
        return new BarrelRecipe.Builder(r -> {
            if (!r.getResultItem().isEmpty()) add("barrel", nameOf(r.getResultItem().getItem()), r);
            else if (!r.getOutputFluid().isEmpty()) add("barrel", nameOf(r.getOutputFluid().getFluid()), r);
            else throw new IllegalStateException("Barrel recipe requires a custom name!");
        });
    }

    @Override
    public HolderLookup.Provider lookup()
    {
        return lookup;
    }

    @Override
    public void add(String prefix, String name, Recipe<?> recipe)
    {
        output.accept(Beneath.identifier((prefix + "/" + name).toLowerCase(Locale.ROOT)), recipe, null);
    }

    @Override
    public void remove(String... names)
    {
        for (String name : names)
        {
            final ResourceLocation id = Helpers.identifierMC(name);
            removedRecipes.add(id);
        }
    }

}
