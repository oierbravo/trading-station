package com.oierbravo.trading_station.compat.kubejs;

import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public class KubeJSPlugin extends dev.latvian.mods.kubejs.KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(TradingRecipe.Serializer.ID, TradingRecipeSchema.SCHEMA);
    }

}