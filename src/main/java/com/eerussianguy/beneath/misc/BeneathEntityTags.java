package com.eerussianguy.beneath.misc;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class BeneathEntityTags
{
    public static final TagKey<EntityType<?>> CAN_BE_SACRIFICED = create("can_be_sacrificed");

    private static TagKey<EntityType<?>> create(String id)
    {
        return TagKey.create(Registries.ENTITY_TYPE, Beneath.identifier(id));
    }

}
