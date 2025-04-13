package com.oierbravo.trading_station.foundation.gui;

import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TradingRecipeSlot extends SlotItemHandler {
    String tradingRecipeId;
    public TradingRecipeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        //this.tradingRecipeId = tradingRecipe;
    }

    /*@Override
    public @NotNull ItemStack getItem() {
        ItemStack item =  super.getItem();
        if(!tradingRecipeId.isEmpty()){
            CompoundTag tag = item.getOrCreateTag();
            tag.putString("tradingRecipeId", tradingRecipeId);
            item.setTag(tag);
        }
        return item;
    }*/
}
