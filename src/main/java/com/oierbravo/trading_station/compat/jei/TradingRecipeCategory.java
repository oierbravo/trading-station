package com.oierbravo.trading_station.compat.jei;

import com.oierbravo.mechanicals.compat.jei.RecipeRequirementRenderer;
import com.oierbravo.mechanicals.foundation.gui.MechanicalGUITextures;
import com.oierbravo.mechanicals.foundation.ingredient.CountableIngredient;
import com.oierbravo.trading_station.ModLang;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.oierbravo.trading_station.compat.jei.TradingStationJEIPlugin.TRADING_RECIPE;

public class TradingRecipeCategory implements IRecipeCategory<TradingRecipe> {
    //private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final IDrawable background;

    private final IDrawable icon;

    public @Nullable ResourceLocation getRegistryName(TradingRecipe recipe) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter(recipeHolder -> recipeHolder.value().equals(recipe))
                .map(RecipeHolder::id)
                .findFirst()
                .orElse(null);
    }


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
        return TRADING_RECIPE;
    }

    @Override
    public Component getTitle() {
        return ModLang.translate("trading.recipe").component();
    }

    @Override
    public int getWidth() {
        return 180;
    }

    @Override
    public int getHeight() {
        return 70;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull TradingRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        NonNullList<CountableIngredient> ingredients = recipe.getCountableIngredients();
        for(int index = 0; index < ingredients.size(); index++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 0 + index * 18, 0)
                    .addItemStack(ingredients.get(index).asItemStack())
                    .setStandardSlotBackground()
                    .setSlotName("input_" + index);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 35, 23)
                .addItemStack(recipe.getResult())
                .setStandardSlotBackground()
                .setSlotName("output");
    }

    @Override
    public void draw(TradingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);
        drawRequirements(graphics, recipe,60, 2);
        drawProcessingTime(recipe, graphics, 16,38);
        MechanicalGUITextures.JEI_DOWN_RIGHT_ARROW.render(graphics, 16, 23);

    }

    private void drawRequirements( GuiGraphics pGuiGraphics,TradingRecipe recipe, int pX, int pY) {
        RecipeRequirementRenderer.drawRequirements(recipe,pGuiGraphics, pX,pY);
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
