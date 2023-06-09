package com.eerussianguy.beneath.common.blocks;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BeneathBlockTags
{
    public static final TagKey<Block> BREAKS_SLOWLY = create("breaks_slowly");
    public static final TagKey<Block> HELLFORGE_INSULATION = create("hellforge_insulation");
    public static final TagKey<Block> NETHER_BUSH_PLANTABLE_ON = create("nether_bush_plantable_on");

    private static TagKey<Block> create(String id)
    {
        return TagKey.create(Registry.BLOCK_REGISTRY, Beneath.identifier(id));
    }
}
