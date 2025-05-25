package com.oierbravo.trading_station.content.trading_recipe;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.*;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.BiomeRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MaxHeightRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MinHeightRequirement;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.registrate.ModRecipes;
import dev.latvian.mods.kubejs.recipe.schema.RecipeOptional;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradingRecipe extends BaseRecipe<SimpleContainer, TradingRecipe.TradingRecipeParams> {

    private final NonNullList<Ingredient> itemIngredients;
    private final ItemStack result;
    private final int processingTime;

    public static List<RecipeRequirementType<?>> enabledRecipeRequirements = List.of(
            BiomeRequirement.TYPE,
            MinHeightRequirement.TYPE,
            MaxHeightRequirement.TYPE,
            MachineRequirement.TYPE
    );

    public TradingRecipe(TradingRecipeParams params) {
        super(params);
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.processingTime = params.processingTime;

    }


    @Override
    public Map<RecipeRequirementType<?>, RecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }

    @Override
    public boolean checkRequirements(Level level, BlockEntity blockEntity) {
        ArrayList<String> missingRequirements =  RecipeRequirementsUtils.checkRequirements(getRecipeRequirements(),blockEntity);
        if(missingRequirements.isEmpty())
            return true;
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return super.getId();
    }

    @Override
    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide)
            return false;
        if(pContainer.getContainerSize() != itemIngredients.size())
            return false;

        int matchedIngredients = 0;
        for (int i = 0; i < itemIngredients.size(); i++) {
            Ingredient ingredient = itemIngredients.get(i);

            for (int slot = 0; slot < pContainer.getContainerSize(); slot++) {
                ItemStack itemStack = pContainer.getItem(slot);
                if(ingredient.test(pContainer.getItem(slot))){
                    matchedIngredients++;
                }
            }


        }

        return matchedIngredients == itemIngredients.size();

    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return itemIngredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new TradingRecipeSerializer(TradingRecipe.enabledRecipeRequirements);
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }


    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }
    public ItemStack getResult(){
        return result.copy();
    }

    public ItemStack getResultWithRecipeId(){
        ItemStack itemStack =  result.copy();
        CompoundTag tag = ModRecipes.getItemTagWithRecipeId(itemStack,id.toString());
        itemStack.setTag(tag);
        return itemStack;
    }
    public boolean matchesId(ResourceLocation pId) {
        return pId.toString().equals(id.toString());
    }

    public boolean matchIngredient(int slot, ItemStack stack) {
        if(getIngredients().size() - 1 < slot)
            return false;
        return getIngredients().get(slot).test(stack);
    }


    public static class Type implements RecipeType<TradingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final RecipeType<TradingRecipe> RECIPE_TYPE = new Type();
        public static final String ID = "trading";
    }
    public static class TradingRecipeParams extends BaseRecipeParams {
        protected NonNullList<Ingredient> itemIngredients;
        protected ItemStack result;
        protected int fuelConsumed;
        protected int processingTime;

        protected TradingRecipeParams(ResourceLocation id) {
            super(id);
            itemIngredients = NonNullList.create();
            result = ItemStack.EMPTY;
            fuelConsumed = 0;
            processingTime = 1;
        }
    }
    public static class TradingRecipeSerializer extends BaseRecipeSerializer<TradingRecipe, TradingRecipeBuilder> {

        public static final ResourceLocation ID =
                new ResourceLocation(TradingStation.MODID,"trading");

        public TradingRecipeSerializer(List<RecipeRequirementType<?>> pEnabledRecipeRequirements) {
            super(pEnabledRecipeRequirements);
        }


        @Override
        protected TradingRecipeBuilder readFromJson(ResourceLocation recipeId, JsonObject json) {
            TradingRecipeBuilder builder = new TradingRecipeBuilder(recipeId);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            int processingTime = 1;

            for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
                JsonObject jsonObject = je.getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(jsonObject);
                if(jsonObject.has("count")){
                    ItemStack itemStack = ingredient.getItems()[0];
                    itemStack.setCount(jsonObject.get("count").getAsInt());
                }

                itemIngredients.add(ingredient);
            }

            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            if(GsonHelper.isValidNode(json,"processingTime")){
                processingTime = GsonHelper.getAsInt(json,"processingTime");
            }

            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .processingTime(processingTime);
            return builder;
        }

        @Override
        protected TradingRecipeBuilder readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            TradingRecipeBuilder builder = new TradingRecipeBuilder(recipeId);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();

            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                itemIngredients.add(Ingredient.fromNetwork(buffer));

            ItemStack result = buffer.readItem();
            int processingTime = buffer.readInt();

            return builder
                    .withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .processingTime(processingTime);
        }

        @Override
        protected void writeToJson(JsonObject pJson, TradingRecipe pRecipe) {

        }

        @Override
        protected void writeToBuffer(FriendlyByteBuf buffer, TradingRecipe pRecipe) {
            NonNullList<Ingredient> itemIngredients = pRecipe.itemIngredients;
            buffer.writeVarInt(itemIngredients.size());
            itemIngredients.forEach(i -> i.toNetwork(buffer));
            buffer.writeItemStack(pRecipe.getResult(),false);
            buffer.writeInt(pRecipe.getProcessingTime());
        }

    }
}

