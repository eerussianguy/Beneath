package com.eerussianguy.beneath.common.blocks;

import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BeneathBlockTags
{
    public static final TagKey<Block> BREAKS_SLOWLY = create("breaks_slowly");
    public static final TagKey<Block> HELLFORGE_INSULATION = create("hellforge_insulation");
    public static final TagKey<Block> NETHER_BUSH_PLANTABLE_ON = create("nether_bush_plantable_on");
    public static final TagKey<Block> MUSHROOMS = create("mushrooms");

    private static TagKey<Block> create(String id)
    {
        return TagKey.create(Registries.BLOCK, Beneath.identifier(id));
    }
}
