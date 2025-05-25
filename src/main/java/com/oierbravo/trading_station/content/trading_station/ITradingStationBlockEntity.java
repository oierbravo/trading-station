package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.network.packets.ItemStackSyncS2CPacket;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public interface ITradingStationBlockEntity {
    LazyOptional<IItemHandler> getInputItemHandler();
    LazyOptional<IItemHandler> getOutputItemHandler();

    IItemHandler getTargetItemHandler();

    void setItemStack(int slot, ItemStack itemStack, ItemStackSyncS2CPacket.SlotType slotType);

    LazyOptional<IEnergyStorage> getEnergyStorageHandler();

    byte getRedstoneMode();

    void setInputLock(boolean lock);
    boolean isLocked();

    String getTargetedRecipeId();
    void setTargetedRecipeById(ResourceLocation recipeId);

    Optional<TradingRecipe> getRecipe();

    void readClient(CompoundTag readNbt);

    BlockPos getBlockPos();
    Level getLevel();

    enum REDSTONE_MODES {
        IGNORE,
        LOW,
        HIGH
    }

    void setWorking(boolean value);
    boolean isWorking();

    boolean canCraftItem();
    boolean canProcess(ItemStack stack);

    void resetProgress();
    int getProcessingTime();
    int getProgressPercent();
    int getProgress();
    int getMaxProgress();

    default SimpleContainer getInputInventory(){
        int containerSize = 0;
        for(int index = 0; index < getInputItems().getSlots(); index++) {
            if (!getInputItems().getStackInSlot(index).isEmpty())
                containerSize++;
        }

        SimpleContainer inputInventory = new SimpleContainer(containerSize);
        getInputItemHandler().ifPresent(iItemHandler -> {
            for(int slot = 0; slot < iItemHandler.getSlots(); slot++) {
                if(!iItemHandler.getStackInSlot(slot).isEmpty()){
                    inputInventory.addItem(iItemHandler.getStackInSlot(slot));
                }
            }
        });
        return inputInventory;
    }
    void craftItem();
    ItemStackHandler getInputItems();
    ItemStackHandler getOutputItems();

    void setRedstoneMode(byte mode);
    byte getCurrentRedstoneMode();
    boolean isPowered();
    void sendToMenu(FriendlyByteBuf buffer);
    void clearTargetedRecipe();
}
