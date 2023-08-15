package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GhostItemSyncC2SPacket {
    private final ItemStack itemStack;
    private final BlockPos pos;

    public GhostItemSyncC2SPacket(ItemStack itemStack, BlockPos pos) {
        this.itemStack = itemStack;
        this.pos = pos;

    }

    public GhostItemSyncC2SPacket(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
        this.pos = buf.readBlockPos();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeBlockPos(pos);

    }

    public static void handle(GhostItemSyncC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null)
                return;

            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            if(sender.getLevel().getBlockEntity(message.pos) instanceof TradingStationBlockEntity blockEntity) {
                blockEntity.setPreferedItem(message.itemStack);
                //blockEntity.setChanged();

            }
        });
       context.setPacketHandled(true);
    }
}
