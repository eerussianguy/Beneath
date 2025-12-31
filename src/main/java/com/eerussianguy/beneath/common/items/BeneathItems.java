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
import net.minecraft.core.registries.Registries;
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
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.entities.TFCEntities;
import net.dries007.tfc.common.items.TFCBoatItem;
import net.dries007.tfc.common.items.TFCMinecartItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.common.items.TFCItems.ItemId;
import net.dries007.tfc.util.registry.IdHolder;

@SuppressWarnings("unused")
public class BeneathItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Beneath.MOD_ID);

    public static final ItemId AGONIZING_FERTILIZER = register("agonizing_fertilizer");
    public static final ItemId BLACKSTONE_BRICK = register("blackstone_brick");
    public static final ItemId CRACKRACK_ROCK = register("crackrack_rock");
    public static final ItemId CRIMSON_STRAW = register("crimson_straw");
    public static final ItemId CURSECOAL = register("cursecoal");
    public static final ItemId CURSED_HIDE = register("cursed_hide");
    public static final ItemId GOLD_CHUNK = register("gold_chunk");
    public static final ItemId GHOST_PEPPER = register("ghost_pepper", () -> new Item(food()));
    public static final ItemId JUICER = register("juicer", () -> new JuicerItem(new Item.Properties().stacksTo(1)));
    public static final ItemId LOST_PAGE = register("lost_page", () -> new LostPageItem(new Item.Properties()));
    public static final ItemId RAW_SLIME = register("raw_slime");
    public static final ItemId WARPED_STRAW = register("warped_straw");

    public static final Map<Shroom, ItemId> SHROOMS = Helpers.mapOf(Shroom.class, shroom -> register("food/" + shroom.getSerializedName(), () -> new Item(new Item.Properties().food(getPoisonProperties(shroom.isPoison())))));
    public static final Map<Stem, ItemId> LUMBER = Helpers.mapOf(Stem.class, wood -> register("wood/lumber/" + wood.name()));
    public static final Map<Stem, ItemId> SUPPORTS = Helpers.mapOf(Stem.class, wood ->
        register("wood/support/" + wood.name(), () -> new StandingAndWallBlockItem(BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.VERTICAL_SUPPORT).get(), BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.HORIZONTAL_SUPPORT).get(), new Item.Properties(), Direction.DOWN))
    );
    public static final Map<Stem, ItemId> BOATS = Helpers.mapOf(Stem.class, wood -> register("wood/boat/" + wood.name(), () -> new TFCBoatItem(BeneathEntities.BOATS.get(wood), new Item.Properties())));
    public static final Map<Stem, ItemId> CHEST_MINECARTS = Helpers.mapOf(Stem.class, wood -> register("wood/chest_minecart/" + wood.name(), () -> new TFCMinecartItem(new Item.Properties(), TFCEntities.CHEST_MINECART, () -> BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.CHEST).get().asItem())));
    public static final Map<Stem, ItemId> SIGNS = Helpers.mapOf(Stem.class, wood -> register("wood/sign/" + wood.name(), () -> new SignItem(new Item.Properties(), BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.SIGN).get(), BeneathBlocks.WOODS.get(wood).get(Wood.BlockType.WALL_SIGN).get())));
    public static final Map<Stem, Map<Metal, ItemId>> HANGING_SIGNS = Helpers.mapOf(Stem.class, wood ->
        Helpers.mapOf(Metal.class, Metal::allParts, metal ->
            register("wood/hanging_sign/" + metal.name() + "/" + wood.name(), () -> new HangingSignItem(BeneathBlocks.CEILING_HANGING_SIGNS.get(wood).get(metal).get(), BeneathBlocks.WALL_HANGING_SIGNS.get(wood).get(metal).get(), new Item.Properties()))
        )
    );

    public static final Map<NCrop, ItemId> SEEDS = Helpers.mapOf(NCrop.class, crop ->
        register("seeds/" + crop.name(), () -> new ItemNameBlockItem(BeneathBlocks.CROPS.get(crop).get(), new Item.Properties()))
    );
    public static final Map<SoulFarmlandBlockEntity.NutrientType, ItemId> PURE_NUTRIENTS = Helpers.mapOf(SoulFarmlandBlockEntity.NutrientType.class, nut ->
        register("pure_" + nut.name())
    );

    public static final ItemId RED_ELK_EGG = registerSpawnEgg(BeneathEntities.RED_ELK);

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
        if (fast) builder.fast();
        return builder.nutrition(4).saturationModifier(0.3f).build();
    }

    private static <T extends Mob> ItemId registerSpawnEgg(IdHolder<EntityType<T>> entity)
    {
        return register("spawn_egg/" + entity.getId().getPath(), () -> new DeferredSpawnEggItem(entity.holder(), 0xffffff, 0xffffff, new Item.Properties()));
    }

    private static ItemId register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> ItemId register(String name, Supplier<T> item)
    {
        return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }
}
