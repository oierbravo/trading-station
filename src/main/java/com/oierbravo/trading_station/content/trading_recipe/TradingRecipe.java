package com.oierbravo.trading_station.content.trading_recipe;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.*;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.BiomeRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MaxHeightRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MinHeightRequirement;
import com.oierbravo.trading_station.TradingStation;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class TradingRecipe extends BaseRecipe<SimpleContainer, TradingRecipe.TradingRecipeParams> {

    private final NonNullList<Ingredient> itemIngredients;
    private final ItemStack result;
    private final int processingTime;

    public static List<RecipeRequirementType<?>> enabledRecipeRequirements = List.of(
            BiomeRequirement.TYPE,
            MinHeightRequirement.TYPE,
            MaxHeightRequirement.TYPE
    );

    public TradingRecipe(IRecipeTypeInfo typeInfo, TradingRecipeParams params) {
        super(typeInfo, params);
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.processingTime = params.processingTime;

    }


    @Override
    public Map<RecipeRequirementType<?>, RecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }

    @Override
    public void setRecipeRequirements(Map<RecipeRequirementType<?>, RecipeRequirement> map) {
        recipeRequirements = map;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide)
            return false;
        if(pContainer.getContainerSize() != itemIngredients.size())
            return false;

        /*if(!getBiomeCondition().test(biome,pLevel))
            return false;
        if(!getExclusiveToCondition().test(traderType))
            return false;*/

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

    public boolean matchesOutput(ItemStack targetItemStack){
        return ItemStack.isSameItem(targetItemStack,result);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return itemIngredients;
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
    public boolean matchesId(ResourceLocation pId) {
        return pId.toString().equals(id.toString());
    }

    public boolean matchesBiome(Biome biome, Level pLevel) {
        return true;
    }

    public boolean matchesExclusiveTo(String machineType) {
        return true;
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
        public static final TradingRecipeSerializer INSTANCE = new TradingRecipeSerializer();

        public static final ResourceLocation ID =
                new ResourceLocation(TradingStation.MODID,"trading");

        public TradingRecipeSerializer(TradingRecipeBuilder.TradingRecipeFactory pFactory, List<RecipeRequirementType<?>> pEnabledRecipeRequirements) {
            super((BaseRecipeBuilder.MechanicalRecipeFactory<TradingRecipe>) pFactory, pEnabledRecipeRequirements);
        }

        public TradingRecipeSerializer() {
            super();
        }

        @Override
        protected TradingRecipeBuilder readFromJson(ResourceLocation recipeId, JsonObject json) {
            TradingRecipeBuilder builder = new TradingRecipeBuilder(this.factory,recipeId);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            int processingTime = 1;
            //BiomeCondition biomeCondition = BiomeCondition.EMPTY;
            //ExclusiveToCondition exclusiveToCondition = ExclusiveToCondition.EMPTY;

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

           /* if(GsonHelper.isValidNode(json,"biome")){
                biomeCondition = BiomeCondition.fromJson(json.get("biome"));
            }

            if(GsonHelper.isValidNode(json,"exclusiveTo")){
                exclusiveToCondition = ExclusiveToCondition.fromJson(json.get("exclusiveTo"));
            }
*/
            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .processingTime(processingTime);
                    //.withBiomeCondition(biomeCondition)
                    //.exclusiveTo(exclusiveToCondition);

            return builder;
        }

        @Override
        protected TradingRecipeBuilder readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        protected void writeToJson(JsonObject json, TradingRecipe recipe) {

        }

        @Override
        protected void writeToBuffer(FriendlyByteBuf buffer, TradingRecipe recipe) {

        }
    }
}

