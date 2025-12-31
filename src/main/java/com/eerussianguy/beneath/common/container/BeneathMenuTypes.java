package com.eerussianguy.beneath.common.container;

import java.util.function.Supplier;
import com.eerussianguy.beneath.common.blockentities.BeneathBlockEntities;
import com.eerussianguy.beneath.common.blockentities.HellforgeBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.ItemStackContainer;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.common.container.TFCContainerTypes.Id;

import static com.eerussianguy.beneath.Beneath.*;

public final class BeneathMenuTypes
{
    public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(Registries.MENU, MOD_ID);

    public static final Id<HellforgeContainer> HELLFORGE = BeneathMenuTypes.<HellforgeBlockEntity, HellforgeContainer>registerBlock("hellforge", BeneathBlockEntities.HELLFORGE, HellforgeContainer::create);
    public static final Id<JuicerContainer> JUICER = registerItem("juicer", JuicerContainer::create);

    private static <T extends InventoryBlockEntity<?>, C extends BlockEntityContainer<T>> Id<C> registerBlock(String name, Supplier<BlockEntityType<T>> type, BlockEntityContainer.Factory<T, C> factory)
    {
        return new Id<>(RegistrationHelpers.registerBlockEntityContainer(MENU, name, type, factory));
    }

    private static <C extends ItemStackContainer> Id<C> registerItem(String name, ItemStackContainer.Factory<C> factory)
    {
        return new Id<>(RegistrationHelpers.registerItemStackContainer(MENU, name, factory));
    }

    private static <C extends AbstractContainerMenu> Id<C> register(String name, IContainerFactory<C> factory)
    {
        return new Id<>(RegistrationHelpers.registerContainer(MENU, name, factory));
    }

}
