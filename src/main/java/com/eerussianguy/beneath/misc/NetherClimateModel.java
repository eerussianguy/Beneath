package com.eerussianguy.beneath.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;

public class NetherClimateModel implements TimeInvariantClimateModel
{
    @Override
    public ClimateModelType type()
    {
        return BeneathClimateModels.NETHER.get();
    }

    @Override
    public float getTemperature(LevelReader level, BlockPos pos)
    {
        final Biome biome = level.getBiome(pos).value();
        final ResourceLocation name = biome.getRegistryName();
        if (name != null)
        {
            if (name.equals(Biomes.NETHER_WASTES.location()))
            {
                return 70f + altitude(pos);
            }
            else if (name.equals(Biomes.BASALT_DELTAS.location()))
            {
                return 80f + altitude(pos);
            }
            else if (name.equals(Biomes.CRIMSON_FOREST.location()))
            {
                return 60f + altitude(pos);
            }
            else if (name.equals(Biomes.WARPED_FOREST.location()))
            {
                return 60f + altitude(pos);
            }
            else if (name.equals(Biomes.SOUL_SAND_VALLEY.location()))
            {
                return 55f + altitude(pos);
            }
        }
        return 80f;
    }

    public static float altitude(BlockPos pos)
    {
        return -1 * Mth.clampedMap(pos.getY(), 32f, 128f, -5f, 5f);
    }

    @Override
    public float getRainfall(LevelReader level, BlockPos pos)
    {
        return 0;
    }
}
