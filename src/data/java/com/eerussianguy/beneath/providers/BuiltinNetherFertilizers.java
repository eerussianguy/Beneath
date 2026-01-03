package com.eerussianguy.beneath.providers;

import java.util.concurrent.CompletableFuture;
import com.eerussianguy.beneath.Accessors;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.common.items.BeneathItems;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;

public class BuiltinNetherFertilizers extends DataManagerProvider<NetherFertilizer> implements Accessors
{
    public BuiltinNetherFertilizers(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(NetherFertilizer.MANAGER, output, lookup, Beneath.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DEATH), 0.1f, 0.0f, 0.0f, 0.0f, 0.0f);
        add(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DECAY), 0.0f, 0.0f, 0.1f, 0.0f, 0.0f);
        add(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.DESTRUCTION), 0.0f, 0.1f, 0.0f, 0.0f, 0.0f);
        add(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.SORROW), 0.0f, 0.0f, 0.0f, 0.1f, 0.0f);
        add(BeneathItems.PURE_NUTRIENTS.get(SoulFarmlandBlockEntity.NutrientType.FLAME), 0.0f, 0.0f, 0.0f, 0.0f, 0.1f);

        add(TFCItems.ORE_POWDERS.get(Ore.SULFUR), 0.0f, 0.0f, 0.2f, 0.0f, 0.1f);
        add(Items.GUNPOWDER, 0.4f, 0.6f, 0.0f, 0.0f, 0.1f);
        add(Items.GHAST_TEAR, 0.0f, 0.0f, 0.0f, 0.3f, 0.0f);
        add(Items.BLAZE_POWDER, 0.0f, 0.0f, 0.0f, 0.0f, 0.2f);
        add(BeneathItems.AGONIZING_FERTILIZER, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f);
    }

    private void add(ItemLike item, float death, float destruction, float decay, float sorrow, float flame)
    {
        add(nameOf(item), new NetherFertilizer(Ingredient.of(item), death, destruction, decay, sorrow, flame));
    }
}
