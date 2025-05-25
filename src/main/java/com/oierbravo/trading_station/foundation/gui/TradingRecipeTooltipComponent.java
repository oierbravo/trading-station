package com.oierbravo.trading_station.foundation.gui;

import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class TradingRecipeTooltipComponent implements  TooltipComponent{

    TradingRecipe tradingRecipe;
    public TradingRecipeTooltipComponent(TradingRecipe pTradingRecipe) {
        this.tradingRecipe = pTradingRecipe;
    }
}
