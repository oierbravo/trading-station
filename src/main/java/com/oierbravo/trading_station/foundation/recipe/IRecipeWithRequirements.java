package com.oierbravo.trading_station.foundation.recipe;

import java.util.Map;

public interface IRecipeWithRequirements {
    Map<RecipeRequirementType<?>, RecipeRequirement> getRecipeRequirements();
}
