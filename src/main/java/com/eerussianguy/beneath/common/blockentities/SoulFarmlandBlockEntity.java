package com.eerussianguy.beneath.common.blockentities;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.util.Helpers;

public class SoulFarmlandBlockEntity extends TFCBlockEntity
{
    private final float[] nutrients = new float[NutrientType.VALUES.length];

    public SoulFarmlandBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        Arrays.fill(nutrients, 0f);
    }

    public SoulFarmlandBlockEntity(BlockPos pos, BlockState state)
    {
        super(BeneathBlockEntities.SOUL_FARMLAND.get(), pos, state);
    }

    public void addTooltipInfo(List<Component> tooltip)
    {
        for (NutrientType type : NutrientType.VALUES)
        {
            if (getNutrient(type) > 0)
            {
                tooltip.add(Helpers.translatable("beneath.nutrient." + type.getName(), format(getNutrient(type))));
            }
        }
    }

    private String format(float value)
    {
        return String.format("%.2f", value * 100);
    }

    @Override
    protected void loadAdditional(CompoundTag tag)
    {
        super.loadAdditional(tag);
        for (NutrientType type : NutrientType.VALUES)
        {
            nutrients[type.ordinal()] = tag.getFloat(type.name);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        for (NutrientType type : NutrientType.VALUES)
        {
            tag.putFloat(type.name, nutrients[type.ordinal()]);
        }
    }

    public float getNutrient(NutrientType type)
    {
        return nutrients[type.ordinal()];
    }

    public void addNutrient(NutrientType type, float value)
    {
        setNutrient(type, Mth.clamp(nutrients[type.ordinal()] + value, 0f, 1f));
        markForSync();
    }

    public float consumeNutrient(NutrientType type, float amount)
    {
        final float startValue = getNutrient(type);
        final float consumed = Math.min(startValue, amount);
        setNutrient(type, startValue - consumed);
        return consumed;
    }

    public void setMinimumNutrient(NutrientType type, float minimumValue)
    {
        setNutrient(type, Math.max(nutrients[type.ordinal()], minimumValue));
    }

    public void addUpToMinimumNutrient(NutrientType type, float minimumValue, float add)
    {
        final float current = nutrients[type.ordinal()];
        setNutrient(type, Math.min(minimumValue, current + add));
    }

    public void setNutrient(NutrientType type, float value)
    {
        nutrients[type.ordinal()] = value;
        markForSync();
    }

    public enum NutrientType
    {
        DEATH,
        DESTRUCTION,
        DECAY,
        SORROW,
        FLAME,
        ;

        public static final NutrientType[] VALUES = values();

        private final String name;

        NutrientType()
        {
            this.name = name().toLowerCase(Locale.ROOT);
        }

        public String getName()
        {
            return name;
        }
    }
}
