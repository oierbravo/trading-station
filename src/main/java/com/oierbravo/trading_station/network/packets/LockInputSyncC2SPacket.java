package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LockInputSyncC2SPacket {
    private final boolean lock;
    private final BlockPos pos;

    public LockInputSyncC2SPacket(boolean lock, BlockPos pos) {
        this.lock = lock;
        this.pos = pos;

    }

    public LockInputSyncC2SPacket(FriendlyByteBuf buf) {
        this.lock = buf.readBoolean();
        this.pos = buf.readBlockPos();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(lock);
        buf.writeBlockPos(pos);

    }

    public static void handle(LockInputSyncC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null)
                return;

            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            if(sender.serverLevel().getBlockEntity(message.pos) instanceof ITradingStationBlockEntity blockEntity) {
                blockEntity.setInputLock(message.lock);
            }
        });
       context.setPacketHandled(true);
    }
}
