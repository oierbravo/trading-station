package com.oierbravo.trading_station.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.BiomeCondition;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.registrate.TradingStationRegistrate;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class TradingRecipeCategory implements IRecipeCategory<TradingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(TradingStation.MODID, "trading");
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    private final IDrawable background;
    private final IDrawable icon;

    public TradingRecipeCategory(IGuiHelper helper) {
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        this.background = new IDrawable() {
            @Override
            public int getWidth() {
                return 176;
            }

            @Override
            public int getHeight() {
                return 45;
            }

            @Override
            public void draw(PoseStack poseStack, int xOffset, int yOffset) {

            }
        };
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TradingStationRegistrate.BLOCK.get()));

    }

    protected IDrawableAnimated getArrow() {
        return this.cachedArrows.getUnchecked(50);
    }
    @Override
    public RecipeType<TradingRecipe> getRecipeType() {
        return RecipeType.create("trading_station","trading", TradingRecipe.class);
    }

    @Override
    public Component getTitle() {
        return ModLang.translate("trading.recipe");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
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
            builder.addSlot(RecipeIngredientRole.INPUT, 41 + index * 18, 11)
                    .addIngredients(ingredients.get(index))
                    .setSlotName("input_" + index);
        //.addItemStacks(Arrays.asList(ingredients.get(0).getItems()));

        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 11)
                .addItemStack(recipe.getResultItem())
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    if(recipe.getBiomeCondition() != BiomeCondition.EMPTY)
                        tooltip.add(recipe.getBiomeCondition().toComponent());
                })
                .setSlotName("output");
    }

    @Override
    public void draw(TradingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
        IDrawableAnimated arrow = getArrow();
        arrow.draw(stack, 75, 14);
        //drawProcessingTime(recipe, stack, 81,4);
        drawBiome(recipe, stack, 1,34);


    }
    protected void drawProcessingTime(TradingRecipe recipe, PoseStack poseStack, int x, int y) {
        int processingTime = recipe.getProcessingTime();
        if (processingTime > 0) {
            int cookTimeSeconds = processingTime / 20;
            MutableComponent timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            fontRenderer.draw(poseStack, timeString, x, y, 0xFF808080);
        }
    }
    protected void drawBiome(TradingRecipe recipe, PoseStack poseStack, int x, int y) {
        if(recipe.getBiomeCondition() == BiomeCondition.EMPTY)
            return;
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        fontRenderer.draw(poseStack, recipe.getBiomeCondition().toComponent(), x, y, 0xFF808080);
    }
}
