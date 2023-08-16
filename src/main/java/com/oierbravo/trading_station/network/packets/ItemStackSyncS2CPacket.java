package com.oierbravo.trading_station.network.packets;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {
    private final int slot;
    private final ItemStack itemStack;
    private final BlockPos pos;
    public enum SlotType {
        INPUT, OUTPUT, TARGET;
    }
    private final SlotType slotType;

    public ItemStackSyncS2CPacket(int slot, ItemStack itemStack, BlockPos pos,SlotType slotType) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.pos = pos;
        this.slotType =  slotType;
    }

    public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.itemStack = buf.readItem();
        this.pos = buf.readBlockPos();
        this.slotType = SlotType.values()[buf.readInt()];
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeItem(itemStack);
        buf.writeBlockPos(pos);
        buf.writeInt(slotType.ordinal());

    }

    public static boolean handle(ItemStackSyncS2CPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(message.pos) instanceof ITradingStationBlockEntity blockEntity) {
                blockEntity.setItemStack(message.slot,message.itemStack,message.slotType);

            }
        });
        return true;
    }
}
