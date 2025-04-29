package com.oierbravo.trading_station.compat.kubejs.recipe;

import com.oierbravo.mechanicals.compat.kubejs.components.CountableIngredientComponent;
import com.oierbravo.mechanicals.compat.kubejs.components.RecipeRequirementsComponent;
import com.oierbravo.mechanicals.foundation.ingredient.CountableIngredient;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity.DEFAULT_PROCESSING_TIME;

public interface TradingRecipeSchema {
    RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT).noFunctions();
    RecipeKey<List<CountableIngredient>> INGREDIENTS = CountableIngredientComponent.COUNTABLE_INGREDIENT.asList().key("ingredients", ComponentRole.INPUT).noFunctions();
    RecipeKey<Integer> PROCESSING_TIME = NumberComponent.INT.key("processingTime",  ComponentRole.OTHER).optional(DEFAULT_PROCESSING_TIME).allowEmpty();
    RecipeKey<List<IRecipeRequirement>> RECIPE_REQUIREMENTS = RecipeRequirementsComponent.RECIPE_REQUIREMENT.asList().key("requirements", ComponentRole.OTHER).optional(List.of()).allowEmpty();

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, PROCESSING_TIME, RECIPE_REQUIREMENTS).factory(TradingKubeRecipe.FACTORY);
}
