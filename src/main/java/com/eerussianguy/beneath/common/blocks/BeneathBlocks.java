package com.eerussianguy.beneath.common.blocks;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import com.eerussianguy.beneath.common.items.BeneathItems;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.blockentities.LoomBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.CharcoalPileBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.ThatchBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.MossGrowingBlock;
import net.dries007.tfc.common.blocks.rock.MossSpreadingBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.common.blocks.wood.TFCCeilingHangingSignBlock;
import net.dries007.tfc.common.blocks.wood.TFCLoomBlock;
import net.dries007.tfc.common.blocks.wood.TFCStandingSignBlock;
import net.dries007.tfc.common.blocks.wood.TFCWallHangingSignBlock;
import net.dries007.tfc.common.blocks.wood.TFCWallSignBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryWood;

@SuppressWarnings("unused")
public class BeneathBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Beneath.MOD_ID);

    public static final Map<BeneathOre, Map<Ore.Grade, RegistryObject<Block>>> GRADED_ORES = Helpers.mapOfKeys(BeneathOre.class, ore -> Helpers.mapOfKeys(Ore.Grade.class, grade ->
        register("ore/" + grade.name() + "_" + ore.name(), () -> new Block(Block.Properties.of().sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()))
    ));

    public static final Map<BeneathMineral, RegistryObject<Block>> MINERALS = Helpers.mapOfKeys(BeneathMineral.class, ore -> register("ore/" + ore.name(), () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops())));

    public static final RegistryObject<Block> HAUNTED_SPIKE = register("haunted_spike", () -> new RockSpikeBlock(Block.Properties.of().sound(SoundType.NETHERRACK).strength(2f)));
    public static final RegistryObject<Block> GLOWSTONE_SPIKE = register("glowstone_spike", () -> new RockSpikeBlock(Block.Properties.of().sound(SoundType.GLASS).strength(1f).lightLevel(s -> 15)));
    public static final RegistryObject<Block> NETHER_PEBBLE = register("nether_pebble", () -> new LooseRockBlock(Block.Properties.of().strength(0.05f, 0.0f).sound(SoundType.STONE).noCollission()));
    public static final RegistryObject<Block> BLACKSTONE_PEBBLE = register("blackstone_pebble", () -> new LooseRockBlock(Block.Properties.of().strength(0.05f, 0.0f).sound(SoundType.STONE).noCollission()));
    public static final RegistryObject<Block> FUNGAL_COBBLERACK = register("fungal_cobblerack", () -> new MossSpreadingBlock(BlockBehaviour.Properties.of().sound(SoundType.NETHERRACK).strength(5.5F, 10.0F).randomTicks()));
    public static final RegistryObject<Block> COBBLERACK = register("cobblerack", () -> new MossGrowingBlock(BlockBehaviour.Properties.of().sound(SoundType.NETHERRACK).strength(5.5F, 10.0F), FUNGAL_COBBLERACK));
    public static final RegistryObject<Block> SULFUR = registerNoItem("sulfur", () -> new SulfurBlock(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).noCollission().strength(1f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BLACKSTONE_AQUEDUCT = register("blackstone_aqueduct", () -> new LavaAqueductBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(7f, 10).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SOUL_FARMLAND = register("soul_farmland", () -> new SoulFarmlandBlock(ExtendedProperties.of().strength(1.3f).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(BeneathBlockEntities.SOUL_FARMLAND), () -> Blocks.SOUL_SOIL));
    public static final Map<NCrop, RegistryObject<Block>> CROPS = Helpers.mapOfKeys(NCrop.class, crop -> registerNoItem("crop/" + crop.name(), crop::create));
    public static final RegistryObject<Block> CRIMSON_THATCH = register("crimson_thatch", () -> new ThatchBlock(ExtendedProperties.of(MapColor.CRIMSON_STEM).strength(0.6F, 0.4F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(TFCSounds.THATCH)));
    public static final RegistryObject<Block> WARPED_THATCH = register("warped_thatch", () -> new ThatchBlock(ExtendedProperties.of(MapColor.WARPED_STEM).strength(0.6F, 0.4F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(TFCSounds.THATCH)));
    public static final RegistryObject<Block> SOUL_CLAY = register("soul_clay", () -> new SoulClayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.5F).speedFactor(0.4F).sound(SoundType.SOUL_SAND).isValidSpawn(BeneathBlocks::always).isRedstoneConductor(BeneathBlocks::always).isViewBlocking(BeneathBlocks::always).isSuffocating(BeneathBlocks::always)));
    public static final RegistryObject<Block> CRACKRACK = register("crackrack", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(5F).requiresCorrectToolForDrops().sound(SoundType.NETHERRACK)));
    public static final RegistryObject<Block> HELLBRICKS = register("hellbricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(5F).requiresCorrectToolForDrops().sound(SoundType.NETHER_BRICKS)));
    public static final RegistryObject<Block> HELLFORGE = registerNoItem("hellforge", () -> new HellforgeBlock(ExtendedProperties.of(MapColor.COLOR_BLACK).strength(0.2F).randomTicks().sound(TFCSounds.CHARCOAL).lightLevel(state -> state.getValue(HellforgeBlock.HEAT) * 2).pathType(BlockPathTypes.DAMAGE_FIRE).blockEntity(BeneathBlockEntities.HELLFORGE).serverTicks(HellforgeBlockEntity::serverTick)));
    public static final RegistryObject<Block> HELLFORGE_SIDE = registerNoItem("hellforge_side", () -> new HellforgeSideBlock(ExtendedProperties.of(MapColor.COLOR_BLACK).strength(0.2f).randomTicks().sound(TFCSounds.CHARCOAL).lightLevel(state -> state.getValue(HellforgeBlock.HEAT) * 2).pathType(BlockPathTypes.DAMAGE_FIRE)));
    public static final RegistryObject<Block> CURSECOAL_PILE = registerNoItem("cursecoal_pile", () -> new CursecoalPileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(0.2F).sound(TFCSounds.CHARCOAL).isViewBlocking((state, level, pos) -> state.getValue(CharcoalPileBlock.LAYERS) >= 8).isSuffocating((state, level, pos) -> state.getValue(CharcoalPileBlock.LAYERS) >= 8)));
    public static final RegistryObject<Block> GLEAMFLOWER = register("gleamflower", () -> new NFlowerBlock(ExtendedProperties.of().sound(SoundType.GRASS).instabreak().speedFactor(0.8f).noCollission().lightLevel(s -> 7)));
    public static final RegistryObject<Block> BURPFLOWER = register("burpflower", () -> new BurpingFlowerBlock(ExtendedProperties.of().sound(SoundType.GRASS).instabreak().speedFactor(0.8f).noCollission().randomTicks()));

    public static final Map<Stem, Map<Wood.BlockType, RegistryObject<Block>>> WOODS = Helpers.mapOfKeys(Stem.class, wood ->
        Helpers.mapOfKeys(Wood.BlockType.class, type ->
            register(type.nameFor(wood), createWood(wood, type), type.createBlockItem(wood, new Item.Properties()))
        )
    );
    public static final Map<Stem, Map<Metal.Default, RegistryObject<TFCCeilingHangingSignBlock>>> CEILING_HANGING_SIGNS = registerHangingSigns("hanging_sign", TFCCeilingHangingSignBlock::new);
    public static final Map<Stem, Map<Metal.Default, RegistryObject<TFCWallHangingSignBlock>>> WALL_HANGING_SIGNS = registerHangingSigns("wall_hanging_sign", TFCWallHangingSignBlock::new);

    public static Supplier<Block> createWood(Stem stem, Wood.BlockType blockType)
    {
        if (blockType == Wood.BlockType.LOOM)
        {
            return () -> new TFCLoomBlock(woodProperties(stem).strength(2.5F).noOcclusion().blockEntity(TFCBlockEntities.LOOM).ticks(LoomBlockEntity::tick), Beneath.identifier("block/wood/planks/" + stem.getSerializedName()));
        }
        if (blockType == Wood.BlockType.SIGN)
        {
            return () -> new TFCStandingSignBlock(ExtendedProperties.of(MapColor.WOOD).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).flammableLikePlanks().blockEntity(BeneathBlockEntities.SIGN), stem.getVanillaWoodType());
        }
        if (blockType == Wood.BlockType.WALL_SIGN)
        {
            return () -> new TFCWallSignBlock(ExtendedProperties.of(MapColor.WOOD).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(stem.getBlock(Wood.BlockType.SIGN)).flammableLikePlanks().blockEntity(BeneathBlockEntities.SIGN), stem.getVanillaWoodType());
        }
        return blockType.create(stem);
    }

    private static ExtendedProperties woodProperties(RegistryWood wood)
    {
        return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD);
    }

    public static void registerFlowerPotFlowers()
    {
        FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
        WOODS.forEach((wood, map) -> pot.addPlant(map.get(Wood.BlockType.SAPLING).getId(), map.get(Wood.BlockType.POTTED_SAPLING)));
    }

    private static boolean always(BlockState s, BlockGetter l, BlockPos p, EntityType<?> t) { return true; }

    private static boolean always(BlockState s, BlockGetter l, BlockPos p) { return true; }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, b -> new BlockItem(b, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(BLOCKS, BeneathItems.ITEMS, name, blockSupplier, blockItemFactory);
    }

    private static <B extends SignBlock> Map<Stem, Map<Metal.Default, RegistryObject<B>>> registerHangingSigns(String variant, BiFunction<ExtendedProperties, WoodType, B> factory)
    {
        return Helpers.mapOfKeys(Stem.class, wood ->
            Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasUtilities, metal -> register(
                "wood/planks/" + variant + "/" + metal.getSerializedName() + "/" + wood.getSerializedName(),
                () -> factory.apply(ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).noCollission().strength(1F).flammableLikePlanks().blockEntity(BeneathBlockEntities.HANGING_SIGN).ticks(SignBlockEntity::tick), wood.getVanillaWoodType()),
                (Function<B, BlockItem>) null)
            )
        );
    }
}
