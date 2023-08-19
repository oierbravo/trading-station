package com.oierbravo.trading_station.compat.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.trading_station.content.trading_recipe.BiomeCondition;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.NativeObject;
import net.minecraft.world.level.biome.Biome;

public class BiomeConditionComponent implements RecipeComponent<BiomeCondition> {
    public static final RecipeComponent<BiomeCondition> BIOME_CONDITION= new BiomeConditionComponent();

    public ComponentRole role() {
        return ComponentRole.OTHER;
    }

    @Override
    public Class<?> componentClass() {
        return BiomeCondition.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, BiomeCondition value) {
        return value.toJson();
    }

    private BiomeCondition fromNativeObject(NativeObject nativeObject ){
        if(nativeObject.containsKey("tag"))
            return BiomeCondition.fromString("#" + nativeObject.get("tag"));
        return BiomeCondition.fromString(nativeObject.get("name").toString());
    }
    @Override
    public BiomeCondition read(RecipeJS recipe, Object from) {
        if (from instanceof BiomeCondition bc) {
            return bc;

        } else if (from instanceof Biome b) {
            return BiomeCondition.fromBiome(b);
        } else if (from instanceof JsonObject je) {
            return BiomeCondition.fromJson(je);
        } else if(from instanceof NativeObject no){
            return fromNativeObject(no);

        } else {
            return BiomeCondition.fromString(String.valueOf(from));
        }
    }
}