package com.eerussianguy.beneath.common.items;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BeneathItemTags
{
    public static final TagKey<Item> SPARKS_ON_SULFUR = create("sparks_on_sulfur");
    public static final TagKey<Item> USABLE_IN_JUICER = create("usable_in_juicer");
    public static final TagKey<Item> UNPOSTABLE = create("unpostable");

    private static TagKey<Item> create(String id)
    {
        return TagKey.create(Registries.ITEM, Beneath.identifier(id));
    }

}
