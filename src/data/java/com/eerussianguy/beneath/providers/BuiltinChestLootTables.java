package com.eerussianguy.beneath.providers;

import java.util.function.BiConsumer;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.NCrop;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;

import static net.minecraft.world.level.storage.loot.entries.LootItem.*;

public class BuiltinChestLootTables implements LootTableSubProvider
{
    private final HolderLookup.Provider myRegistries;

    public BuiltinChestLootTables(HolderLookup.Provider myRegistries)
    {
        this.myRegistries = myRegistries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output)
    {
        output.accept(BuiltInLootTables.RUINED_PORTAL, LootTable.lootTable().withPool(
            LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                .add(weight(Blocks.OBSIDIAN, 40, 1, 2))
                .add(weight(Items.FLINT, 40, 1, 4))
                .add(weight(metalItem(Metal.WROUGHT_IRON, Metal.ItemType.INGOT), 40, 1, 4))
                .add(weight(Items.FLINT_AND_STEEL, 40))
                .add(weight(Items.FIRE_CHARGE, 40))
                .add(weight(BeneathItems.GOLD_CHUNK, 15, 1, 6))
                .add(weight(Items.BOW, 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.SWORD), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.AXE), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.SHIELD), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.SCYTHE), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.SHOVEL), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.HELMET), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.CHESTPLATE), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.BOOTS), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.GREAVES), 15))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.HORSE_ARMOR), 3))
                .add(weight(Items.CLOCK, 5))
                .add(weight(metalItem(Metal.GOLD, Metal.ItemType.INGOT), 5, 2, 8))
                .add(weight(Items.BELL, 1))
                .add(weight(TFCBlocks.BRONZE_BELL, 1))
                .add(weight(BeneathItems.LOST_PAGE, 10))
                .add(weight(BeneathItems.LOST_PAGE, 10))
                .add(weight(BeneathItems.LOST_PAGE, 5))
                .add(weight(BeneathItems.TOME, 5))
        ));

        output.accept(BuiltInLootTables.NETHER_BRIDGE, LootTable.lootTable().withPool(
            LootPool.lootPool().setRolls(UniformGenerator.between(2, 4))
                .add(weight(TFCItems.GEMS.get(Ore.DIAMOND), 5, 1, 3))
                .add(weight(metalItem(Metal.WROUGHT_IRON, Metal.ItemType.INGOT), 5, 1, 5))
                .add(weight(metalItem(Metal.GOLD, Metal.ItemType.INGOT), 5, 1, 15))
                .add(weight(metalItem(Metal.WROUGHT_IRON, Metal.ItemType.SWORD), 5))
                .add(weight(metalItem(Metal.WROUGHT_IRON, Metal.ItemType.CHESTPLATE), 5))
                .add(weight(Items.FLINT_AND_STEEL, 5))
                .add(weight(Items.SADDLE, 10))
                .add(weight(metalItem(Metal.BISMUTH_BRONZE, Metal.ItemType.HORSE_ARMOR), 8))
                .add(weight(metalItem(Metal.COPPER, Metal.ItemType.HORSE_ARMOR), 5))
                .add(weight(Blocks.OBSIDIAN, 5, 2, 4))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DECAY), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DEATH), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DESTRUCTION), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.FLAME), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.SORROW), 1))
                .add(weight(BeneathItems.SEEDS.get(NCrop.GHOST_PEPPER), 1, 3, 6))
                .add(weight(BeneathItems.SEEDS.get(NCrop.GLEAMFLOWER), 1, 3, 6))
                .add(weight(BeneathItems.LOST_PAGE, 10))
                .add(weight(BeneathItems.LOST_PAGE, 5))
                .add(weight(BeneathItems.TOME, 5))
        ));

        output.accept(BuiltInLootTables.BASTION_TREASURE, LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(3))
                .add(weight(metalItem(Metal.BLACK_STEEL, Metal.ItemType.CHESTPLATE), 6))
                .add(weight(metalItem(Metal.BLACK_STEEL, Metal.ItemType.HELMET), 6))
                .add(weight(metalItem(Metal.BLACK_STEEL, Metal.ItemType.GREAVES), 6))
                .add(weight(metalItem(Metal.BLACK_STEEL, Metal.ItemType.BOOTS), 6))
                .add(weight(metalItem(Metal.BLACK_STEEL, Metal.ItemType.SWORD), 7))
                .add(weight(metalItem(Metal.BLACK_STEEL, Metal.ItemType.SCYTHE), 7))
                .add(weight(TFCItems.GEMS.get(Ore.DIAMOND), 10))
                .add(weight(TFCItems.GEMS.get(Ore.OPAL), 10))
                .add(weight(TFCItems.GEMS.get(Ore.RUBY), 10))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DECAY), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DEATH), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DESTRUCTION), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.FLAME), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.SORROW), 1))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.TOME, 1))
            )
            .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 4))
                .add(weight(Items.SPECTRAL_ARROW, 1, 12, 25))
                .add(weight(metalItem(Metal.PIG_IRON, Metal.ItemType.INGOT), 1, 2, 5))
                .add(weight(metalItem(Metal.GOLD, Metal.ItemType.INGOT), 1, 3, 9))
                .add(weight(Items.CRYING_OBSIDIAN, 1, 3, 5))
                .add(weight(Items.GILDED_BLACKSTONE, 1, 5, 15))
                .add(weight(Items.MAGMA_CREAM, 1, 3, 8))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.TOME, 1))
            )
        );

        output.accept(BuiltInLootTables.BASTION_OTHER, LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.PICKAXE), 6, true))
                .add(weight(metalItem(Metal.BLACK_BRONZE, Metal.ItemType.SHOVEL), 6, true))
                .add(weight(Items.CROSSBOW, 6))
                .add(weight(Items.SPECTRAL_ARROW, 10, 10, 22))
                .add(weight(Items.PIGLIN_BANNER_PATTERN, 9))
                .add(weight(Items.MUSIC_DISC_PIGSTEP, 5))
                .add(weight(BeneathItems.SEEDS.get(NCrop.GHOST_PEPPER), 1, 3, 6))
                .add(weight(BeneathItems.SEEDS.get(NCrop.GLEAMFLOWER), 1, 3, 6))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.TOME, 1))
            )
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(weight(TFCItems.ROCK_TOOLS.get(RockCategory.IGNEOUS_EXTRUSIVE).get(RockCategory.ItemType.AXE), 2, true))
                .add(weight(TFCItems.ROCK_TOOLS.get(RockCategory.IGNEOUS_EXTRUSIVE).get(RockCategory.ItemType.SHOVEL), 2, true))
                .add(weight(TFCItems.ROCK_TOOLS.get(RockCategory.IGNEOUS_EXTRUSIVE).get(RockCategory.ItemType.HOE), 2, true))
                .add(weight(metalItem(Metal.COPPER, Metal.ItemType.SCYTHE), 2, true))
                .add(weight(Items.BOOK, 10))
                .add(weight(metalItem(Metal.GOLD, Metal.ItemType.INGOT), 10, 2, 4))
                .add(weight(Items.GILDED_BLACKSTONE, 1, 1, 5))
                .add(weight(metalBlock(Metal.BLACK_BRONZE, Metal.BlockType.CHAIN), 1, 2, 10))
                .add(weight(Blocks.OBSIDIAN, 1, 4, 6))
                .add(weight(Items.ARROW, 2, 5, 17))
                .add(weight(TFCItems.FOOD.get(Food.COOKED_PORK), 1))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.TOME, 1))
            )
        );

        output.accept(BuiltInLootTables.BASTION_BRIDGE, LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(weight(Items.CROSSBOW, 1, true))
                .add(weight(Items.SPECTRAL_ARROW, 1, 10, 28, true))
                .add(weight(Items.GILDED_BLACKSTONE, 1, 8, 12))
                .add(weight(Items.CRYING_OBSIDIAN, 1, 3, 8))
                .add(weight(metalItem(Metal.GOLD, Metal.ItemType.INGOT), 1, 4, 9))
                .add(weight(metalItem(Metal.PIG_IRON, Metal.ItemType.INGOT), 1, 4, 9))
                .add(weight(metalItem(Metal.STEEL, Metal.ItemType.CHESTPLATE), 1))
                .add(weight(metalItem(Metal.STEEL, Metal.ItemType.HELMET), 1))
                .add(weight(metalItem(Metal.STEEL, Metal.ItemType.GREAVES), 1))
                .add(weight(metalItem(Metal.STEEL, Metal.ItemType.BOOTS), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DECAY), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DEATH), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DESTRUCTION), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.FLAME), 1))
                .add(weight(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.SORROW), 1))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.TOME, 1))
            )
            .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2, 4))
                .add(weight(Items.STRING, 1, 4, 16))
                .add(weight(Items.LEATHER, 1, 4, 6))
                .add(weight(Items.ARROW, 1, 5, 17))
                .add(weight(TFCItems.GLOW_ARROW, 1, 5, 17))
            )
        );

        output.accept(BuiltInLootTables.BASTION_HOGLIN_STABLE, LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(weight(metalItem(Metal.WROUGHT_IRON, Metal.ItemType.SHOVEL), 15, true))
                .add(weight(metalItem(Metal.WROUGHT_IRON, Metal.ItemType.PICKAXE), 15, true))
                .add(weight(Items.SADDLE, 12))
            )
            .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 4))
                .add(weight(metalItem(Metal.COPPER, Metal.ItemType.AXE), 1, true))
                .add(weight(Items.CRYING_OBSIDIAN, 1, 1, 5))
                .add(weight(Items.GLOWSTONE, 1, 3, 6))
                .add(weight(metalBlock(Metal.BRONZE, Metal.BlockType.LAMP), 1))
                .add(weight(metalBlock(Metal.COPPER, Metal.BlockType.LAMP), 1))
                .add(weight(Items.CRIMSON_NYLIUM, 1, 2, 7))
                .add(weight(Items.WARPED_NYLIUM, 1, 2, 7))
                .add(weight(Items.LEATHER, 2, 2, 7))
                .add(weight(Items.ARROW, 1, 5, 17))
                .add(weight(TFCItems.GLOW_ARROW, 1, 5, 17))
                .add(weight(Items.STRING, 1, 3, 8))
                .add(weight(BeneathBlocks.WOODS.get(Stem.CRIMSON).get(Wood.BlockType.SAPLING), 1, 2, 7))
                .add(weight(BeneathBlocks.WOODS.get(Stem.WARPED).get(Wood.BlockType.SAPLING), 1, 2, 7))
                .add(weight(BeneathItems.LOST_PAGE, 1))
                .add(weight(BeneathItems.TOME, 1))
            )
        );
    }

    private ItemLike metalBlock(Metal metal, Metal.BlockType type)
    {
        return TFCBlocks.METALS.get(metal).get(type);
    }

    private ItemLike metalItem(Metal metal, Metal.ItemType type)
    {
        return TFCItems.METAL_ITEMS.get(metal).get(type);
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight, int min, int max)
    {
        return weight(item, weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight, int value)
    {
        return weight(item, weight).apply(SetItemCountFunction.setCount(ConstantValue.exactly(value)));
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight)
    {
        return lootTableItem(item).setWeight(weight);
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item)
    {
        return lootTableItem(item);
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight, int min, int max, boolean damage)
    {
        var builder = weight(item, weight, min, max);
        if (damage)
        {
            builder = builder.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.9F)));
        }
        return builder;
    }

    private LootPoolSingletonContainer.Builder<?> weight(ItemLike item, int weight, boolean damage)
    {
        var builder = weight(item, weight);
        if (damage)
        {
            builder = builder.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.9F)));
        }
        return builder;
    }

    private ItemLike rockBlock(Rock rock, Rock.BlockType type)
    {
        return TFCBlocks.ROCK_BLOCKS.get(rock).get(type);
    }
}
