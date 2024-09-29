package com.oierbravo.trading_station.foundation.gui;

import com.oierbravo.mechanical_lemon_ui.foundation.utility.FakeItemRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.crafting.Ingredient;

public record TradingRecipeTooltipRenderer(TradingRecipeTooltipComponent tradingRecipeTooltipComponent) implements  ClientTooltipComponent{
    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public int getWidth(Font pFont) {
        return 100;
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        for(int index = 0; index < tradingRecipeTooltipComponent.tradingRecipe.getIngredients().size(); index++){
            Ingredient ingredient =  tradingRecipeTooltipComponent.tradingRecipe.getIngredients().get(index);
            if(!ingredient.isEmpty()) {
                FakeItemRenderer.renderFakeItem(pGuiGraphics,ingredient.getItems()[0], pX + index * 20, pY, false,true);
            }
        }
    }
}
