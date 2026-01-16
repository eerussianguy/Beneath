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
    public static final TagKey<Block> EVENT_REPLACEABLE = create("event_replaceable");
    public static final TagKey<Block> NETHER_BRICKS = create("nether_bricks");
    public static final TagKey<Block> NETHER_BRICK_DECOR = create("nether_brick_decor");
    public static final TagKey<Block> BLACKSTONE = create("blackstone");
    public static final TagKey<Block> BLACKSTONE_DECOR = create("blackstone_decor");
    public static final TagKey<Block> CRIMSON_LOGS = create("crimson_logs");
    public static final TagKey<Block> WARPED_LOGS = create("warped_logs");

    private static TagKey<Block> create(String id)
    {
        return TagKey.create(Registries.BLOCK, Beneath.identifier(id));
    }
}
