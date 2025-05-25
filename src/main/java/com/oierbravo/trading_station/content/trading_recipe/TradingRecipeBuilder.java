package com.oierbravo.trading_station.content.trading_recipe;


import com.oierbravo.mechanical_lemon_lib.foundation.recipe.BaseRecipeBuilder;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.IBaseRecipeParams;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.RecipeRequirementType;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class TradingRecipeBuilder extends BaseRecipeBuilder<TradingRecipe, TradingRecipe.TradingRecipeParams> {
    public TradingRecipeBuilder( ResourceLocation id) {
        super(id);
        params = new TradingRecipe.TradingRecipeParams(id);

    }

    @Override
    public TradingRecipe build() {
        return new TradingRecipe(this.params);
    }

    public TradingRecipeBuilder withItemIngredients(Ingredient... itemIngredients) {
        return withItemIngredients(NonNullList.of(Ingredient.EMPTY, itemIngredients));
    }

    public TradingRecipeBuilder withItemIngredients(NonNullList<Ingredient> itemIngredients) {
        params.itemIngredients = itemIngredients;
        return this;
    }

    public TradingRecipeBuilder withSingleItemOutput(ItemStack output) {
        params.result = output;
        return this;
    }
    public TradingRecipeBuilder withSingleItemOutput(ItemLike output) {
        return withSingleItemOutput(new ItemStack(output));
    }

    public TradingRecipeBuilder processingTime(int time) {
        params.processingTime = time;
        return this;
    }
    @FunctionalInterface
    public interface TradingRecipeFactory {
        TradingRecipe create(TradingRecipe.TradingRecipeParams params, List<RecipeRequirementType<?>> enabledRecipeRequeriments);
    }
}