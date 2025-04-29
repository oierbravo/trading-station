package com.oierbravo.trading_station.compat.crafttweaker;

import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

@IRecipeHandler.For(TradingRecipe.class)
public class TradingRecipeHandler implements IRecipeHandler<TradingRecipe>{

    @Override
    public String dumpToCommandString(IRecipeManager<? super TradingRecipe> manager, TradingRecipe recipe) {
        return manager.getCommandString() + recipe.toString() + recipe.getResult() + "[" + recipe.getIngredients() + "]";
    }
    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super TradingRecipe> manager, TradingRecipe firstRecipe, U secondRecipe) {
        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super TradingRecipe> manager, TradingRecipe recipe) {
        return Optional.empty();
    }

    @Override
    public Optional<TradingRecipe> recompose(IRecipeManager<? super TradingRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        return Optional.empty();
    }


}
