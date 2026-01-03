package com.eerussianguy.beneath.providers;

import java.util.stream.Stream;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;

import static net.minecraft.world.level.storage.loot.LootPool.*;
import static net.minecraft.world.level.storage.loot.LootTable.*;
import static net.minecraft.world.level.storage.loot.entries.LootItem.*;

public class BuiltinEntityLootTables extends EntityLootSubProvider
{
    public BuiltinEntityLootTables(HolderLookup.Provider registries)
    {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes()
    {
        return Stream.of(EntityType.HOGLIN, EntityType.PIGLIN, EntityType.STRIDER, EntityType.ZOGLIN, EntityType.ZOMBIFIED_PIGLIN);
    }

    @Override
    public void generate()
    {
        this.add(EntityType.HOGLIN, lootTable()
            .withPool(lootPool().add(lootTableItem(TFCItems.FOOD.get(Food.PORK)).apply(setCount(2, 4))))
            .withPool(lootPool().add(lootTableItem(Items.BONE).apply(setCount(3, 6))))
            .withPool(lootPool().add(lootTableItem(BeneathItems.CURSED_HIDE)))
        );

        this.add(EntityType.PIGLIN, lootTable()
            .withPool(lootPool().add(lootTableItem(TFCItems.FOOD.get(Food.PORK)).apply(setCount(1, 2))))
            .withPool(lootPool().add(lootTableItem(Items.BONE).apply(setCount(1, 3))))
        );

        this.add(EntityType.STRIDER, lootTable()
            .withPool(lootPool().add(lootTableItem(Items.BONE).apply(setCount(1, 2))))
            .withPool(lootPool().add(lootTableItem(BeneathItems.CURSED_HIDE)))
        );

        this.add(EntityType.ZOGLIN, lootTable()
            .withPool(lootPool().add(lootTableItem(Items.BONE).apply(setCount(1, 3))))
            .withPool(lootPool().add(lootTableItem(Items.ROTTEN_FLESH).apply(setCount(1, 3))))
            .withPool(lootPool().add(lootTableItem(BeneathItems.CURSED_HIDE)))
        );

        this.add(EntityType.ZOMBIFIED_PIGLIN, lootTable()
            .withPool(lootPool().add(lootTableItem(Items.BONE).apply(setCount(1, 3))))
            .withPool(lootPool().add(lootTableItem(Items.ROTTEN_FLESH).apply(setCount(1, 3))))
        );
    }

    private static SetItemCountFunction.Builder<?> setCount(int min, int max)
    {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

}
