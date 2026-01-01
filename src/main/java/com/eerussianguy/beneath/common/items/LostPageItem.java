package com.eerussianguy.beneath.common.items;

import java.util.List;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.client.BeneathClientUtil;
import com.eerussianguy.beneath.common.component.BeneathComponents;
import com.eerussianguy.beneath.common.component.LostPage;
import com.eerussianguy.beneath.common.component.LostPageComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LostPageItem extends Item
{
    public LostPageItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof LostPageItem item && item.getReward(stack).isEmpty())
        {
            return InteractionResultHolder.pass(stack);
        }
        if (level.isClientSide)
        {
            BeneathClientUtil.openLostPageScreen(stack);
        }
        return InteractionResultHolder.success(stack);
    }

    public boolean hasInitialized(ItemStack stack)
    {
        final LostPageComponent page = stack.get(BeneathComponents.LOST_PAGE.get());
        return page != null;
    }

    public Ingredient getCost(ItemStack stack)
    {
        final LostPage page = getLostPage(stack);
        return page != null ? page.cost() : Ingredient.EMPTY;
    }

    @Nullable
    public MutableComponent getIngredientTranslation(ItemStack stack)
    {
        final LostPage page = getLostPage(stack);
        if (page != null)
        {
            return page.translation().map(Component::copy).orElse(null);
        }
        return null;
    }

    public Component getSpecificIngredientTranslation(ItemStack stack)
    {
        final Ingredient cost = getCost(stack);
        Component trans = getIngredientTranslation(stack);
        if (trans == null)
        {
            if (cost.isEmpty())
                return Component.empty();
            trans = cost.getItems()[0].getHoverName();
        }
        return trans;
    }

    public ItemStack getReward(ItemStack stack)
    {
        final LostPage page = getLostPage(stack);
        return page != null ? page.reward().value().getDefaultInstance() : ItemStack.EMPTY;
    }

    public int getCostAmount(ItemStack stack)
    {
        final LostPageComponent page = stack.get(BeneathComponents.LOST_PAGE.get());
        return page != null ? page.cost() : 0;
    }

    public int getRewardAmount(ItemStack stack)
    {
        final LostPageComponent page = stack.get(BeneathComponents.LOST_PAGE.get());
        return page != null ? page.reward() : 0;
    }

    public LostPage.Punishment getPunishment(ItemStack stack)
    {
        final LostPageComponent page = stack.get(BeneathComponents.LOST_PAGE.get());
        return page != null ? page.punishment() : LostPage.Punishment.NONE;
    }

    @Nullable
    public LostPage getLostPage(ItemStack stack)
    {
        final LostPageComponent page = stack.get(BeneathComponents.LOST_PAGE.get());
        return page != null ? page.parent() : null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag)
    {
        if (hasInitialized(stack))
        {
            tooltip.add(Component.translatable("beneath.screen.lost_page.cost").withStyle(ChatFormatting.RED).append(Component.literal(": ")).append(Component.literal(getCostAmount(stack) + "x ").append(getSpecificIngredientTranslation(stack))));
            tooltip.add(Component.translatable("beneath.screen.lost_page.reward").withStyle(ChatFormatting.GOLD).append(Component.literal(": ")).append(Component.literal(getRewardAmount(stack) + "x ").append(getReward(stack).getHoverName())));
            tooltip.add(Component.translatable("beneath.screen.lost_page.punishment").withStyle(ChatFormatting.BLUE).append(Component.literal(": ")).append(Beneath.translateEnum(getPunishment(stack))));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return hasInitialized(stack);
    }
}
