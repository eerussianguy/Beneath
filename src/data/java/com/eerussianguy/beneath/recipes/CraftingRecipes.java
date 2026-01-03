package com.eerussianguy.beneath.recipes;

import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.items.HideItemType;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.DataGenerationHelpers;

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
    }

    private DataGenerationHelpers.Builder recipe(String recipeName)
    {
        return new DataGenerationHelpers.Builder((name, r) -> {
            if (name != null) add(name + "_" + recipeName, r);
            else if (recipeName != null) add(recipeName, r);
            else add(r);
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
