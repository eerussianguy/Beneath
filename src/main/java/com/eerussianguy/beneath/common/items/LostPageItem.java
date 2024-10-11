package com.eerussianguy.beneath.common.items;

import java.util.List;
import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.common.container.BeneathContainerTypes;
import com.eerussianguy.beneath.misc.LostPage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

import net.dries007.tfc.util.Helpers;

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
        if (player instanceof ServerPlayer serverPlayer)
        {
            Helpers.openScreen(serverPlayer, BeneathContainerTypes.LOST_PAGE_PROVIDER, buf -> buf.writeItem(stack));
        }
        return InteractionResultHolder.success(stack);
    }

    public void init(ItemStack stack, RandomSource random, LostPage data)
    {
        final CompoundTag tag = stack.getOrCreateTag();
        if (hasInitialized(stack))
            return;
        tag.putString("beneath:lost_page_id", data.getId().toString());
        tag.putInt("beneath:cost_amount", data.getCosts().get(random.nextInt(data.getCosts().size())));
        tag.putInt("beneath:reward_amount", data.getRewards().get(random.nextInt(data.getRewards().size())));
        final List<LostPage.Punishment> pun = data.getPunishments();
        tag.putInt("beneath:punishment", pun.isEmpty() ? LostPage.Punishment.NONE.ordinal() : pun.get(random.nextInt(pun.size())).ordinal());
    }

    public boolean hasInitialized(ItemStack stack)
    {
        final CompoundTag tag = stack.getTag();
        return tag != null && (tag.contains("beneath:cost") || tag.contains("beneath:reward"));
    }

    public Ingredient getCost(ItemStack stack)
    {
        final LostPage page = getLostPage(stack);
        if (page != null)
        {
            return page.getCost();
        }
        return Ingredient.EMPTY;
    }

    @Nullable
    public MutableComponent getIngredientTranslation(ItemStack stack)
    {
        final LostPage page = getLostPage(stack);
        if (page != null)
        {
            return page.getIngredientTranslation() != null ? Component.translatable(page.getIngredientTranslation()) : null;
        }
        return null;
    }

    public ItemStack getReward(ItemStack stack)
    {
        final LostPage page = getLostPage(stack);
        return page != null ? page.getReward().getDefaultInstance() : ItemStack.EMPTY;
    }

    public int getCostAmount(ItemStack stack)
    {
        final CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("beneath:cost_amount", CompoundTag.TAG_INT))
        {
            return tag.getInt("beneath:cost_amount");
        }
        return 0;
    }

    public int getRewardAmount(ItemStack stack)
    {
        final CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("beneath:reward_amount", CompoundTag.TAG_INT))
        {
            return tag.getInt("beneath:reward_amount");
        }
        return 0;
    }

    public LostPage.Punishment getPunishment(ItemStack stack)
    {
        final CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("beneath:punishment", CompoundTag.TAG_INT))
        {
            return LostPage.Punishment.valueOf(tag.getInt("beneath:punishment"));
        }
        return LostPage.Punishment.NONE;
    }

    @Nullable
    public LostPage getLostPage(ItemStack stack)
    {
        final CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("beneath:lost_page_id", CompoundTag.TAG_STRING))
        {
            return LostPage.MANAGER.get(new ResourceLocation(tag.getString("beneath:lost_page_id")));
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag debug)
    {
        if (hasInitialized(stack))
        {
            final Ingredient cost = getCost(stack);
            Component trans = getIngredientTranslation(stack);
            if (trans == null)
            {
                if (cost.isEmpty())
                    return;
                trans = cost.getItems()[0].getHoverName();
            }
            tooltip.add(Component.translatable("beneath.screen.lost_page.cost").append(Component.literal(": ")).append(Component.literal(getCostAmount(stack) + "x ").append(trans)));
            tooltip.add(Component.translatable("beneath.screen.lost_page.reward").append(Component.literal(": ")).append(Component.literal(getRewardAmount(stack) + "x ").append(getReward(stack).getHoverName())));
            tooltip.add(Component.translatable("beneath.screen.lost_page.punishment").append(Component.literal(": ")).append(Beneath.translateEnum(getPunishment(stack))));
        }
     }
}
