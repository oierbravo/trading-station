package com.oierbravo.trading_station.content.trading_recipe;


import com.oierbravo.mechanicals.foundation.ingredient.CountableIngredient;
import com.oierbravo.mechanicals.foundation.recipe.AbstractMechanicalRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class TradingRecipeBuilder extends AbstractMechanicalRecipeBuilder<TradingRecipe, TradingRecipe.TradingRecipeParams,TradingRecipeBuilder> {
    public TradingRecipeBuilder() {
        params = new TradingRecipe.TradingRecipeParams();
    }

    @Override
    public TradingRecipe build() {
        return new TradingRecipe(this.params);
    }

    @Override
    public TradingRecipeBuilder create() {
        params = new TradingRecipe.TradingRecipeParams();
        return this;
    }

    public TradingRecipeBuilder require(CountableIngredient... itemIngredients) {
        return require(NonNullList.of(CountableIngredient.EMPTY,itemIngredients));
    }

    public TradingRecipeBuilder require(NonNullList<CountableIngredient> itemIngredients) {
        params.itemIngredients = itemIngredients;
        return this;
    }

    public TradingRecipeBuilder output(ItemStack output) {
        params.result = output;
        return this;
    }
    public TradingRecipeBuilder output(ItemLike output) {
        return output(new ItemStack(output));
    }

    public TradingRecipeBuilder processingTime(int time) {
        params.processingTime = time;
        return this;
    }
    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation))
                .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        recipeOutput.accept(resourceLocation, build(), advancement.build(this.id.withPrefix("recipes/")));
    }
    @Override
    public void save(RecipeOutput recipeOutput) {
        save(recipeOutput, this.id);
    }
}