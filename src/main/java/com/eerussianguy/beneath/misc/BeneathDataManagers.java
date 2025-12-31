package com.eerussianguy.beneath.misc;

import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.DataManagers;

public final class BeneathDataManagers
{
    public static final DeferredRegister<DataManager<?>> MANAGERS = DeferredRegister.create(DataManagers.KEY, TerraFirmaCraft.MOD_ID);

    static
    {
        register(NetherFertilizer.MANAGER);
        register(LostPage.MANAGER);
    }

    private static void register(DataManager<?> manager)
    {
        MANAGERS.register(manager.getName(), () -> manager);
    }

}
