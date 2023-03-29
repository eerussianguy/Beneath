package com.eerussianguy.beneath.common.entities;

import java.util.Locale;
import java.util.Map;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.entities.blaze_leviathan.BasicFireball;
import com.eerussianguy.beneath.common.entities.blaze_leviathan.BlazeLeviathanBoss;
import com.eerussianguy.beneath.common.entities.prey.NetherPrey;
import com.eerussianguy.beneath.common.items.BeneathItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.entities.TFCBoat;
import net.dries007.tfc.util.Helpers;

import static net.dries007.tfc.TerraFirmaCraft.*;

public class BeneathEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Beneath.MOD_ID);

    public static final RegistryObject<EntityType<BlazeLeviathanBoss>> BLAZE_LEVIATHAN = register("blaze_leviathan", EntityType.Builder.of(BlazeLeviathanBoss::new, MobCategory.MONSTER).fireImmune().sized(4f, 9f).clientTrackingRange(10));
    public static final RegistryObject<EntityType<BasicFireball>> LEVIATHAN_FIREBALL = register("leviathan_fireball", EntityType.Builder.<BasicFireball>of(BasicFireball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10));

    public static final RegistryObject<EntityType<NetherPrey>> RED_ELK = register("red_elk", EntityType.Builder.of(NetherPrey::makeDeer, MobCategory.CREATURE).sized(1.0F, 1.3F).fireImmune().clientTrackingRange(10));

    public static final Map<Stem, RegistryObject<EntityType<TFCBoat>>> BOATS = Helpers.mapOfKeys(Stem.class, wood ->
        register("boat/" + wood.name(), EntityType.Builder.<TFCBoat>of((type, level) -> new TFCBoat(type, level, BeneathItems.BOATS.get(wood)), MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10))
    );

    public static <E extends Entity> RegistryObject<EntityType<E>> register(String name, EntityType.Builder<E> builder)
    {
        return register(name, builder, true);
    }

    public static <E extends Entity> RegistryObject<EntityType<E>> register(String name, EntityType.Builder<E> builder, boolean serialize)
    {
        final String id = name.toLowerCase(Locale.ROOT);
        return ENTITIES.register(id, () -> {
            if (!serialize) builder.noSave();
            return builder.build(MOD_ID + ":" + id);
        });
    }

    public static void onAttributes(EntityAttributeCreationEvent event)
    {
        event.put(BLAZE_LEVIATHAN.get(), BlazeLeviathanBoss.createAttributes().build());
        event.put(RED_ELK.get(), NetherPrey.createAttributes().build());
    }

    public static void onSpawnPlacement()
    {
        SpawnPlacements.register(RED_ELK.get(), SpawnPlacements.Type.IN_LAVA, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NetherPrey::checkSpawnRules);
    }
}
