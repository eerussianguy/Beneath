package com.eerussianguy.beneath.common.blocks;

import java.util.Random;
import java.util.function.Predicate;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.CharcoalForgeBlock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.MultiBlock;

public class HellforgeBlock extends CharcoalForgeBlock
{
    public static boolean hellForgeValid(LevelAccessor level, BlockPos pos)
    {
        return HELLFORGE_MULTIBLOCK.test(level, pos);
    }

    public static final MultiBlock HELLFORGE_MULTIBLOCK = makeMultiblock(s -> s.getBlock() instanceof HellforgeBlock, s -> s.getBlock() instanceof HellforgeSideBlock);
    public static final MultiBlock PRE_HELLFORGE_MULTIBLOCK = makeMultiblock(s -> s.getBlock() instanceof CursecoalPileBlock && s.getValue(CursecoalPileBlock.LAYERS) >= 7, s -> s.getBlock() instanceof CursecoalPileBlock && s.getValue(CursecoalPileBlock.LAYERS) >= 7);

    private static MultiBlock makeMultiblock(Predicate<BlockState> centerTest, Predicate<BlockState> sideTest)
    {
        final MultiBlock mb = new MultiBlock();
        for (int x = -2; x <= 2; x++)
        {
            for (int z = -2; z <= 2; z++)
            {
                if (!(Mth.abs(x) == 2 && Mth.abs(z) == 2)) // dont check corners
                {
                    if (Mth.abs(x) != 2 && Mth.abs(z) != 2)
                    {
                        // the interior of the forge
                        if (x == 0 && z == 0)
                        {
                            mb.match(new BlockPos(x, 0, z), centerTest);
                        }
                        else
                        {
                            mb.match(new BlockPos(x, 0, z), sideTest);
                        }
                        mb.match(new BlockPos(x, 1, z), s -> s.isAir() || Helpers.isBlock(s, TFCTags.Blocks.FORGE_INVISIBLE_WHITELIST));
                        mb.match(new BlockPos(x, -1, z), BeneathBlockTags.HELLFORGE_INSULATION);
                    }
                    else
                    {
                        mb.match(new BlockPos(x, 0, z), BeneathBlockTags.HELLFORGE_INSULATION);
                    }
                }
                else
                {
                    mb.match(new BlockPos(x, 0, z), s -> s.getBlock() instanceof LavaAqueductBlock && s.getFluidState().is(FluidTags.LAVA));
                }

            }
        }
        return mb;
    }

    public HellforgeBlock(ExtendedProperties properties)
    {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand)
    {
        if (hellForgeValid(level, pos))
        {
            Helpers.fireSpreaderTick(level, pos, rand, 3);
        }
        else
        {
            level.setBlockAndUpdate(pos, this.defaultBlockState().setValue(HEAT, 0));
        }
    }

    @Override
    public void intakeAir(Level level, BlockPos pos, BlockState state, int amount)
    {
        if (level.getBlockEntity(pos) instanceof HellforgeBlockEntity forge)
        {
            forge.intakeAir(amount);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state)
    {
        return state.getValue(HEAT) > 0;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        level.scheduleTick(currentPos, state.getBlock(), 1);
        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random)
    {
        if (!hellForgeValid(level, pos))
        {
            level.setBlockAndUpdate(pos, state.setValue(HEAT, 0));
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if (level.getBlockEntity(pos) instanceof HellforgeBlockEntity forge)
        {
            if (player instanceof ServerPlayer serverPlayer)
            {
                Helpers.openScreen(serverPlayer, forge, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
