package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RedstoneModeSyncC2SPacket {
    private final byte mode;
    private final BlockPos pos;

    public RedstoneModeSyncC2SPacket(byte mode, BlockPos pos) {
        this.mode = mode;
        this.pos = pos;

    }

    public RedstoneModeSyncC2SPacket(FriendlyByteBuf buf) {
        this.mode = buf.readByte();
        this.pos = buf.readBlockPos();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(mode);
        buf.writeBlockPos(pos);

    }

    public static void handle(RedstoneModeSyncC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null)
                return;

            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            if(sender.serverLevel().getBlockEntity(message.pos) instanceof ITradingStationBlockEntity blockEntity) {
                blockEntity.setRedstoneMode(message.mode);
            }
        });
       context.setPacketHandled(true);
    }
}
