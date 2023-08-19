package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RecipeSelectC2SPacket {

    private final ResourceLocation recipeId;
    private final BlockPos pos;


    public RecipeSelectC2SPacket(ResourceLocation recipeId, BlockPos pos) {
        this.recipeId = recipeId;
        this.pos = pos;
    }

    public RecipeSelectC2SPacket(FriendlyByteBuf buf) {
        this.recipeId = buf.readResourceLocation();
        this.pos = buf.readBlockPos();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(recipeId);
        buf.writeBlockPos(pos);

    }
    public static void handle(RecipeSelectC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null)
                return;

            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            if(sender.getLevel().getBlockEntity(message.pos) instanceof ITradingStationBlockEntity blockEntity) {
                blockEntity.setTargetedRecipeById(message.recipeId);
                //blockEntity.setChanged();

            }
        });
        context.setPacketHandled(true);
    }
}
