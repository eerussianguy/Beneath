package com.eerussianguy.beneath.world;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.world.feature.NearLavaPlacement;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.blockpredicate.ReplaceablePredicate;
import net.dries007.tfc.world.placement.FlatEnoughPlacement;

public class BeneathPlacements
{
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Beneath.MOD_ID);

    public static final RegistryObject<PlacedFeature> QUARTZ_VEIN = register("vein/quartz", BeneathConfiguredFeatures.QUARTZ_VEIN);
    public static final RegistryObject<PlacedFeature> SYLVITE_VEIN = register("vein/sylvite", BeneathConfiguredFeatures.SYLVITE_VEIN);
    public static final RegistryObject<PlacedFeature> NORMAL_GOLD_VEIN = register("vein/normal_gold", BeneathConfiguredFeatures.NORMAL_GOLD_VEIN);
    public static final RegistryObject<PlacedFeature> DEEP_GOLD_VEIN = register("vein/deep_gold", BeneathConfiguredFeatures.DEEP_GOLD_VEIN);
    public static final RegistryObject<PlacedFeature> CURSECOAL_VEIN = register("vein/cursecoal", BeneathConfiguredFeatures.CURSECOAL_VEIN);
    public static final RegistryObject<PlacedFeature> NETHER_SPIKES = register("nether_spikes", BeneathConfiguredFeatures.NETHER_SPIKES, () -> List.of(CountPlacement.of(128), InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, BiomeFilter.biome()));
    public static final RegistryObject<PlacedFeature> GLOWSTONE_SPIKES = register("glowstone_spikes", BeneathConfiguredFeatures.GLOWSTONE_SPIKES, () -> List.of(RarityFilter.onAverageOnceEvery(3), CountPlacement.of(32), InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, BiomeFilter.biome()));
    public static final RegistryObject<PlacedFeature> NETHER_PEBBLE = register("nether_pebble", BeneathConfiguredFeatures.NETHER_PEBBLE, () -> List.of(BlockPredicateFilter.forPredicate(ReplaceablePredicate.INSTANCE), BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BeneathBlocks.NETHER_PEBBLE.get().defaultBlockState(), Vec3i.ZERO)), BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluid(Fluids.EMPTY, Vec3i.ZERO))));
    public static final RegistryObject<PlacedFeature> NETHER_PEBBLE_PATCH = register("nether_pebble_patch", BeneathConfiguredFeatures.NETHER_PEBBLE_PATCH, () -> List.of(RarityFilter.onAverageOnceEvery(3), everyLayer(5), BiomeFilter.biome()));
    public static final RegistryObject<PlacedFeature> BLACKSTONE_PEBBLE = register("blackstone_pebble", BeneathConfiguredFeatures.BLACKSTONE_PEBBLE, () -> List.of(BlockPredicateFilter.forPredicate(ReplaceablePredicate.INSTANCE), BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BeneathBlocks.NETHER_PEBBLE.get().defaultBlockState(), Vec3i.ZERO)), BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluid(Fluids.EMPTY, Vec3i.ZERO))));
    public static final RegistryObject<PlacedFeature> BLACKSTONE_PEBBLE_PATCH = register("blackstone_pebble_patch", BeneathConfiguredFeatures.BLACKSTONE_PEBBLE_PATCH, () -> List.of(RarityFilter.onAverageOnceEvery(3), everyLayer(2), BiomeFilter.biome()));
    public static final RegistryObject<PlacedFeature> SULFUR = register("sulfur", BeneathConfiguredFeatures.SULFUR, () -> List.of(BlockPredicateFilter.forPredicate(ReplaceablePredicate.INSTANCE), BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BeneathBlocks.SULFUR.get().defaultBlockState(), Vec3i.ZERO)), BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluid(Fluids.EMPTY, Vec3i.ZERO))));
    public static final RegistryObject<PlacedFeature> SULFUR_PATCH = register("sulfur_patch", BeneathConfiguredFeatures.SULFUR_PATCH, () -> List.of(RarityFilter.onAverageOnceEvery(14), everyLayer(5), new NearLavaPlacement(5), BiomeFilter.biome()));
    public static final RegistryObject<PlacedFeature> BLACKSTONE_BOULDER = register("blackstone_boulder", BeneathConfiguredFeatures.BLACKSTONE_BOULDER, () -> List.of(RarityFilter.onAverageOnceEvery(24), everyLayer(1), new FlatEnoughPlacement(0.4f, 2, 4)));
    public static final RegistryObject<PlacedFeature> COBBLE_BOULDER = register("cobble_boulder", BeneathConfiguredFeatures.COBBLE_BOULDER, () -> List.of(RarityFilter.onAverageOnceEvery(24), everyLayer(1), new FlatEnoughPlacement(0.4f, 3, 4)));
    public static final RegistryObject<PlacedFeature> CRIMSON_TREE = register("tree/crimson", BeneathConfiguredFeatures.CRIMSON_TREE, () -> List.of(everyLayer(8), BiomeFilter.biome()));
    public static final RegistryObject<PlacedFeature> WARPED_TREE = register("tree/warped", BeneathConfiguredFeatures.WARPED_TREE, () -> List.of(everyLayer(8), BiomeFilter.biome()));
    public static final RegistryObject<PlacedFeature> AMETHYST_GEODE = register("amethyst_geode", BeneathConfiguredFeatures.AMETHYST_GEODE, () -> List.of(RarityFilter.onAverageOnceEvery(100), HeightRangePlacement.uniform(VerticalAnchor.BOTTOM, VerticalAnchor.aboveBottom(32)), InSquarePlacement.spread()));
    public static final RegistryObject<PlacedFeature> DELTA = register("delta", BeneathConfiguredFeatures.DELTA, () -> List.of(everyLayer(40), BiomeFilter.biome()));
    public static final Map<Rock, RegistryObject<PlacedFeature>> MAGMA_ORES = Helpers.mapOfKeys(Rock.class, rock -> rock.category() == RockCategory.IGNEOUS_EXTRUSIVE, rock ->
        register("magma_" + rock.getSerializedName(), BeneathConfiguredFeatures.MAGMA_ORES.get(rock), () -> orePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(27), VerticalAnchor.absolute(36))))
    );

    private static List<PlacementModifier> orePlacement(int rarity, PlacementModifier heightFilter)
    {
        return List.of(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), heightFilter, BiomeFilter.biome());
    }

    @SuppressWarnings("deprecation")
    private static CountOnEveryLayerPlacement everyLayer(int amount)
    {
        return CountOnEveryLayerPlacement.of(amount);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<PlacedFeature> register(String name, RegistryObject<ConfiguredFeature<FC, F>> cf)
    {
        return register(name, cf, List::of);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<PlacedFeature> register(String name, RegistryObject<ConfiguredFeature<FC, F>> cf, Supplier<List<PlacementModifier>> placements)
    {
        return PLACED_FEATURES.register(name, () -> new PlacedFeature(Holder.hackyErase(cf.getHolder().orElseThrow()), placements.get()));
    }
}
