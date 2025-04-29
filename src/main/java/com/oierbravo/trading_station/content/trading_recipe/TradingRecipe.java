package com.oierbravo.trading_station.content.trading_recipe;


import com.oierbravo.mechanicals.foundation.ingredient.CountableIngredient;
import com.oierbravo.mechanicals.foundation.recipe.AbstractMechanicalRecipe;
import com.oierbravo.mechanicals.foundation.recipe.AbstractMechanicalRecipeParams;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class TradingRecipe extends AbstractMechanicalRecipe<RecipeInput, TradingRecipe.TradingRecipeParams> {

    private final NonNullList<CountableIngredient> itemIngredients;
    private final ItemStack result;
    private final int processingTime;

    public TradingRecipe(TradingRecipeParams params) {
        super(params);
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.processingTime = params.processingTime;

    }


    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> outputIngredients = NonNullList.create();
        itemIngredients.forEach(countableIngredient -> outputIngredients.add(countableIngredient.ingredient()));
        return outputIngredients;
    }

    public NonNullList<CountableIngredient> getCountableIngredients() {
        return itemIngredients;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return TradingRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    @Override
    public boolean matches(RecipeInput pRecipeInput, Level pLevel) {
        if(pLevel.isClientSide)
            return false;
        if(pRecipeInput.size() != itemIngredients.size())
            return false;

        int matchedIngredients = 0;
        for (int i = 0; i < itemIngredients.size(); i++) {
            CountableIngredient ingredient = itemIngredients.get(i);

            for (int slot = 0; slot < pRecipeInput.size(); slot++) {
                ItemStack itemStack = pRecipeInput.getItem(slot);
                if(ingredient.test(pRecipeInput.getItem(slot))){
                    matchedIngredients++;
                }
            }


        }

        return matchedIngredients == itemIngredients.size();
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }
    public ItemStack getResult(){
        return result.copy();
    }

    public boolean matchIngredient(int slot, ItemStack stack) {
        if(getIngredients().size() - 1 < slot)
            return false;
        return getIngredients().get(slot).test(stack);
    }

    @Override
    public ArrayList<IRecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }



    public static class Type implements RecipeType<TradingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final RecipeType<TradingRecipe> RECIPE_TYPE = new Type();
        public static final String ID = "trading";
    }
    public static class TradingRecipeParams extends AbstractMechanicalRecipeParams {
        protected NonNullList<CountableIngredient> itemIngredients;
        protected ItemStack result;
        protected int processingTime;

        protected TradingRecipeParams() {
            super();
            itemIngredients = NonNullList.create();
            result = ItemStack.EMPTY;
            processingTime = 1;
        }
    }
}

