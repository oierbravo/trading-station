package com.oierbravo.trading_station.compat.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.trading_station.content.trading_recipe.BiomeCondition;
import com.oierbravo.trading_station.content.trading_recipe.ExclusiveToCondition;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.NativeArray;
import dev.latvian.mods.rhino.NativeObject;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class ExclusiveToConditionComponent implements RecipeComponent<ExclusiveToCondition> {
    public static final RecipeComponent<ExclusiveToCondition> EXCLUSIVE_CONDITION = new ExclusiveToConditionComponent();


    public ComponentRole role() {
        return ComponentRole.OTHER;
    }

    @Override
    public Class<?> componentClass() {
        return ExclusiveToCondition.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, ExclusiveToCondition value) {
        return value.toJson();
    }

private ExclusiveToCondition fromNativeArray(NativeArray pNativeArray){
        return ExclusiveToCondition.fromList(pNativeArray.stream().toList());
}
    @Override
    public ExclusiveToCondition read(RecipeJS recipe, Object from) {
        if (from instanceof ExclusiveToCondition ec) {
            return ec;
        } else if(from instanceof NativeArray nativeArray){
            return fromNativeArray(nativeArray);

        } else {
            return ExclusiveToCondition.fromString(String.valueOf(from));
        }
    }
}