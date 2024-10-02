package com.eerussianguy.beneath.common.blocks;

import java.util.ArrayList;
import java.util.List;
import com.eerussianguy.beneath.common.blockentities.UnposterBlockEntity;
import com.eerussianguy.beneath.common.items.BeneathItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.devices.BottomSupportedDeviceBlock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.ICalendar;

public class UnposterBlock extends BottomSupportedDeviceBlock implements EntityBlockExtension
{
    public static final IntegerProperty STAGE = TFCBlockStateProperties.STAGE_8;

    public UnposterBlock(ExtendedProperties properties)
    {
        super(properties, InventoryRemoveBehavior.NOOP);
        registerDefaultState(this.defaultBlockState().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(STAGE));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (state.getValue(STAGE) > 0 && level.getBlockEntity(pos) instanceof UnposterBlockEntity poster)
        {
            final long ticksSinceUpdate = poster.getTicksSinceUpdate();
            int cyclesLeft = Math.min(state.getValue(STAGE), (int) (ticksSinceUpdate % ICalendar.TICKS_IN_DAY));
            int cyclesUsed = 0;
            if (cyclesLeft > 0)
            {
                final List<BlockState> shrooms = new ArrayList<>();
                for (BlockPos testPos : BlockPos.withinManhattan(pos, 5, 1, 5))
                {
                    final BlockState stateAt = level.getBlockState(testPos);
                    if (Helpers.isBlock(stateAt, BeneathBlockTags.MUSHROOMS))
                    {
                        shrooms.add(stateAt);
                    }
                }
                if (!shrooms.isEmpty())
                {
                    for (BlockPos testPos : BlockPos.withinManhattan(pos, 5, 1, 5))
                    {
                        if (shrooms.isEmpty() || cyclesLeft <= 0)
                            break;
                        final BlockState stateAt = level.getBlockState(testPos);
                        if (stateAt.isAir())
                        {
                            final BlockState shroomState = shrooms.get(0);
                            if (shroomState.canSurvive(level, testPos))
                            {
                                shrooms.remove(0);
                                cyclesLeft--;
                                cyclesUsed++;
                                level.setBlockAndUpdate(testPos, shroomState);
                            }
                        }
                    }
                }

                if (cyclesUsed > 0)
                {
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, Math.max(0, state.getValue(STAGE) - cyclesUsed)));
                }
                poster.resetCounter();
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (level.getBlockEntity(pos) instanceof UnposterBlockEntity poster)
        {
            poster.resetCounter();
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        final int stage = state.getValue(STAGE);
        final ItemStack held = player.getItemInHand(hand);

        if (stage < 8 && Helpers.isItem(held, BeneathItemTags.UNPOSTABLE))
        {
            level.setBlockAndUpdate(pos, state.setValue(STAGE, Math.min(8, stage + Mth.nextInt(level.random, 1, 2))));
            held.shrink(1);
            Helpers.playSound(level, pos, SoundEvents.NYLIUM_PLACE);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
