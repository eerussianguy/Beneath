package com.eerussianguy.beneath.common.blockentities;

import java.util.function.Supplier;
import java.util.stream.Stream;
import com.eerussianguy.beneath.common.blocks.BeneathBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.blockentities.TFCBlockEntities.Id;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import static com.eerussianguy.beneath.Beneath.*;

public class BeneathBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static final Id<SoulFarmlandBlockEntity> SOUL_FARMLAND = register("soul_farmland", SoulFarmlandBlockEntity::new, BeneathBlocks.SOUL_FARMLAND);
    public static final Id<NetherCropBlockEntity> NETHER_CROP = register("nether_crop", NetherCropBlockEntity::new, BeneathBlocks.CROPS.values().stream());
    public static final Id<HellforgeBlockEntity> HELLFORGE = register("hellforge", HellforgeBlockEntity::new, BeneathBlocks.HELLFORGE);
    public static final Id<BeneathSignBlockEntity> SIGN = register("sign", BeneathSignBlockEntity::new, BeneathBlocks.WOODS.values().stream().flatMap(map -> Stream.of(Wood.BlockType.SIGN, Wood.BlockType.WALL_SIGN).map(map::get)));
    public static final Id<BeneathHangingSignBlockEntity> HANGING_SIGN = register("hanging_sign", BeneathHangingSignBlockEntity::new, Stream.of(
        BeneathBlocks.CEILING_HANGING_SIGNS, BeneathBlocks.WALL_HANGING_SIGNS
    ).flatMap(woodMap -> woodMap.values().stream().flatMap(metalMap -> metalMap.values().stream())));
    public static final Id<UnposterBlockEntity> UNPOSTER = register("unposter", UnposterBlockEntity::new, BeneathBlocks.UNPOSTER);
    public static final Id<AncientAltarBlockEntity> ANCIENT_ALTAR = register("ancient_altar", AncientAltarBlockEntity::new, BeneathBlocks.ANCIENT_ALTAR);

    private static <T extends BlockEntity> Id<T> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block)
    {
        return new Id<>(RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block));
    }

    private static <T extends BlockEntity> Id<T> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        return new Id<>(RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks));
    }


}
