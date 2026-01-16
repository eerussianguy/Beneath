package com.eerussianguy.beneath.common.items;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BeneathItemTags
{
    public static final TagKey<Item> SPARKS_ON_SULFUR = create("sparks_on_sulfur");
    public static final TagKey<Item> USABLE_IN_JUICER = create("usable_in_juicer");
    public static final TagKey<Item> UNPOSTABLE = create("unpostable");
    public static final TagKey<Item> CRIMSON_LOGS = create("crimson_logs");
    public static final TagKey<Item> WARPED_LOGS = create("warped_logs");
    public static final TagKey<Item> MUSHROOMS = create("mushrooms");

    private static TagKey<Item> create(String id)
    {
        return TagKey.create(Registries.ITEM, Beneath.identifier(id));
    }

}
