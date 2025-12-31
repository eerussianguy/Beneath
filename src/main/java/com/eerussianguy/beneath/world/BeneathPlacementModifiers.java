package com.eerussianguy.beneath.world;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.world.feature.HeightLimitPlacement;
import com.eerussianguy.beneath.world.feature.NearLavaPlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.world.placement.TFCPlacements.Id;

public class BeneathPlacementModifiers
{
    public static final DeferredRegister<PlacementModifierType<?>> MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Beneath.MOD_ID);

    public static final Id<NearLavaPlacement> NEAR_LAVA = register("near_lava", () -> NearLavaPlacement.CODEC);
    public static final Id<HeightLimitPlacement> HEIGHT_LIMIT = register("height_limit", () -> HeightLimitPlacement.CODEC);

    private static <C extends PlacementModifier> Id<C> register(String name, PlacementModifierType<C> codec)
    {
        return new Id<>(MODIFIERS.register(name, () -> codec));
    }

}
