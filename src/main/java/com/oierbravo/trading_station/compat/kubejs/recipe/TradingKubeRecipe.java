package com.oierbravo.trading_station.compat.kubejs.recipe;

import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;

public class TradingKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
            ModConstants.asResource(TradingRecipe.Type.ID),
            TradingKubeRecipe.class,
            TradingKubeRecipe::new
    );
}
