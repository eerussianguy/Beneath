package com.eerussianguy.beneath.common.blocks;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.registry.RegistryWood;

import static net.dries007.tfc.common.blocks.wood.Wood.BlockType.*;

public enum Stem implements RegistryWood
{
    CRIMSON(true, MapColor.TERRACOTTA_RED, MapColor.COLOR_RED, 8, 0.0428f),
    WARPED(true, MapColor.TERRACOTTA_BLUE, MapColor.COLOR_BLUE, 8, 0.0115f);

    public static final Stem[] VALUES = values();

    private final String serializedName;
    private final boolean conifer;
    private final MapColor woodColor;
    private final MapColor barkColor;
    private final TreeGrower tree;
    private final int ticksToGrow;
    private final BlockSetType blockSet;
    private final WoodType woodType;
    private final float saplingChance;

    Stem(boolean conifer, MapColor woodColor, MapColor barkColor, int daysToGrow, float saplingChance)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.conifer = conifer;
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.tree = new TreeGrower(
            Beneath.identifier(serializedName).toString(),
            Optional.empty(),
            Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, Beneath.identifier("tree/" + serializedName))),
            Optional.empty()
        );
        this.blockSet = new BlockSetType(serializedName);
        this.woodType = new WoodType(Beneath.identifier(this.serializedName).toString(), this.blockSet);
        this.ticksToGrow = daysToGrow * ICalendar.CALENDAR_TICKS_IN_DAY;
        this.saplingChance = saplingChance;
    }

    public float getSaplingDropChance()
    {
        return saplingChance;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public boolean isConifer()
    {
        return conifer;
    }

    @Override
    public MapColor woodColor()
    {
        return woodColor;
    }

    @Override
    public MapColor barkColor()
    {
        return barkColor;
    }

    @Override
    public TreeGrower tree()
    {
        return tree;
    }

    @Override
    public Supplier<Integer> ticksToGrow()
    {
        return () -> ticksToGrow;
    }

    @Override
    public int autumnIndex()
    {
        return 0;
    }

    @Override
    public Supplier<Block> getBlock(Wood.BlockType type)
    {
        return BeneathBlocks.WOODS.get(this).get(type);
    }

    @SuppressWarnings("unchecked")
    public <B extends Block> Supplier<? extends B> getBlockCasted(Wood.BlockType type)
    {
        return (Supplier<? extends B>) this.getBlock(type);
    }

    @Override
    public BlockSetType getBlockSet()
    {
        return blockSet;
    }

    @Override
    public WoodType getVanillaWoodType()
    {
        return woodType;
    }

    public Wood.BlockType twig() { return TWIG; }
    public Wood.BlockType fallenLeaves() { return FALLEN_LEAVES; }
    public Wood.BlockType leaves() { return LEAVES; }
    public Wood.BlockType axle() { return AXLE; }
    public Wood.BlockType windmill() { return WINDMILL; }
}
