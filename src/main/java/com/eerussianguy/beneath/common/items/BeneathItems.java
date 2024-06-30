package com.eerussianguy.beneath.common.items;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import com.eerussianguy.beneath.common.blocks.NCrop;
import com.eerussianguy.beneath.common.blocks.Shroom;
import com.eerussianguy.beneath.common.blocks.Stem;
import com.eerussianguy.beneath.common.entities.BeneathEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.entities.TFCEntities;
import net.dries007.tfc.common.items.TFCBoatItem;
import net.dries007.tfc.common.items.TFCMinecartItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

@SuppressWarnings("unused")
public class BeneathItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Beneath.MOD_ID);

    public static final RegistryObject<Item> AGONIZING_FERTILIZER = register("agonizing_fertilizer");
    public static final RegistryObject<Item> BLACKSTONE_BRICK = register("blackstone_brick");
    public static final RegistryObject<Item> CRACKRACK_ROCK = register("crackrack_rock");
    public static final RegistryObject<Item> CRIMSON_STRAW = register("crimson_straw");
    public static final RegistryObject<Item> CURSECOAL = register("cursecoal");
    public static final RegistryObject<Item> CURSED_HIDE = register("cursed_hide");
    public static final RegistryObject<Item> GOLD_CHUNK = register("gold_chunk");
    public static final RegistryObject<Item> GHOST_PEPPER = register("ghost_pepper", () -> new Item(food()));
    public static final RegistryObject<Item> JUICER = register("juicer", () -> new JuicerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WARPED_STRAW = register("warped_straw");

    public static final Map<Shroom, RegistryObject<Item>> SHROOMS = Helpers.mapOfKeys(Shroom.class, shroom -> register("food/" + shroom.getSerializedName(), () -> new Item(new Item.Properties().food(getPoisonProperties(shroom.isPoison())))));
    public static final Map<Stem, RegistryObject<Item>> LUMBER = Helpers.mapOfKeys(Stem.class, wood -> register("wood/lumber/" + wood.name()));
    public static final Map<Stem, RegistryObject<Item>> SUPPORTS = Helpers.mapOfKeys(Stem.class, wood ->
        register("wood/support/" + wood.name(), () -> new StandingAndWallBlockItem(BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.VERTICAL_SUPPORT).get(), BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.HORIZONTAL_SUPPORT).get(), new Item.Properties(), Direction.DOWN))
    );
    public static final Map<Stem, RegistryObject<Item>> BOATS = Helpers.mapOfKeys(Stem.class, wood -> register("wood/boat/" + wood.name(), () -> new TFCBoatItem(BeneathEntities.BOATS.get(wood), new Item.Properties())));
    public static final Map<Stem, RegistryObject<Item>> CHEST_MINECARTS = Helpers.mapOfKeys(Stem.class, wood -> register("wood/chest_minecart/" + wood.name(), () -> new TFCMinecartItem(new Item.Properties(), TFCEntities.CHEST_MINECART, () -> BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.CHEST).get().asItem())));
    public static final Map<Stem, RegistryObject<Item>> SIGNS = Helpers.mapOfKeys(Stem.class, wood -> register("wood/sign/" + wood.name(), () -> new SignItem(new Item.Properties(), BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.SIGN).get(), BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.WALL_SIGN).get())));
    public static final Map<Stem, Map<Metal.Default, RegistryObject<Item>>> HANGING_SIGNS = Helpers.mapOfKeys(Stem.class, wood ->
        Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasUtilities, metal ->
            register("wood/hanging_sign/" + metal.name() + "/" + wood.name(), () -> new HangingSignItem(BeneathBlocks.CEILING_HANGING_SIGNS.get(wood).get(metal).get(), BeneathBlocks.WALL_HANGING_SIGNS.get(wood).get(metal).get(), new Item.Properties()))
        )
    );

    public static final Map<NCrop, RegistryObject<Item>> SEEDS = Helpers.mapOfKeys(NCrop.class, crop ->
        register("seeds/" + crop.name(), () -> new ItemNameBlockItem(BeneathBlocks.CROPS.get(crop).get(), new Item.Properties()))
    );
    public static final Map<SoulFarmlandBlockEntity.NutrientType, RegistryObject<Item>> PURE_NUTRIENTS = Helpers.mapOfKeys(SoulFarmlandBlockEntity.NutrientType.class, nut ->
        register("pure_" + nut.name())
    );

    public static final RegistryObject<Item> RED_ELK_EGG = registerSpawnEgg(BeneathEntities.RED_ELK, 10236982, 6387319);

    public static Item.Properties food()
    {
        return new Item.Properties().food(getFoodProperties());
    }

    public static FoodProperties getPoisonProperties(boolean poison)
    {
        if (!poison)
            return getFoodProperties();
        return new FoodProperties.Builder().effect(() -> new MobEffectInstance(MobEffects.POISON, 1200, 1), 1.0F).build();
    }

    public static FoodProperties getFoodProperties()
    {
        return getFoodProperties(false, false);
    }

    public static FoodProperties getFoodProperties(boolean meat, boolean fast)
    {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        if (meat) builder.meat();
        if (fast) builder.fast();
        return builder.nutrition(4).saturationMod(0.3f).build();
    }

    private static <T extends EntityType<? extends Mob>> RegistryObject<Item> registerSpawnEgg(RegistryObject<T> entity, int color1, int color2)
    {
        return register("spawn_egg/" + entity.getId().getPath(), () -> new ForgeSpawnEggItem(entity, color1, color2, new Item.Properties()));
    }

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
