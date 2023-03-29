package com.eerussianguy.beneath.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.BeneathMineral;
import com.eerussianguy.beneath.common.blocks.BeneathOre;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.misc.WeightedBuilder;
import com.eerussianguy.beneath.world.feature.LargeNetherSpikesFeature;
import com.eerussianguy.beneath.world.feature.NetherBouldersFeature;
import com.eerussianguy.beneath.world.feature.NetherSpikeConfig;
import com.eerussianguy.beneath.world.feature.NetherSpikesFeature;
import com.eerussianguy.beneath.world.feature.WeightedStateConfig;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.util.collections.Weighted;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.TFCGeodeConfig;
import net.dries007.tfc.world.feature.TFCGeodeFeature;
import net.dries007.tfc.world.feature.tree.RandomTreeConfig;
import net.dries007.tfc.world.feature.tree.RandomTreeFeature;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.dries007.tfc.world.feature.vein.ClusterVeinFeature;
import net.dries007.tfc.world.feature.vein.DiscVeinConfig;
import net.dries007.tfc.world.feature.vein.DiscVeinFeature;
import net.dries007.tfc.world.feature.vein.VeinConfig;

public class BeneathConfiguredFeatures
{
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Beneath.MOD_ID);

    public static final RegistryObject<ConfiguredFeature<VeinConfig, ClusterVeinFeature>> QUARTZ_VEIN = register("vein/quartz", () -> new ConfiguredFeature<>(TFCFeatures.CLUSTER_VEIN.get(), new VeinConfig(
        ImmutableMap.of(Blocks.NETHERRACK, IWeighted.singleton(Blocks.NETHER_QUARTZ_ORE.defaultBlockState())),
        Optional.empty(),
        30,
        15,
        0.6f,
        VerticalAnchor.absolute(1),
        VerticalAnchor.absolute(127),
        "quartz",
        Optional.empty()
    )));

    public static final RegistryObject<ConfiguredFeature<VeinConfig, ClusterVeinFeature>> SYLVITE_VEIN = register("vein/sylvite", () -> new ConfiguredFeature<>(TFCFeatures.CLUSTER_VEIN.get(), new VeinConfig(
        ImmutableMap.of(Blocks.BLACKSTONE, IWeighted.singleton(getMineral(BeneathMineral.BLACKSTONE_SYLVITE))),
        Optional.empty(),
        35,
        17,
        0.6f,
        VerticalAnchor.absolute(1),
        VerticalAnchor.absolute(127),
        "sylvite",
        Optional.empty()
    )));

    public static final RegistryObject<ConfiguredFeature<VeinConfig, ClusterVeinFeature>> NORMAL_GOLD_VEIN = register("vein/normal_gold", () -> new ConfiguredFeature<>(TFCFeatures.CLUSTER_VEIN.get(), new VeinConfig(
        ImmutableMap.of(Blocks.NETHERRACK, makeOre(BeneathOre.NETHER_GOLD, 30, 15, 5, getMineral(BeneathMineral.NETHER_PYRITE), 7)),
        Optional.empty(),
        30,
        15,
        0.5f,
        VerticalAnchor.absolute(64),
        VerticalAnchor.absolute(128),
        "normal_gold",
        Optional.empty()
    )));

    public static final RegistryObject<ConfiguredFeature<VeinConfig, ClusterVeinFeature>> DEEP_GOLD_VEIN = register("vein/deep_gold", () -> new ConfiguredFeature<>(TFCFeatures.CLUSTER_VEIN.get(), new VeinConfig(
        ImmutableMap.of(Blocks.NETHERRACK, makeOre(BeneathOre.NETHER_GOLD, 5, 10, 30)),
        Optional.empty(),
        81,
        17,
        0.6f,
        VerticalAnchor.absolute(1),
        VerticalAnchor.absolute(32),
        "deep_gold",
        Optional.empty()
    )));

    public static final RegistryObject<ConfiguredFeature<DiscVeinConfig, DiscVeinFeature>> CURSECOAL_VEIN = register("vein/cursecoal", () -> new ConfiguredFeature<>(TFCFeatures.DISC_VEIN.get(), new TempDVConfig(new VeinConfig(
        ImmutableMap.of(Blocks.NETHERRACK, IWeighted.singleton(getMineral(BeneathMineral.NETHER_CURSECOAL))),
        Optional.empty(),
        45,
        13,
        0.88f,
        VerticalAnchor.absolute(80),
        VerticalAnchor.absolute(128),
        "cursecoal",
        Optional.empty()
    ), 3)));

    public static final RegistryObject<ConfiguredFeature<NetherSpikeConfig, NetherSpikesFeature>> NETHER_SPIKES = register("nether_spikes", () -> new ConfiguredFeature<>(BeneathFeatures.NETHER_SPIKES.get(), new NetherSpikeConfig(Blocks.NETHERRACK.defaultBlockState(), BeneathBlocks.HAUNTED_SPIKE.get().defaultBlockState())));
    public static final RegistryObject<ConfiguredFeature<NetherSpikeConfig, LargeNetherSpikesFeature>> GLOWSTONE_SPIKES = register("large_nether_spikes", () -> new ConfiguredFeature<>(BeneathFeatures.LARGE_NETHER_SPIKES.get(), new NetherSpikeConfig(Blocks.GLOWSTONE.defaultBlockState(), BeneathBlocks.GLOWSTONE_SPIKE.get().defaultBlockState())));
    public static final RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>>> NETHER_PEBBLE = register("nether_pebble", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BeneathBlocks.NETHER_PEBBLE.get()))));
    public static final RegistryObject<ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>>> NETHER_PEBBLE_PATCH = register("nether_pebble_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(16, 10, 1, BeneathPlacements.NETHER_PEBBLE.getHolder().orElseThrow())));
    public static final RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>>> BLACKSTONE_PEBBLE = register("blackstone_pebble", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BeneathBlocks.BLACKSTONE_PEBBLE.get()))));
    public static final RegistryObject<ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>>> BLACKSTONE_PEBBLE_PATCH = register("blackstone_pebble_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(16, 10, 1, BeneathPlacements.BLACKSTONE_PEBBLE.getHolder().orElseThrow())));
    public static final RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>>> SULFUR = register("sulfur", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BeneathBlocks.SULFUR.get()))));
    public static final RegistryObject<ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>>> SULFUR_PATCH = register("sulfur_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(8, 5, 1, BeneathPlacements.SULFUR.getHolder().orElseThrow())));
    public static final RegistryObject<ConfiguredFeature<WeightedStateConfig, NetherBouldersFeature>> BLACKSTONE_BOULDER = register("nether_boulders", () -> new ConfiguredFeature<>(BeneathFeatures.NETHER_BOULDERS.get(), new WeightedStateConfig(new Weighted<>(Map.of(Blocks.BLACKSTONE.defaultBlockState(), 50d, Blocks.BASALT.defaultBlockState(), 10d, Blocks.GILDED_BLACKSTONE.defaultBlockState(), 1d)))));
    public static final RegistryObject<ConfiguredFeature<WeightedStateConfig, NetherBouldersFeature>> COBBLE_BOULDER = register("cobble_boulder", () -> new ConfiguredFeature<>(BeneathFeatures.NETHER_BOULDERS.get(), new WeightedStateConfig(new Weighted<>(Map.of(Blocks.NETHERRACK.defaultBlockState(), 10d, BeneathBlocks.COBBLERACK.get().defaultBlockState(), 20d, BeneathBlocks.FUNGAL_COBBLERACK.get().defaultBlockState(), 10d)))));
    public static final RegistryObject<ConfiguredFeature<TFCGeodeConfig, TFCGeodeFeature>> AMETHYST_GEODE = register("amethyst_geode", () -> new ConfiguredFeature<>(TFCFeatures.GEODE.get(), new TFCGeodeConfig(Blocks.BLACKSTONE.defaultBlockState(), tfcRock(Rock.QUARTZITE, Rock.BlockType.RAW), SimpleWeightedRandomList.<BlockState>builder().add(tfcRock(Rock.QUARTZITE, Rock.BlockType.RAW), 2).add(getMineral(Rock.QUARTZITE, Ore.AMETHYST), 1).build())));

    public static final RegistryObject<ConfiguredFeature<DeltaFeatureConfiguration, Feature<DeltaFeatureConfiguration>>> DELTA = register("delta", () -> new ConfiguredFeature<>(Feature.DELTA_FEATURE, new DeltaFeatureConfiguration(
        Blocks.LAVA.defaultBlockState(),
        TFCBlocks.MAGMA_BLOCKS.get(Rock.BASALT).get().defaultBlockState(),
        UniformInt.of(3, 7),
        UniformInt.of(0, 2)
    )));

    public static final Map<Rock, RegistryObject<ConfiguredFeature<OreConfiguration, Feature<OreConfiguration>>>> MAGMA_ORES = Helpers.mapOfKeys(Rock.class, rock -> rock.category() == RockCategory.IGNEOUS_EXTRUSIVE, rock ->
        register("magma_" + rock.getSerializedName(), () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new BlockMatchTest(Blocks.NETHERRACK), TFCBlocks.MAGMA_BLOCKS.get(rock).get().defaultBlockState(), 33)))
    );

    public static final RegistryObject<ConfiguredFeature<RandomTreeConfig, RandomTreeFeature>> CRIMSON_TREE = register("tree/crimson", () -> new ConfiguredFeature<>(TFCFeatures.RANDOM_TREE.get(),
        new RandomTreeConfig(
            randomTreeList("crimson", 16),
            Optional.of(new TrunkConfig(BeneathBlocks.WOODS.get(Stem.CRIMSON).get(Wood.BlockType.LOG).get().defaultBlockState(), 3, 5, 1)),
            new TreePlacementConfig(1, 5, false, false)
        )));
    public static final RegistryObject<ConfiguredFeature<RandomTreeConfig, RandomTreeFeature>> WARPED_TREE = register("tree/warped", () -> new ConfiguredFeature<>(TFCFeatures.RANDOM_TREE.get(),
        new RandomTreeConfig(
            randomTreeList("warped", 17),
            Optional.empty(),
            new TreePlacementConfig(1, 5, false, false)
        )));

    private static List<ResourceLocation> randomTreeList(String name, int amount)
    {
        final List<ResourceLocation> list = new ArrayList<>(amount);
        for (int i = 1; i <= amount; i++)
        {
            list.add(Beneath.identifier(name + "/" + i));
        }
        return list;
    }

    private static BlockState tfcRock(Rock rock, Rock.BlockType type)
    {
        return TFCBlocks.ROCK_BLOCKS.get(rock).get(type).get().defaultBlockState();
    }

    private static IWeighted<BlockState> makeOre(BeneathOre ore, int poor, int med, int rich)
    {
        return makeOre(ore, poor, med, rich, null, 0);
    }

    private static IWeighted<BlockState> makeOre(BeneathOre ore, int poor, int normal, int rich, @Nullable BlockState spoiler, int spoilerRarity)
    {
        final var builder = WeightedBuilder.create(BlockState.class).and(poor, getOre(ore, Ore.Grade.POOR)).and(normal, getOre(ore, Ore.Grade.NORMAL)).and(rich, getOre(ore, Ore.Grade.RICH));
        if (spoiler != null) builder.and(spoilerRarity, spoiler);
        return builder;
    }

    private static BlockState getMineral(BeneathMineral ore)
    {
        return BeneathBlocks.MINERALS.get(ore).get().defaultBlockState();
    }

    private static BlockState getMineral(Rock rock, Ore ore)
    {
        return TFCBlocks.ORES.get(rock).get(ore).get().defaultBlockState();
    }

    private static BlockState getOre(BeneathOre ore, Ore.Grade grade)
    {
        return BeneathBlocks.GRADED_ORES.get(ore).get(grade).get().defaultBlockState();
    }

    private static BlockState getOre(Rock rock, Ore ore, Ore.Grade grade)
    {
        return TFCBlocks.GRADED_ORES.get(rock).get(ore).get(grade).get().defaultBlockState();
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, F>> register(String name, Supplier<ConfiguredFeature<FC, F>> supplier)
    {
        return CONFIGURED_FEATURES.register(name, supplier);
    }
}
