package com.eerussianguy.beneath.common.entities;

import java.util.Locale;
import java.util.Map;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.entities.prey.NetherPrey;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.entities.misc.TFCBoat;
import net.dries007.tfc.common.entities.misc.TFCChestBoat;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.common.entities.TFCEntities.Id;

public class BeneathEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Beneath.MOD_ID);

    public static final Id<NetherPrey> RED_ELK = register("red_elk", EntityType.Builder.of(NetherPrey::makeDeer, MobCategory.CREATURE).sized(1.0F, 1.3F).fireImmune().clientTrackingRange(10));

    public static final Map<Stem, Id<TFCChestBoat>> CHEST_BOATS = Helpers.mapOf(Stem.class, wood ->
        register("chest_boat/" + wood.name(), EntityType.Builder.<TFCChestBoat>of((type, level) -> new TFCChestBoat(type, level, BeneathItems.BOATS.get(wood)), MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10))
    );
    public static final Map<Stem, Id<TFCBoat>> BOATS = Helpers.mapOf(Stem.class, wood ->
        register("boat/" + wood.name(), EntityType.Builder.<TFCBoat>of((type, level) -> new TFCBoat(type, level, CHEST_BOATS.get(wood), BeneathItems.BOATS.get(wood)), MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10))
    );

    public static <E extends Entity> Id<E> register(String name, EntityType.Builder<E> builder)
    {
        return register(name, builder, true);
    }

    public static <E extends Entity> Id<E> register(String name, EntityType.Builder<E> builder, boolean serialize)
    {
        final String id = name.toLowerCase(Locale.ROOT);
        return new Id<>(ENTITIES.register(id, () -> {
            if (!serialize) builder.noSave();
            return builder.build(Beneath.MOD_ID + ":" + id);
        }));
    }

    public static void onAttributes(EntityAttributeCreationEvent event)
    {
        event.put(RED_ELK.get(), NetherPrey.createAttributes().build());
    }

    public static void onSpawnPlacement(RegisterSpawnPlacementsEvent event)
    {
        event.register(RED_ELK.get(), SpawnPlacementTypes.IN_LAVA, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NetherPrey::spawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
