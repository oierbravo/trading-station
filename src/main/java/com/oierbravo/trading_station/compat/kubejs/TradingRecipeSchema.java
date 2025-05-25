package com.oierbravo.trading_station.compat.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.component.TagKeyComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface TradingRecipeSchema {
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<Integer> PROCESSING_TIME = NumberComponent.INT.key("processingTime").optional(1);
    RecipeKey<Integer> MIN_HEIGHT = NumberComponent.INT.key("min_height").defaultOptional().allowEmpty();
    RecipeKey<Integer> MAX_HEIGHT = NumberComponent.INT.key("max_height").defaultOptional().allowEmpty();
    RecipeKey<TagKey<Biome>> BIOME = TagKeyComponent.BIOME.key("biome").defaultOptional().allowEmpty();
    RecipeKey<String> MACHINE = StringComponent.ANY.key("machine").defaultOptional().allowEmpty();


    public class TradingRecipeJS extends RecipeJS {
        public RecipeJS minHeight(int value) {
            return setValue(MIN_HEIGHT, value);
        }

        public RecipeJS maxHeight(int value) {
            return setValue(MAX_HEIGHT, value);
        }

        @Override
        public JsonElement writeInputItem(InputItem value) {
            JsonObject json = super.writeInputItem(value).getAsJsonObject();
            if (value.count > 1) {
                json.addProperty("count", value.count);
            }
            return (JsonElement) json;
        }
    }

    //RecipeSchema SCHEMA = new RecipeSchema(TradingRecipeJS.class, TradingRecipeJS::new, RESULT, INGREDIENTS, PROCESSING_TIME,BIOME, EXCLUSIVE_TO);
    RecipeSchema SCHEMA = new RecipeSchema(TradingRecipeJS.class, TradingRecipeJS::new, RESULT, INGREDIENTS, PROCESSING_TIME, MIN_HEIGHT, MAX_HEIGHT, BIOME, MACHINE);
}
