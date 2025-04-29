package com.oierbravo.trading_station.foundation.gui;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class TradingRecipeSlot extends SlotItemHandler {
    String tradingRecipeId;
    public TradingRecipeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

}
