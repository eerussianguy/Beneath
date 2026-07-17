package com.eerussianguy.beneath.common.items;

import java.util.List;
import java.util.Optional;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.component.BeneathComponents;
import com.eerussianguy.beneath.common.component.LostPage;
import com.eerussianguy.beneath.common.component.TomeComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

public class TomeItem extends Item
{
    public TomeItem(Properties properties)
    {
        super(properties);
    }

    public boolean hasEnchantment(ItemStack stack)
    {
        return stack.has(BeneathComponents.TOME.get());
    }

    @Nullable
    public Holder<Enchantment> getEnchantment(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        return tome != null ? tome.enchantment() : null;
    }

    public int getLevel(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        return tome != null ? tome.level() : 0;
    }

    @Nullable
    public Holder<Item> getOffering(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        return tome != null ? tome.offering() : null;
    }

    public int getCost(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        return tome != null ? tome.cost() : 0;
    }

    public int getUses(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        return tome != null ? tome.uses() : 0;
    }

    public int getMaxUses(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        return tome != null ? tome.maxUses() : 0;
    }

    @Nullable
    public LostPage.Punishment getPunishment(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        return tome != null ? tome.punishment() : null;
    }

    /**
     * Spends one use of the tome. Returns {@code true} if the tome is now spent and should be destroyed; otherwise the
     * stack's component is updated with the decremented use count.
     */
    public boolean spendUse(ItemStack stack)
    {
        final TomeComponent tome = stack.get(BeneathComponents.TOME.get());
        if (tome == null || tome.uses() <= 1)
            return true;
        stack.set(BeneathComponents.TOME.get(), tome.spendUse());
        return false;
    }

    /**
     * Rolls a blank tome into an enchantment. If {@code tools} is non-empty the roll is constrained to enchantments
     * compatible with those tools (intersecting when more than one). Returns false if already rolled or nothing fit.
     */
    public boolean roll(ItemStack stack, RegistryAccess access, RandomSource random, List<ItemStack> tools)
    {
        if (hasEnchantment(stack))
            return false;
        final Optional<TomeComponent> rolled = TomeComponent.roll(access, random, tools);
        if (rolled.isEmpty())
            return false;
        stack.set(BeneathComponents.TOME.get(), rolled.get());
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag)
    {
        final Holder<Enchantment> enchantment = getEnchantment(stack);
        if (enchantment != null)
        {
            tooltip.add(Enchantment.getFullname(enchantment, getLevel(stack)));
            final Holder<Item> offering = getOffering(stack);
            if (offering != null)
            {
                tooltip.add(Component.translatable("beneath.enchant.cost", getCost(stack), offering.value().getDescription()).withStyle(ChatFormatting.RED));
            }
            tooltip.add(Component.translatable("beneath.enchant.uses", getUses(stack)).withStyle(ChatFormatting.GRAY));
            final LostPage.Punishment punishment = getPunishment(stack);
            if (punishment != null && punishment != LostPage.Punishment.NONE)
            {
                tooltip.add(Component.translatable("beneath.enchant.punishment", Beneath.translateEnum(punishment)).withStyle(ChatFormatting.DARK_RED));
            }
        }
        else
        {
            tooltip.add(Component.literal("XXXXXXXXXXXX").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.OBFUSCATED));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return hasEnchantment(stack);
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        final int maxUses = getMaxUses(stack);
        final float fraction = maxUses <= 0 ? 0.0f : (float) getUses(stack) / maxUses;
        return Mth.hsvToRgb(Math.max(0.0f, fraction) / 3.0f, 1.0f, 1.0f);
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        final int maxUses = getMaxUses(stack);
        return maxUses <= 0 ? 0 : Mth.clamp(Math.round(13.0f * getUses(stack) / maxUses), 0, 13);
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        // Only once at least one use has been spent, so a fresh tome shows no bar.
        return hasEnchantment(stack) && getUses(stack) < getMaxUses(stack);
    }
}
