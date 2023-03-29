package com.eerussianguy.beneath.common.blocks;

import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;

import static com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity.NutrientType.*;

public enum NCrop
{
    CRIMSON_ROOTS(4, DECAY),
    GHOST_PEPPER(7, SORROW),
    GLEAMFLOWER(4, FLAME),
    NETHER_WART(4, DEATH),
    WARPED_ROOTS(4, DESTRUCTION),
    ;


    private final int stages;
    private final SoulFarmlandBlockEntity.NutrientType nutrient;

    NCrop(int stages, SoulFarmlandBlockEntity.NutrientType nutrient)
    {
        this.stages = stages;
        this.nutrient = nutrient;
    }

    public Block create()
    {
        return DefaultNetherCropBlock.create(crop(), stages, this);
    }

    public int getStages()
    {
        return stages;
    }

    public SoulFarmlandBlockEntity.NutrientType getNutrient()
    {
        return nutrient;
    }

    private static ExtendedProperties crop()
    {
        return dead().blockEntity(BeneathBlockEntities.NETHER_CROP).serverTicks(CropBlockEntity::serverTick);
    }

    private static ExtendedProperties dead()
    {
        return ExtendedProperties.of(Material.PLANT).noCollission().randomTicks().strength(0.4F).sound(SoundType.CROP).flammable(60, 30);
    }

}
