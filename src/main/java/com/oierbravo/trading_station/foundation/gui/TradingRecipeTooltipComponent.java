package com.oierbravo.trading_station.foundation.gui;

import com.oierbravo.mechanicals.foundation.ingredient.CountableIngredient;
import com.oierbravo.mechanicals_ui.foundation.utility.FakeItemRenderer;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.atomic.AtomicInteger;

public record TradingRecipeTooltipComponent(TradingRecipe tradingRecipe) implements  TooltipComponent, ClientTooltipComponent {
    private static int LINE_HEIGHT = 10;
    @Override
    public int getHeight() {
        return 20 + tradingRecipe.getRecipeRequirements().size() * LINE_HEIGHT;
    }

    @Override
    public int getWidth(Font font) {
        return 190;
    }
    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        for (int index = 0; index < tradingRecipe.getCountableIngredients().size(); index++) {
            CountableIngredient ingredient = tradingRecipe.getCountableIngredients().get(index);
            if (!ingredient.ingredient().isEmpty()) {
                FakeItemRenderer.renderFakeItem(pGuiGraphics, new ItemStack(ingredient.ingredient().getItems()[0].getItem(), ingredient.count()), pX + index * 20, pY, false, true);
            }
        }
        Font font = Minecraft.getInstance().font;
        AtomicInteger yOffset = new AtomicInteger(18);
        tradingRecipe.getRecipeRequirements().forEach((recipeRequirement) -> {
            //if(recipeRequirementType != MachineRequirement.TYPE){
                pGuiGraphics.drawString(font,recipeRequirement.toItemTooltipComponent()
                        ,pX,pY + yOffset.get(), Color.WHITE.getRGB(),false);
                yOffset.addAndGet(LINE_HEIGHT);
            //}
        });
    }
}
