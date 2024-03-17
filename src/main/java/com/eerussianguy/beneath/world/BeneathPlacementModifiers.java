package com.eerussianguy.beneath.world;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.world.feature.NearLavaPlacement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BeneathPlacementModifiers
{
    public static final DeferredRegister<PlacementModifierType<?>> MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Beneath.MOD_ID);

    public static final RegistryObject<PlacementModifierType<NearLavaPlacement>> NEAR_LAVA = register("near_lava", () -> NearLavaPlacement.CODEC);

    private static <T extends PlacementModifier> RegistryObject<PlacementModifierType<T>> register(String name, PlacementModifierType<T> codec)
    {
        return MODIFIERS.register(name, () -> codec);
    }

}
