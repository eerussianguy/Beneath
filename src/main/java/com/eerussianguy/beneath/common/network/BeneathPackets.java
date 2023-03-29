package com.eerussianguy.beneath.common.network;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.mutable.MutableInt;

import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.network.PacketHandler;
import net.dries007.tfc.util.DataManager;

public final class BeneathPackets
{
    private static final String VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Beneath.identifier("network"), () -> VERSION, VERSION::equals, VERSION::equals);
    private static final MutableInt ID = new MutableInt(0);

    public static void send(PacketDistributor.PacketTarget target, Object message)
    {
        CHANNEL.send(target, message);
    }

    public static void init()
    {
        registerDataManager(NetherFertilizer.Packet.class, NetherFertilizer.MANAGER);
    }

    private static <T extends DataManagerSyncPacket<E>, E> void registerDataManager(Class<T> cls, DataManager<E> manager)
    {
        PacketHandler.registerDataManager(cls, manager, CHANNEL, ID.getAndIncrement());
    }

}
