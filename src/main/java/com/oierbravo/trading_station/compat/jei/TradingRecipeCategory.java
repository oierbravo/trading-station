package com.oierbravo.trading_station.compat.jei;

import com.oierbravo.mechanical_lemon_ui.foundation.utility.Color;
import com.oierbravo.mechanical_lemon_ui.foundation.utility.Components;
import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirement;
import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirementType;
import com.oierbravo.mechanicals.utility.LibLang;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.registrate.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.oierbravo.trading_station.compat.jei.JEIPlugin.TRAING_RECIPE;

public class TradingRecipeCategory implements IRecipeCategory<TradingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(TradingStation.MODID, "trading");
    //private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final IDrawable background;

    private final IDrawable icon;

    public TradingRecipeCategory(IGuiHelper helper) {
        this.background = new IDrawable() {
            @Override
            public int getWidth() {
                return 176;
            }

            @Override
            public int getHeight() {
                return 51;
            }

            @Override
            public void draw(GuiGraphics graphics, int xOffset, int yOffset) {

            }
        };
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TRADING_STATION.get()));

    }

    @Override
    public RecipeType<TradingRecipe> getRecipeType() {
        return TRAING_RECIPE;
    }

    @Override
    public Component getTitle() {
        return ModLang.translate("trading.recipe").component();
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 51;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull TradingRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for(int index = 0; index < ingredients.size(); index++) {
            Ingredient ing = ingredients.get(index);
            builder.addSlot(RecipeIngredientRole.INPUT, 41 + index * 18, 2)
                    .addIngredients(ingredients.get(index))
                    .setSlotName("input_" + index);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 2)
                .addItemStack(recipe.getResult())
                .setSlotName("output");
    }

    @Override
    public void draw(TradingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);
        drawRequirements(graphics, recipe,2, 25);
        drawProcessingTime(recipe, graphics, 81,4);


    }

    private void drawRequirements( GuiGraphics pGuiGraphics,TradingRecipe recipe, int pX, int pY) {

        MutableComponent recipeRequirementComponent = Components.empty();
        Map<RecipeRequirementType<?>, RecipeRequirement> recipeRequirements = recipe.getRecipeRequirements();
        recipeRequirements.forEach((recipeRequirementType, recipeRequirement) -> {
            if(recipeRequirement.isPresent())
                recipeRequirementComponent.append(recipeRequirement.toRequirementComponent());

        });
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        if(recipeRequirements.isEmpty())
          pGuiGraphics.drawString(fontRenderer, LibLang.translate("ui.recipe_requirement.none.tooltip").component(),pX, pY, Color.BLACK.getRGB(), false);
        int yOffset = 10;
        for(int count = 0; count < recipeRequirementComponent.getSiblings().size(); count++){
            pGuiGraphics.drawString(fontRenderer, recipeRequirementComponent.getSiblings().get(count),pX, pY + count * yOffset, Color.BLACK.getRGB(), false);
        }
    }

    protected void drawProcessingTime(TradingRecipe recipe, GuiGraphics graphics, int x, int y) {
        int processingTime = recipe.getProcessingTime();
        if (processingTime > 0) {
            int cookTimeSeconds = processingTime / 20;
            MutableComponent timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            graphics.drawString(fontRenderer, timeString, x, y, 0xFF808080,false);

        }
    }

}
