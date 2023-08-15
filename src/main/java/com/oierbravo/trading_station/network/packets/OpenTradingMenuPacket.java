package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import dev.latvian.mods.rhino.ast.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenTradingMenuPacket {
    protected BlockPos sourcePos;

    public OpenTradingMenuPacket(BlockPos pSourcePos){
        this.sourcePos = pSourcePos;
    }
    public OpenTradingMenuPacket(FriendlyByteBuf buf){
        this(buf.readBlockPos());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(sourcePos);
    }

    public BlockPos getSourcePos(){
        return sourcePos;
    }
    public static boolean handle(OpenTradingMenuPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null)
                return;

            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            NetworkHooks.openScreen(sender, new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> new TradingStationMenu(windowId,playerInventory,message.sourcePos), Component.translatable("trading_station.block.display")), (buf -> {
                buf.writeBlockPos(message.sourcePos);
            }));
        });
        return true;
    }

}
