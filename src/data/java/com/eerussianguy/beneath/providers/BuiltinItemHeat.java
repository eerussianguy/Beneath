package com.eerussianguy.beneath.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.FluidHeat;

public class BuiltinItemHeat extends DataManagerProvider<HeatDefinition> implements Accessors
{
    public static <T> void fakeDataManager(DataManager<T> manager, Map<String, T> values)
    {
        final Map<ResourceLocation, T> map = new HashMap<>();
        for (T value : manager.getValues())
        {
            map.put(manager.getId(value), value);
        }
        for (Map.Entry<String, T> entry : values.entrySet())
        {
            map.put(Helpers.identifier(entry.getKey()), entry.getValue());
        }
        manager.bindValues(map);
    }

    public static final float FLUID_HEAT_CAPACITY = 0.003f;

    public final List<MeltingRecipe> meltingRecipes = new ArrayList<>();

    public BuiltinItemHeat(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(HeatCapability.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        fakeDataManager(FluidHeat.MANAGER, Map.of(
            Metal.GOLD.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.GOLD).getSource(), 0.6f, 1060)
        ));

        addAndMelt(BeneathItems.GOLD_CHUNK, Metal.GOLD, 100);

    }

    private void addAndMelt(ItemLike item, Metal metal, int units)
    {
        meltingRecipes.add(new MeltingRecipe(item, metal, units));
        add(nameOf(item), Ingredient.of(item), metal, units);
    }

    private void add(String name, Ingredient ingredient, Metal metal, int units)
    {
        add(name, Helpers.identifier(metal.getSerializedName()), ingredient, units);
    }

    private void add(String name, ResourceLocation metalSerializedName, Ingredient ingredient, int units)
    {
        if (FluidHeat.MANAGER.getValues().isEmpty())
        {
            Beneath.LOGGER.error("FluidHeat manager has not been loaded.");
            return;
        }
        final FluidHeat fluidHeat = FluidHeat.MANAGER.getOrThrow(metalSerializedName);
        add(name, new HeatDefinition(
            ingredient,
            (fluidHeat.specificHeatCapacity() / FLUID_HEAT_CAPACITY) * (units / 100f),
            fluidHeat.meltTemperature() * 0.6f,
            fluidHeat.meltTemperature() * 0.8f));
    }

    record MeltingRecipe(ItemLike item, Metal metal, int units) {}
}
