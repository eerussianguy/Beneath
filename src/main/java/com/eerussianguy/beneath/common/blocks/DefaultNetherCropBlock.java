package com.eerussianguy.beneath.common.blocks;

import java.util.function.Supplier;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.items.TFCItems;

public abstract class DefaultNetherCropBlock extends NetherCropBlock
{

    public static DefaultNetherCropBlock create(ExtendedProperties properties, int stages, NCrop crop)
    {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages - 1);
        return new DefaultNetherCropBlock(properties, stages - 1, TFCItems.CROP_SEEDS.get(crop), crop.getNutrient())
        {
            @Override
            public IntegerProperty getAgeProperty()
            {
                return property;
            }
        };
    }

    protected DefaultNetherCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Item> seeds, SoulFarmlandBlockEntity.NutrientType primaryNutrient)
    {
        super(properties, maxAge, seeds, primaryNutrient);
    }

    @Override
    public void die(Level level, BlockPos pos, BlockState state, boolean fullyGrown)
    {
        level.setBlockAndUpdate(pos, state.setValue(getAgeProperty(), 0));
        if (level.getBlockEntity(pos) instanceof CropBlockEntity crop)
        {
            crop.setGrowth(0f);
            crop.setYield(0f);
            crop.setExpiry(0f);
        }
    }

    @Override
    protected void postGrowthTick(Level level, BlockPos pos, BlockState state, CropBlockEntity crop)
    {
        final int age = crop.getGrowth() == 1 ? getMaxAge() : (int) (crop.getGrowth() * getMaxAge());
        level.setBlockAndUpdate(pos, state.setValue(getAgeProperty(), age));
    }
}
