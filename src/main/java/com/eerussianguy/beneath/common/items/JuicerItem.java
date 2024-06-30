package com.eerussianguy.beneath.common.items;

import java.util.List;
import com.eerussianguy.beneath.common.container.JuicerContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.container.ItemStackContainerProvider;
import net.dries007.tfc.common.items.JugItem;
import net.dries007.tfc.util.Tooltips;

public class JuicerItem extends JugItem
{
    public static final int CAPACITY = 250;

    public JuicerItem(Properties properties)
    {
        super(properties, () -> CAPACITY, TFCTags.Fluids.USABLE_IN_JUG);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag isAdvanced)
    {
        stack.getCapability(Capabilities.FLUID_ITEM).ifPresent(cap -> {
            if (!cap.getFluidInTank(0).isEmpty())
            {
                tooltips.add(Tooltips.fluidUnitsAndCapacityOf(cap.getFluidInTank(0), CAPACITY));
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        final ItemStack stack = player.getItemInHand(hand);
        if (!hasFluid(stack))
        {
            if (player instanceof ServerPlayer serverPlayer)
            {
                final var provider = new ItemStackContainerProvider(JuicerContainer::create);
                provider.openScreen(serverPlayer, hand);
            }
            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, hand);
    }

    private boolean hasFluid(ItemStack stack)
    {
        return stack.getCapability(Capabilities.FLUID_ITEM).map(cap -> !cap.getFluidInTank(0).isEmpty()).orElse(false);
    }
}
