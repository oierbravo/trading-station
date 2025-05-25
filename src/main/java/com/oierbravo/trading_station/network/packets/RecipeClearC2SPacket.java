package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RecipeClearC2SPacket {
    private final BlockPos pos;

    public RecipeClearC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public RecipeClearC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);

    }
    public static void handle(RecipeClearC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null)
                return;

            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            if(sender.serverLevel().getBlockEntity(message.pos) instanceof ITradingStationBlockEntity blockEntity) {
                blockEntity.clearTargetedRecipe();
            }
        });
        context.setPacketHandled(true);
    }
}
