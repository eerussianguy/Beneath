package com.eerussianguy.beneath.common.items;

import java.util.List;
import com.eerussianguy.beneath.common.container.JuicerContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;

import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.container.ItemStackContainerProvider;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.items.JugItem;
import net.dries007.tfc.util.tooltip.Tooltips;

public class JuicerItem extends JugItem
{
    public static final int CAPACITY = 250;

    public JuicerItem(Properties properties)
    {
        super(properties, () -> CAPACITY, TFCTags.Fluids.USABLE_IN_JUG);
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag isAdvanced)
    {
        final var cap = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (cap != null && !cap.getFluidInTank(0).isEmpty())
        {
            tooltips.add(Tooltips.fluidUnitsAndCapacityOf(cap.getFluidInTank(0), CAPACITY));
        }
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

    @Override
    public int getBarColor(ItemStack stack)
    {
        final FluidStack fluid = FluidHelpers.getContainedFluid(stack);
        if (!fluid.isEmpty())
        {
            final int color = RenderHelpers.getFluidColor(fluid);
            final int r = FastColor.ARGB32.red(color);
            final int g = FastColor.ARGB32.green(color);
            final int b = FastColor.ARGB32.blue(color);
            return FastColor.ARGB32.color(0, r, g, b);
        }
        return 0xFFFFF;
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        final FluidStack fluid = FluidHelpers.getContainedFluid(stack);
        return fluid.isEmpty() ? 0 : (int) Mth.clamp((float) fluid.getAmount() / containerInfo.fluidCapacity() * 13, 1, 13);
    }

    private boolean hasFluid(ItemStack stack)
    {
        final var cap = stack.getCapability(Capabilities.FluidHandler.ITEM);
        return cap != null && !cap.getFluidInTank(0).isEmpty();
    }
}
