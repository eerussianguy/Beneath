package com.eerussianguy.beneath.compat.tooltip;

import java.util.ArrayList;
import java.util.List;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.blockentities.AncientAltarBlockEntity;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import com.eerussianguy.beneath.common.blocks.AncientAltarBlock;
import com.eerussianguy.beneath.common.blocks.HellforgeBlock;
import com.eerussianguy.beneath.common.blocks.HellforgeSideBlock;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;

import net.dries007.tfc.util.tooltip.BlockEntityTooltip;
import net.dries007.tfc.util.tooltip.RegisterCallback;

import static net.dries007.tfc.util.tooltip.BlockEntityTooltips.*;

public class BeneathTooltips
{
    public class BlockEntities
    {

        public static void register(RegisterCallback<BlockEntityTooltip, Block> r)
        {
            register(r, "ancient_altar", ANCIENT_ALTAR, AncientAltarBlock.class);
            register(r, "hellforge", HELLFORGE, HellforgeBlock.class);
            register(r, "hellforge_side", HELLFORGE, HellforgeSideBlock.class);
        }

        private static void register(RegisterCallback<BlockEntityTooltip, Block> r, String name, BlockEntityTooltip tooltip, Class<? extends Block> aClass)
        {
            r.register(Beneath.identifier(name), tooltip, aClass);
        }

        public static final BlockEntityTooltip ANCIENT_ALTAR = (level, state, pos, entity, tooltip) -> {
            if (entity instanceof AncientAltarBlockEntity altar)
            {
                final List<Component> text = new ArrayList<>();
                final ItemStack stack = altar.getStack();
                stack.getItem().appendHoverText(stack, Item.TooltipContext.of(level), text, TooltipFlag.NORMAL);
                text.forEach(tooltip);

                final ItemEnchantments enchants = stack.get(DataComponents.ENCHANTMENTS);
                if (enchants != null)
                    enchants.addToTooltip(Item.TooltipContext.of(level), tooltip, TooltipFlag.NORMAL);
            }
        };

        public static final BlockEntityTooltip HELLFORGE = (level, state, pos, entity, tooltip) -> {
            if (state.getBlock() instanceof HellforgeSideBlock)
            {
                pos = HellforgeSideBlock.getCenterPos(level, pos);
                if (pos == null)
                    return;
//                state = level.getBlockState(pos);
                entity = level.getBlockEntity(pos);
            }

            if (entity instanceof HellforgeBlockEntity forge)
            {
                heat(tooltip, forge.getTemperature());
            }
        };
    }
}
