package com.eerussianguy.beneath.recipes;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.HideItemType;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.DataGenerationHelpers;
import net.dries007.tfc.util.Metal;

public interface CraftingRecipes extends Recipes
{
    default void craftingRecipes()
    {
        recipe().input('X', BeneathBlocks.NETHER_PEBBLE).pattern("XX", "XX").shaped(BeneathBlocks.COBBLERACK);
        recipe().input(BeneathBlocks.COBBLERACK).shapeless(BeneathBlocks.NETHER_PEBBLE, 4);
        recipe().input('X', BeneathItems.CRACKRACK_ROCK).pattern("XX", "XX").shaped(BeneathBlocks.CRACKRACK);
        recipe().input(BeneathBlocks.CRACKRACK).shapeless(BeneathItems.CRACKRACK_ROCK, 4);
        recipe().input('X', BeneathItems.CRIMSON_STRAW).pattern("XX", "XX").shaped(BeneathBlocks.CRIMSON_THATCH);
        recipe().input('X', BeneathItems.WARPED_STRAW).pattern("XX", "XX").shaped(BeneathBlocks.WARPED_THATCH);
        recipe().input('X', BeneathBlocks.BLACKSTONE_PEBBLE).pattern("XX", "XX").shaped(Blocks.BLACKSTONE);
        recipe().input(Blocks.BLACKSTONE).shapeless(BeneathBlocks.BLACKSTONE_PEBBLE, 4);
        recipe()
            .input('X', BeneathItems.BLACKSTONE_BRICK)
            .input('M', TFCItems.MORTAR)
            .pattern("XMX", "MXM", "XMX")
            .shaped(Blocks.POLISHED_BLACKSTONE_BRICKS, 4);
        recipe()
            .input('X', Items.NETHER_BRICK)
            .input('M', TFCItems.MORTAR)
            .pattern("XMX", "MXM", "XMX")
            .shaped(Blocks.NETHER_BRICKS, 4);
        recipe()
            .input(BeneathBlocks.BLACKSTONE_PEBBLE)
            .inputIsPrimary(TFCTags.Items.TOOLS_CHISEL)
            .damageInputs()
            .shapeless(BeneathItems.BLACKSTONE_BRICK);
        recipe()
            .input(BeneathBlocks.NETHER_PEBBLE)
            .inputIsPrimary(TFCTags.Items.TOOLS_CHISEL)
            .damageInputs()
            .shapeless(Items.NETHER_BRICK);
        recipe()
            .input(BeneathItems.BLACKSTONE_BRICK)
            .inputIsPrimary(TFCTags.Items.TOOLS_CHISEL)
            .damageInputs()
            .shapeless(Blocks.POLISHED_BLACKSTONE_BUTTON);
        recipe()
            .input(BeneathItems.BLACKSTONE_BRICK, 2)
            .inputIsPrimary(TFCTags.Items.TOOLS_CHISEL)
            .damageInputs()
            .shapeless(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        recipe()
            .input('X', BeneathItems.BLACKSTONE_BRICK)
            .input('M', TFCItems.MORTAR)
            .pattern("X X", "MXM")
            .shaped(BeneathBlocks.BLACKSTONE_AQUEDUCT);
        recipe()
            .input(Blocks.POLISHED_BLACKSTONE_BRICKS)
            .inputIsPrimary(TFCTags.Items.TOOLS_HAMMER)
            .damageInputs()
            .shapeless(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        recipe()
            .input(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DEATH))
            .input(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DESTRUCTION))
            .input(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DECAY))
            .input(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.SORROW))
            .input(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.FLAME))
            .shapeless(BeneathItems.AGONIZING_FERTILIZER);
        recipe()
            .input(BeneathItems.CURSED_HIDE)
            .input(itemOf(Powder.FLUX), 2)
            .input(Items.WHITE_DYE)
            .shapeless(TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE));
        recipe()
            .input(Blocks.NETHER_BRICKS)
            .input(Items.MAGMA_CREAM)
            .input(TFCItems.ORE_POWDERS.get(Ore.SULFUR))
            .shapeless(BeneathBlocks.HELLBRICKS);
        recipe()
            .input(Tags.Items.LEATHERS, 3)
            .inputIsPrimary(TFCTags.Items.TOOLS_KNIFE)
            .input(Tags.Items.RODS_WOODEN)
            .damageInputs()
            .shapeless(BeneathItems.JUICER);
        recipe()
            .input('X', BeneathItems.LUMBER.get(Stem.CRIMSON))
            .input('Z', BeneathItems.LUMBER.get(Stem.WARPED))
            .input('Y', TFCBlocks.COMPOSTER)
            .pattern("XYX", "XXX", "ZZZ")
            .shaped(BeneathBlocks.UNPOSTER);
        recipe("blackstone_from_soot")
            .input(Tags.Items.COBBLESTONES)
            .input(TFCItems.SOOT)
            .shapeless(Blocks.BLACKSTONE);
        recipe("blackstone_bricks_from_soot")
            .input(ItemTags.STONE_BRICKS)
            .input(TFCItems.SOOT)
            .shapeless(Blocks.POLISHED_BLACKSTONE_BRICKS);
        recipe()
            .input('X', Blocks.BLACKSTONE)
            .input('Y', Tags.Items.INGOTS_GOLD)
            .input('Z', TFCBlocks.SAND.get(SandBlockType.BLACK))
            .pattern("XXX", "XYX", "ZXZ")
            .shaped(BeneathBlocks.ANCIENT_ALTAR);

        for (Stem wood : Stem.values())
        {
            final var blocks = BeneathBlocks.WOODS.get(wood);
            final var lumber = BeneathItems.LUMBER.get(wood);
            final var planks = blocks.get(Wood.BlockType.PLANKS);

            recipe()
                .input('W', blocks.get(Wood.BlockType.STRIPPED_LOG))
                .input('G', TFCItems.GLUE)
                .pattern("WGW")
                .shaped(blocks.get(Wood.BlockType.AXLE), 4);
            recipe()
                .input('L', lumber)
                .pattern("L L", "L L", "LLL")
                .shaped(blocks.get(Wood.BlockType.BARREL));
            recipe()
                .input(blocks.get(Wood.BlockType.AXLE))
                .input(ingredientOf(Metal.STEEL, Metal.ItemType.INGOT))
                .shapeless(blocks.get(Wood.BlockType.BLADED_AXLE));
            recipe()
                .input('P', planks)
                .pattern("P P", "PPP")
                .shaped(BeneathItems.BOATS.get(wood));
            recipe()
                .input('L', lumber)
                .input('S', Tags.Items.RODS_WOODEN)
                .pattern("LLL", "SSS", "LLL")
                .shaped(blocks.get(Wood.BlockType.BOOKSHELF));
            recipe()
                .input(planks)
                .shapeless(blocks.get(Wood.BlockType.BUTTON));
            recipe()
                .input('L', lumber)
                .pattern("LLL", "L L", "LLL")
                .shaped(blocks.get(Wood.BlockType.CHEST));
            recipe()
                .input(blocks.get(Wood.BlockType.CHEST))
                .input(Items.MINECART)
                .shapeless(BeneathItems.CHEST_MINECARTS.get(wood));
            recipe()
                .input('L', lumber)
                .input('S', blocks.get(Wood.BlockType.STRIPPED_LOG))
                .input('M', TFCItems.BRASS_MECHANISMS)
                .input('A', blocks.get(Wood.BlockType.AXLE))
                .input('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("LSL", "MAR", "LSL")
                .shaped(blocks.get(Wood.BlockType.CLUTCH), 2);
            recipe()
                .input('L', lumber)
                .pattern("LL", "LL", "LL")
                .shaped(blocks.get(Wood.BlockType.DOOR));
            recipe()
                .input('L', lumber)
                .input('S', blocks.get(Wood.BlockType.STRIPPED_LOG))
                .input('A', blocks.get(Wood.BlockType.AXLE))
                .pattern(" S ", "LAL", " S ")
                .shaped(blocks.get(Wood.BlockType.ENCASED_AXLE), 4);
            recipe()
                .input('P', planks)
                .input('L', lumber)
                .pattern("PLP", "PLP")
                .shaped(blocks.get(Wood.BlockType.FENCE), 8);
            recipe()
                .input('P', planks)
                .input('L', lumber)
                .pattern("LPL", "LPL")
                .shaped(blocks.get(Wood.BlockType.FENCE_GATE), 2);
            recipe()
                .input('L', lumber)
                .input('M', TFCItems.BRASS_MECHANISMS)
                .pattern(" L ", "LML", " L ")
                .shaped(blocks.get(Wood.BlockType.GEAR_BOX), 2);
            recipe()
                .input('L', lumber)
                .input('B', blocks.get(Wood.BlockType.BOOKSHELF))
                .pattern("LLL", " B ", " L ")
                .shaped(blocks.get(Wood.BlockType.LECTERN));
            recipe()
                .input('P', blocks.get(Wood.BlockType.LOG))
                .input('L', lumber)
                .pattern("PLP", "PLP")
                .shaped(blocks.get(Wood.BlockType.LOG_FENCE), 8);
            recipe()
                .input('L', lumber)
                .input('S', Tags.Items.RODS_WOODEN)
                .pattern("LLL", "LSL", "L L")
                .shaped(blocks.get(Wood.BlockType.LOOM));
            recipe("from_logs")
                .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                .input(woodLogsTagOf(Registries.ITEM, wood))
                .damageInputs()
                .shapeless(lumber, 8);
            recipe("from_planks")
                .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                .input(planks)
                .damageInputs()
                .shapeless(lumber, 4);
            recipe("from_stairs")
                .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                .input(blocks.get(Wood.BlockType.STAIRS))
                .damageInputs()
                .shapeless(lumber, 3);
            recipe("from_slabs")
                .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                .input(blocks.get(Wood.BlockType.SLAB))
                .damageInputs()
                .shapeless(lumber, 2);
            recipe().to2x2(lumber, planks, 1);
            recipe()
                .input('L', lumber)
                .pattern("LL")
                .shaped(blocks.get(Wood.BlockType.PRESSURE_PLATE));
            recipe()
                .input('F', Tags.Items.FEATHERS)
                .input('D', Tags.Items.DYES_BLACK)
                .input('S', blocks.get(Wood.BlockType.SLAB))
                .input('W', planks)
                .pattern("F D", "SSS", "W W")
                .shaped(blocks.get(Wood.BlockType.SCRIBING_TABLE));
            recipe()
                .input('S', Tags.Items.TOOLS_SHEAR)
                .input('L', Tags.Items.LEATHERS)
                .input('P', planks)
                .input('G', blocks.get(Wood.BlockType.LOG))
                .pattern(" LS", "PPP", "G G")
                .shaped(blocks.get(Wood.BlockType.SEWING_TABLE));
            recipe()
                .input('L', lumber)
                .input('P', planks)
                .input('S', Tags.Items.RODS_WOODEN)
                .pattern("PPP", "L L", "S S")
                .shaped(blocks.get(Wood.BlockType.SHELF), 2);
            recipe()
                .input('L', lumber)
                .input('S', Tags.Items.RODS_WOODEN)
                .pattern("LLL", "LLL", " S ")
                .shaped(blocks.get(Wood.BlockType.SIGN), 3);
            recipe()
                .input('#', planks)
                .pattern("###")
                .shaped(blocks.get(Wood.BlockType.SLAB), 6);
            recipe()
                .input('#', planks)
                .pattern("#  ", "## ", "###")
                .shaped(blocks.get(Wood.BlockType.STAIRS), 8);
            recipe()
                .input('L', lumber)
                .input('S', Tags.Items.RODS_WOODEN)
                .pattern("  S", " SL", "SLL")
                .shaped(blocks.get(Wood.BlockType.SLUICE));
            recipe()
                .input('L', woodLogsTagOf(Registries.ITEM, wood))
                .input('S', TFCTags.Items.TOOLS_SAW)
                .pattern("LS", "L ")
                .damageInputs()
                .source(0, 1)
                .shaped(BeneathItems.SUPPORTS.get(wood), 8);
            recipe()
                .input('L', lumber)
                .pattern("LLL", "   ", "LLL")
                .shaped(blocks.get(Wood.BlockType.TOOL_RACK));
            recipe()
                .input('L', lumber)
                .pattern("LLL", "LLL")
                .shaped(blocks.get(Wood.BlockType.TRAPDOOR), 2);
            recipe()
                .input(blocks.get(Wood.BlockType.CHEST))
                .input(Items.TRIPWIRE_HOOK)
                .shapeless(blocks.get(Wood.BlockType.TRAPPED_CHEST));
            recipe()
                .input('L', lumber)
                .input('P', planks)
                .input('A', blocks.get(Wood.BlockType.AXLE))
                .pattern("LPL", "PAP", "LPL")
                .shaped(blocks.get(Wood.BlockType.WATER_WHEEL));
            recipe().to2x2(blocks.get(Wood.BlockType.LOG), blocks.get(Wood.BlockType.WOOD), 3);
            recipe().to2x2(planks, blocks.get(Wood.BlockType.WORKBENCH), 1);

            for (Metal metal : Metal.values())
            {
                if (metal.allParts())
                {
                    recipe()
                        .input('L', BeneathItems.LUMBER.get(wood))
                        .input('C', ingredientOf(metal, Metal.BlockType.CHAIN))
                        .pattern("C C", "LLL", "LLL")
                        .shaped(BeneathItems.HANGING_SIGNS.get(wood).get(metal), 3);
                }
            }
        }

        remove("polished_blackstone_bricks", "polished_blackstone_button", "polished_blackstone_pressure_plate", "cracked_polished_blackstone_bricks");
    }

    private <T> TagKey<T> storageBlockTagOf(ResourceKey<Registry<T>> key, Metal metal)
    {
        assert metal.defaultParts() : "Non-typical use of a non-default metal " + metal.getSerializedName();
        return commonTagOf(key, "storage_blocks/" + metal.getSerializedName());
    }

    private Ingredient ingredientOf(Metal metal, Metal.BlockType type)
    {
        return type == Metal.BlockType.BLOCK
            ? Ingredient.of(storageBlockTagOf(Registries.ITEM, metal))
            : Ingredient.of(TFCBlocks.METALS.get(metal).get(type).get());
    }

    private Ingredient ingredientOf(Metal metal, Metal.ItemType type)
    {
        return type.isCommonTagPart()
            ? Ingredient.of(commonTagOf(metal, type))
            : Ingredient.of(TFCItems.METAL_ITEMS.get(metal).get(type).get());
    }

    private TagKey<Item> commonTagOf(Metal metal, Metal.ItemType type)
    {
        assert type.isCommonTagPart() : "Non-typical use of tag for " + metal.getSerializedName() + " / " + type.name();
        assert type.has(metal) : "Non-typical use of " + metal.getSerializedName() + " / " + type.name();
        return commonTagOf(Registries.ITEM, type.name() + "s/" + metal.name());
    }

    private <T> TagKey<T> woodLogsTagOf(ResourceKey<Registry<T>> registry, Stem wood)
    {
        return TagKey.create(registry, Beneath.identifier(wood.getSerializedName() + "_logs"));
    }

    private DataGenerationHelpers.Builder recipe(String suffix)
    {
        return new DataGenerationHelpers.Builder((name, r) -> {
            assert !suffix.startsWith("_") : "recipe(String suffix) shouldn't start with an '_', it is added for you!";
            assert name == null : "Cannot use a named recipe and recipe(String suffix) at the same time!";
            add(nameOf(r.getResultItem(lookup()).getItem()) + "_" + suffix, r);
        });
    }

    private DataGenerationHelpers.Builder recipe()
    {
        return new DataGenerationHelpers.Builder((name, r) -> {
            if (name != null) add(name, r);
            else add(r);
        });
    }
}
