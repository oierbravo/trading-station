package com.oierbravo.trading_station.content.trading_recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ExclusiveToCondition implements Predicate {
    public static final ExclusiveToCondition EMPTY = new ExclusiveToCondition();
    public NonNullList<String> exclusives;

    public ExclusiveToCondition(){
        exclusives = NonNullList.create();
    }
    public ExclusiveToCondition(NonNullList<String> pExclusives){
        exclusives = pExclusives;
    }
    public static ExclusiveToCondition fromString(String exclusive) {
        NonNullList<String> exclusives = NonNullList.create();
        exclusives.add(exclusive);
        return new ExclusiveToCondition(exclusives);
    }
    public static ExclusiveToCondition fromList(NonNullList<String> exclusives) {
        return new ExclusiveToCondition(exclusives);
    }

    public static ExclusiveToCondition fromList(List<String> pExclusives) {
        NonNullList<String> exclusives = NonNullList.create();
        exclusives.addAll(pExclusives);
        return new ExclusiveToCondition(exclusives);
    }
    public ExclusiveToCondition add(String value){
        exclusives.add(value);
        return this;
    }

    public static ExclusiveToCondition fromJson(@Nullable JsonElement je) {
        ExclusiveToCondition condition = new ExclusiveToCondition();
        if(je.isJsonNull())
            return condition;
        if(je.isJsonArray()){
            condition.readInternal((JsonArray) je);
            return condition;
        }
        JsonObject jsonObject = je.getAsJsonObject();
        if(jsonObject.has("exclusivesTo"))
            condition.readInternal(jsonObject.getAsJsonArray("exclusivesTo"));
        return condition;
    }

    public JsonElement toJson() {
        JsonArray json = new JsonArray();
        writeInternal(json);
        return json;
    }
    /*public JsonArray toJsonArray(){

    }*/
    protected void readInternal(FriendlyByteBuf buffer){
        NonNullList<String> exclusives = NonNullList.create();

        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            exclusives.add(buffer.readUtf());
        this.exclusives = exclusives;
    };
    public void write(FriendlyByteBuf buffer) {
        writeInternal(buffer);
    }
    protected void writeInternal(FriendlyByteBuf buffer){
        buffer.writeVarInt(exclusives.size());
        exclusives.forEach(buffer::writeUtf);
    };
    public static ExclusiveToCondition read(FriendlyByteBuf buffer) {
        ExclusiveToCondition condition = new ExclusiveToCondition();
        condition.readInternal(buffer);
        return condition;
    }

    protected void readInternal(JsonArray json){
        NonNullList<String> exclusivesList = NonNullList.create();
        json.forEach(element -> {
            exclusivesList.add(element.getAsString());
        });
        this.exclusives = exclusivesList;
    };
    protected void readInternal(String element){
        NonNullList<String> exclusives = NonNullList.create();
        exclusives.add(element);
        this.exclusives = exclusives;
    };

    protected void writeInternal(JsonArray jsonArray){
        for (String exclusive : exclusives) {
            jsonArray.add(exclusive.toString());
        }
    };
    @Override
    public boolean test(Object o) {
        if(exclusives.isEmpty())
            return true;
        return exclusives.contains(o);
    }
}
