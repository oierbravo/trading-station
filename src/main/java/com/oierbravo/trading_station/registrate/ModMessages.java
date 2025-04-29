package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.network.packets.data.*;
import com.oierbravo.trading_station.network.packets.handler.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModMessages {
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ModConstants.MODID);

        //Going to Client
        registrar.playToClient(ItemSyncPayload.TYPE, ItemSyncPayload.STREAM_CODEC, ItemSyncPacket.get()::handle);

        //Going to server
        registrar.playToServer(LockInputSyncPayload.TYPE, LockInputSyncPayload.STREAM_CODEC, LockInputSyncPacket.get()::handle);
        registrar.playToServer(RecipeClearSyncPayload.TYPE, RecipeClearSyncPayload.STREAM_CODEC, RecipeClearSyncPacket.get()::handle);
        registrar.playToServer(RecipeSelectSyncPayload.TYPE, RecipeSelectSyncPayload.STREAM_CODEC, RecipeSelectSyncPacket.get()::handle);
        registrar.playToServer(RedstoneModeSyncPayload.TYPE, RedstoneModeSyncPayload.STREAM_CODEC, RedstoneModeSyncPacket.get()::handle);

    }
    public static void sendToAllClients(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers(message);
    }
    public static void sendToServer(CustomPacketPayload message){
        PacketDistributor.sendToServer(message);
    }
    public static void register() {}
}
