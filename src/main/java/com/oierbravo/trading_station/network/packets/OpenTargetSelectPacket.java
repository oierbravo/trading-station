package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenTargetSelectPacket {
    private BlockPos sourcePos;

    public OpenTargetSelectPacket(BlockPos pSourcePos){
        this.sourcePos = pSourcePos;
    }
    public OpenTargetSelectPacket(FriendlyByteBuf buf){
        this(buf.readBlockPos());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(sourcePos);
    }
    public static boolean handle(OpenTargetSelectPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null)
                return;

            TradingStationMenu container = (TradingStationMenu) sender.containerMenu;
            if (container == null)
                return;

            /*NetworkHooks.openScreen(sender, new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> new TradingTradeSelectMenu(windowId, message.sourcePos), Component.translatable("")), (buf -> {
                buf.writeBlockPos(message.sourcePos);
            }));*/
            NetworkHooks.openScreen(((ServerPlayer)sender), (TradingStationBlockEntity)container.blockEntity, message.sourcePos);
        });
        return true;
    }

}
