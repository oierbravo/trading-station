package com.oierbravo.trading_station.compat.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface TradingRecipeSchema {
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<Integer> PROCESSING_TIME = NumberComponent.INT.key("processingTime").optional(1);
   public class TradingRecipeJS extends RecipeJS{
       @Override
       public JsonElement writeInputItem(InputItem value) {
           JsonObject json =  super.writeInputItem(value).getAsJsonObject();
           if(value.count > 1){
               json.addProperty("count", value.count);
           }
           return (JsonElement) json;
       }
   }
    RecipeSchema SCHEMA = new RecipeSchema(TradingRecipeJS.class, TradingRecipeJS::new, RESULT, INGREDIENTS, PROCESSING_TIME);

}
