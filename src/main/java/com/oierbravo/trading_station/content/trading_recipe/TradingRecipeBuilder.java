package com.oierbravo.trading_station.content.trading_recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;

public class TradingRecipeBuilder {
    protected TradingRecipeParams params;
    protected List<ICondition> recipeConditions;
    public TradingRecipeBuilder(ResourceLocation recipeId) {
        params = new TradingRecipeParams(recipeId);
        recipeConditions = new ArrayList<>();


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

    public TradingRecipeBuilder processingTime(int time) {
        params.processingTime = time;
        return this;
    }
    public TradingRecipeBuilder exclusiveTo(NonNullList<String> exclusiveTo) {
        return exclusiveTo(ExclusiveToCondition.fromList(exclusiveTo));
    }
    public TradingRecipeBuilder exclusiveTo(String exclusiveTo) {
        return exclusiveTo(ExclusiveToCondition.fromString(exclusiveTo));
    }
    public TradingRecipeBuilder exclusiveTo(ExclusiveToCondition exclusiveToCondition) {
        params.exclusiveTo = exclusiveToCondition;
        return this;
    }

    public TradingRecipeBuilder withBiomeCondition(BiomeCondition biomeCondition) {
        params.biome = biomeCondition;
        return this;
    }


    public TradingRecipe build(){
        return new TradingRecipe(params);
    }





    public static class TradingRecipeParams {

        protected ResourceLocation id;
        protected NonNullList<Ingredient> itemIngredients;
        protected ItemStack result;
        protected int fuelConsumed;
        protected int processingTime;
        protected BiomeCondition biome;
        public ExclusiveToCondition exclusiveTo;


        protected TradingRecipeParams(ResourceLocation id) {
            this.id = id;
            itemIngredients = NonNullList.create();
            result = ItemStack.EMPTY;
            fuelConsumed = 0;
            processingTime = 1;
            biome = BiomeCondition.EMPTY;
            exclusiveTo = ExclusiveToCondition.EMPTY;
        }

    }
}
