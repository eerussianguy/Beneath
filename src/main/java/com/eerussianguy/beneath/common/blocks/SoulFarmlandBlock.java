package com.eerussianguy.beneath.common.blocks;

import java.util.List;
import java.util.function.Supplier;
import com.eerussianguy.beneath.common.blockentities.SoulFarmlandBlockEntity;
import com.eerussianguy.beneath.misc.NCropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.IForgeBlockExtension;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.common.blocks.soil.HoeOverlayBlock;
import net.dries007.tfc.common.blocks.soil.ISoilBlock;

public class SoulFarmlandBlock extends Block implements ISoilBlock, HoeOverlayBlock, IForgeBlockExtension, EntityBlockExtension
{
    private final ExtendedProperties properties;
    private final Supplier<? extends Block> dirt;

    public SoulFarmlandBlock(ExtendedProperties properties, Supplier<? extends Block> dirt)
    {
        super(properties.properties());
        this.properties = properties;
        this.dirt = dirt;
    }

    @Override
    public ExtendedProperties getExtendedProperties()
    {
        return properties;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return NCropUtil.useFertilizer(level, player, hand, pos) ? InteractionResult.SUCCESS : super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void addHoeOverlayInfo(Level level, BlockPos pos, BlockState state, List<Component> tooltip, boolean debug)
    {
        if (level.getBlockEntity(pos) instanceof SoulFarmlandBlockEntity farmland)
        {
            farmland.addTooltipInfo(tooltip);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
    {
        if (!state.canSurvive(level, pos))
        {
            FarmlandBlock.turnToDirt(state, level, pos);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        final BlockState defaultState = defaultBlockState();
        return defaultState.canSurvive(context.getLevel(), context.getClickedPos()) ? defaultState : getDirt();
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing == Direction.UP && !state.canSurvive(level, currentPos))
        {
            level.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return FarmlandBlock.SHAPE;
    }

    @Override
    public BlockState getDirt()
    {
        return dirt.get().defaultBlockState();
    }
}
