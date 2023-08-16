package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.network.packets.ItemStackSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

public interface ITradingStationBlockEntity {
    public int progress = 0;
    public int maxProgress = 1;
    LazyOptional<IItemHandler> getInputItemHandler();
    LazyOptional<IItemHandler> getOutputItemHandler();

    IItemHandler getTargetItemHandler();

    BlockPos getBlockPos();

    ItemStack getTargetItemStack();

    Level getLevel();

    void setPreferedItem(ItemStack itemStack);

    void setItemStack(int slot, ItemStack itemStack, ItemStackSyncS2CPacket.SlotType slotType);


    IEnergyStorage getEnergyStorage();
}
