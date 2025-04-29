package com.oierbravo.trading_station.compat.kubejs;

import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.compat.kubejs.recipe.TradingKubeRecipe;
import com.oierbravo.trading_station.compat.kubejs.recipe.TradingRecipeSchema;
import com.oierbravo.trading_station.content.trading_recipe.MachineRequirement;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class TradingStationJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeFactories(RecipeFactoryRegistry registry) {
        registry.register(TradingKubeRecipe.FACTORY);
    }
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(ModConstants.asResource(TradingRecipe.Type.ID), TradingRecipeSchema.SCHEMA);
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        if (registry.type().isServer()) {
            registry.add("MachineId", MachineRequirement.class);
        }
    }

}