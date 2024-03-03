package com.eerussianguy.beneath.common.container;

import java.util.function.Supplier;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import static com.eerussianguy.beneath.Beneath.*;

public final class BeneathContainerTypes
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    public static final RegistryObject<MenuType<HellforgeContainer>> HELLFORGE_CONTAINER = BeneathContainerTypes.<HellforgeBlockEntity, HellforgeContainer>registerBlock("hellforge", BeneathBlockEntities.HELLFORGE, HellforgeContainer::create);

    private static <T extends InventoryBlockEntity<?>, C extends BlockEntityContainer<T>> RegistryObject<MenuType<C>> registerBlock(String name, Supplier<BlockEntityType<T>> type, BlockEntityContainer.Factory<T, C> factory)
    {
        return RegistrationHelpers.registerBlockEntityContainer(CONTAINERS, name, type, factory);
    }

    private static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(String name, IContainerFactory<C> factory)
    {
        return RegistrationHelpers.registerContainer(CONTAINERS, name, factory);
    }

}
