package com.oierbravo.trading_station.foundation.gui;

import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirement;
import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirementType;
import com.oierbravo.mechanical_lemon_ui.foundation.utility.Color;
import com.oierbravo.mechanical_lemon_ui.foundation.utility.FakeItemRenderer;
import com.oierbravo.trading_station.content.trading_recipe.MachineRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public record TradingRecipeTooltipRenderer(TradingRecipeTooltipComponent tradingRecipeTooltipComponent) implements  ClientTooltipComponent{
    private Map<RecipeRequirementType<?>, RecipeRequirement>  getRecipeRequirements(){
        return tradingRecipeTooltipComponent.tradingRecipe.getRecipeRequirements();
    }

    @Override
    public int getHeight() {

        return 22 + getRecipeRequirements().size() * 10;
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
        Font font = Minecraft.getInstance().font;
        AtomicInteger yOffset = new AtomicInteger(18);
        getRecipeRequirements().forEach((recipeRequirementType, recipeRequirement) -> {
            if(recipeRequirementType != MachineRequirement.TYPE){
                pGuiGraphics.drawString(font,recipeRequirement.toRequirementComponent()
                        ,pX,pY + yOffset.get(), Color.WHITE.getRGB(),false);
                yOffset.addAndGet(10);
            }
        });
    }
}
