package com.eerussianguy.beneath.misc;

import java.util.function.Supplier;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import javax.annotation.Nonnull;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public class ItemGroup extends CreativeModeTab
{
    public static final ItemGroup BENEATH = new ItemGroup("beneath", () -> BeneathBlocks.NETHER_PEBBLE.get().asItem().getDefaultInstance());

    private final Lazy<ItemStack> stack;

    public ItemGroup(String label, Supplier<ItemStack> icon)
    {
        super(Beneath.MOD_ID + "." + label);
        this.stack = Lazy.of(icon);
    }

    @Override
    @Nonnull
    public ItemStack makeIcon()
    {
        return stack.get();
    }
}
