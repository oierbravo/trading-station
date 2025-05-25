package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(TradingStation.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;


        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();


        net.messageBuilder(RedstoneModeSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RedstoneModeSyncC2SPacket::new)
                .encoder(RedstoneModeSyncC2SPacket::toBytes)
                .consumerMainThread(RedstoneModeSyncC2SPacket::handle)
                .add();

        net.messageBuilder(LockInputSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LockInputSyncC2SPacket::new)
                .encoder(LockInputSyncC2SPacket::toBytes)
                .consumerMainThread(LockInputSyncC2SPacket::handle)
                .add();


        net.messageBuilder(RecipeSelectC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RecipeSelectC2SPacket::new)
                .encoder(RecipeSelectC2SPacket::toBytes)
                .consumerMainThread(RecipeSelectC2SPacket::handle)
                .add();
        net.messageBuilder(RecipeClearC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RecipeClearC2SPacket::new)
                .encoder(RecipeClearC2SPacket::toBytes)
                .consumerMainThread(RecipeClearC2SPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
