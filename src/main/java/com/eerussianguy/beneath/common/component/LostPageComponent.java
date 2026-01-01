package com.eerussianguy.beneath.common.component;

import java.util.List;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public record LostPageComponent(LostPage parent, int cost, int reward, LostPage.Punishment punishment)
{
    public static final Codec<LostPageComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
        LostPage.CODEC.fieldOf("parent").forGetter(c -> c.parent),
        Codec.INT.fieldOf("cost").forGetter(c -> c.cost),
        Codec.INT.fieldOf("reward").forGetter(c -> c.reward),
        LostPage.Punishment.CODEC.fieldOf("punishment").forGetter(c -> c.punishment)
    ).apply(i, LostPageComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LostPageComponent> STREAM_CODEC = StreamCodec.composite(
        LostPage.STREAM_CODEC, c -> c.parent,
        ByteBufCodecs.INT, c -> c.cost,
        ByteBufCodecs.INT, c -> c.reward,
        LostPage.Punishment.STREAM_CODEC, c -> c.punishment,
        LostPageComponent::new
    );

    public static LostPageComponent init(ItemStack stack, RandomSource random, LostPage data)
    {
        final LostPageComponent page = stack.get(BeneathComponents.LOST_PAGE.get());
        if (page != null)
            return page;
        final List<LostPage.Punishment> pun = data.punishments();
        return new LostPageComponent(
            data,
            data.costs().get(random.nextInt(data.costs().size())),
            data.rewards().get(random.nextInt(data.rewards().size())),
            pun.isEmpty() ? LostPage.Punishment.NONE : pun.get(random.nextInt(pun.size()))
        );
    }

}
