package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.network.packets.ItemStackSyncS2CPacket;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public interface ITradingStationBlockEntity {
    public int progress = 0;
    public int maxProgress = 1;
    LazyOptional<IItemHandler> getInputItemHandler();
    LazyOptional<IItemHandler> getOutputItemHandler();

    IItemHandler getTargetItemHandler();


    ItemStack getTargetItemStack();

    Level getLevel();

    BlockPos getBlockPos();

    void setPreferedItem(ItemStack itemStack);

    void setItemStack(int slot, ItemStack itemStack, ItemStackSyncS2CPacket.SlotType slotType);


    IEnergyStorage getEnergyStorage();
    byte currentRedstoneMode = 0;
    default byte getRedstoneMode(){
        return currentRedstoneMode;
    };

    String getTraderType();

    enum REDSTONE_MODES {
        IGNORE,
        LOW,
        HIGH
    }




    boolean canCraftItem();
    boolean canProcess(ItemStack stack);
    void resetProgress();

    default int getProcessingTime(){
        return getRecipe().map(TradingRecipe::getProcessingTime).orElse(1);
    }
    default int getProgressPercent() {
        return progress * 100 / maxProgress;
    }
    default Optional<TradingRecipe> getRecipe(){
        Level level = this.getLevel();
        SimpleContainer inputInventory = getInputInventory();
        if(!getTargetItemHandler().getStackInSlot(0).isEmpty())
            return ModRecipes.findByOutput(level,getTargetItemHandler().getStackInSlot(0));
        return ModRecipes.find(inputInventory,level, getBiome());
    };
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
    default void craftItem() {
        SimpleContainer inputInventory = getInputInventory();

        Optional<TradingRecipe> recipe = getRecipe();

        if(recipe.isPresent()){
            for (int i = 0; i < recipe.get().getIngredients().size(); i++) {
                Ingredient ingredient = recipe.get().getIngredients().get(i);

                for (int slot = 0; slot < getInputItems().getSlots(); slot++) {
                    ItemStack itemStack = getInputItems().getStackInSlot(slot);
                    if(ingredient.test(itemStack)){
                        getInputItems().extractItem(slot,ingredient.getItems()[0].getCount(),false);
                        inputInventory.setChanged();
                    }
                }
            }
            getOutputItems().insertItem(0, recipe.get().getResultItem(), false);
        }

        this.resetProgress();
    }
    ItemStackHandler getInputItems();
    ItemStackHandler getOutputItems();

    void setRedstoneMode(byte mode);
    byte getCurrentRedstoneMode();
    boolean isPowered();

    default Biome getBiome(){
        return this.getLevel().getBiome(getBlockPos()).get();
    }
}
