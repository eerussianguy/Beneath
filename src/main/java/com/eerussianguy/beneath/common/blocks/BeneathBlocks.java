package com.eerussianguy.beneath.common.blocks;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.eerussianguy.beneath.misc.ItemGroup;
import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.TFCMaterials;
import net.dries007.tfc.common.blocks.ThatchBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.MossGrowingBlock;
import net.dries007.tfc.common.blocks.rock.MossSpreadingBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import static net.dries007.tfc.common.TFCItemGroup.*;

public class BeneathBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Beneath.MOD_ID);

    public static final Map<BeneathOre, Map<Ore.Grade, RegistryObject<Block>>> GRADED_ORES = Helpers.mapOfKeys(BeneathOre.class, ore -> Helpers.mapOfKeys(Ore.Grade.class, grade ->
        register("ore/" + grade.name() + "_" + ore.name(), () -> new Block(Block.Properties.of(Material.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()), ItemGroup.BENEATH)
    ));

    public static final Map<BeneathMineral, RegistryObject<Block>> MINERALS = Helpers.mapOfKeys(BeneathMineral.class, ore -> register("ore/" + ore.name(), () -> new Block(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()), ItemGroup.BENEATH));

    public static final RegistryObject<Block> HAUNTED_SPIKE = register("haunted_spike", () -> new RockSpikeBlock(Block.Properties.of(Material.STONE).sound(SoundType.NETHERRACK).strength(2f)), ItemGroup.BENEATH);
    public static final RegistryObject<Block> GLOWSTONE_SPIKE = register("glowstone_spike", () -> new RockSpikeBlock(Block.Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(1f).lightLevel(s -> 15)), ItemGroup.BENEATH);
    public static final RegistryObject<Block> NETHER_PEBBLE = register("nether_pebble", () -> new LooseRockBlock(Block.Properties.of(TFCMaterials.NON_SOLID_STONE).strength(0.05f, 0.0f).sound(SoundType.STONE).noCollission()), ItemGroup.BENEATH);
    public static final RegistryObject<Block> BLACKSTONE_PEBBLE = register("blackstone_pebble", () -> new LooseRockBlock(Block.Properties.of(TFCMaterials.NON_SOLID_STONE).strength(0.05f, 0.0f).sound(SoundType.STONE).noCollission()), ItemGroup.BENEATH);
    public static final RegistryObject<Block> FUNGAL_COBBLERACK = register("fungal_cobblerack", () -> new MossSpreadingBlock(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.NETHERRACK).strength(5.5F, 10.0F).randomTicks()), ItemGroup.BENEATH);
    public static final RegistryObject<Block> COBBLERACK = register("cobblerack", () -> new MossGrowingBlock(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.NETHERRACK).strength(5.5F, 10.0F), FUNGAL_COBBLERACK), ItemGroup.BENEATH);
    public static final RegistryObject<Block> SULFUR = register("sulfur", () -> new SulfurBlock(BlockBehaviour.Properties.of(Material.DECORATION).sound(SoundType.GRAVEL).noCollission().strength(1f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BLACKSTONE_AQUEDUCT = register("blackstone_aqueduct", () -> new LavaAqueductBlock(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(7f, 10).requiresCorrectToolForDrops()), ItemGroup.BENEATH);
    public static final RegistryObject<Block> SOUL_FARMLAND = register("soul_farmland", () -> new SoulFarmlandBlock(ExtendedProperties.of(Material.DIRT).strength(1.3f).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(BeneathBlockEntities.SOUL_FARMLAND), () -> Blocks.SOUL_SOIL), ItemGroup.BENEATH);
    public static final Map<NCrop, RegistryObject<Block>> CROPS = Helpers.mapOfKeys(NCrop.class, crop -> register("crop/" + crop.name(), crop::create));
    public static final RegistryObject<Block> CRIMSON_THATCH = register("crimson_thatch", () -> new ThatchBlock(ExtendedProperties.of(TFCMaterials.THATCH_COLOR_LEAVES, MaterialColor.CRIMSON_STEM).strength(0.6F, 0.4F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(TFCSounds.THATCH)), MISC);
    public static final RegistryObject<Block> WARPED_THATCH = register("warped_thatch", () -> new ThatchBlock(ExtendedProperties.of(TFCMaterials.THATCH_COLOR_LEAVES, MaterialColor.WARPED_STEM).strength(0.6F, 0.4F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(TFCSounds.THATCH)), MISC);

    public static final Map<Stem, Map<Wood.BlockType, RegistryObject<Block>>> WOODS = Helpers.mapOfKeys(Stem.class, wood ->
        Helpers.mapOfKeys(Wood.BlockType.class, type ->
            register(type.nameFor(wood), type.create(wood), type.createBlockItem(new Item.Properties().tab(ItemGroup.BENEATH)))
        )
    );

    public static void registerFlowerPotFlowers()
    {
        FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
        WOODS.forEach((wood, map) -> pot.addPlant(map.get(Wood.BlockType.SAPLING).getId(), map.get(Wood.BlockType.POTTED_SAPLING)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, CreativeModeTab group)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties().tab(group)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(BLOCKS, BeneathItems.ITEMS, name, blockSupplier, blockItemFactory);
    }

}
