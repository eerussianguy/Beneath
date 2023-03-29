package com.eerussianguy.beneath.common.blocks;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BeneathBlockTags
{
    public static final TagKey<Block> BREAKS_SLOWLY = create("breaks_slowly");

    private static TagKey<Block> create(String id)
    {
        return TagKey.create(Registry.BLOCK_REGISTRY, Beneath.identifier(id));
    }
}
