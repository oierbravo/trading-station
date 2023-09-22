package com.oierbravo.trading_station.content.trading_recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipeBuilder.TradingRecipeParams;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.ServiceLoader;

public class TradingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> itemIngredients;
    private final ItemStack result;

    private final int processingTime;

    private BiomeCondition biomeCondition;
    private ExclusiveToCondition exclusiveToCondition;


    public TradingRecipe(TradingRecipeParams params) {
        this.id = params.id;
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.processingTime = params.processingTime;
        this.biomeCondition = params.biome;
        this.exclusiveToCondition = params.exclusiveTo;

    }
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return matches(pContainer, pLevel, (Biome) null, "");
    }


    public boolean matchesBiome(Biome biome, Level pLevel){
        return biomeCondition.test(biome, pLevel);
    }

    public boolean matchesExclusiveTo(String targetedMachine){
        return exclusiveToCondition.test(targetedMachine);
    }

    public boolean matches(SimpleContainer pContainer, Level pLevel, @Nullable Biome biome, String traderType) {
        if(pLevel.isClientSide)
            return false;
        if(pContainer.getContainerSize() != itemIngredients.size())
            return false;

        if(!getBiomeCondition().test(biome,pLevel))
            return false;
        if(!getExclusiveToCondition().test(traderType))
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
    public boolean matches(SimpleContainer pContainer, Level pLevel, ItemStack preferedOutput){
        return false;
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


    public BiomeCondition getBiomeCondition(){
        return biomeCondition;
    }
    public ExclusiveToCondition getExclusiveToCondition(){
        return exclusiveToCondition;
    }

    public ItemStack getResult(){
        return result.copy();
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public boolean matchesId(ResourceLocation pId) {
        return id.equals(pId);
    }

    public static class Type implements RecipeType<TradingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final  RecipeType<TradingRecipe> RECIPE_TYPE = new Type();
        public static final String ID = "trading";
    }
    public static class Serializer implements RecipeSerializer<TradingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(TradingStation.MODID,"trading");

        @Override
        public TradingRecipe fromJson(ResourceLocation id, JsonObject json) {
            TradingRecipeBuilder builder = new TradingRecipeBuilder(id);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            int processingTime = 1;
            BiomeCondition biomeCondition = BiomeCondition.EMPTY;
            ExclusiveToCondition exclusiveToCondition = ExclusiveToCondition.EMPTY;

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

            if(GsonHelper.isValidNode(json,"biome")){
                biomeCondition = BiomeCondition.fromJson(json.get("biome"));
            }

            if(GsonHelper.isValidNode(json,"exclusiveTo")){
                exclusiveToCondition = ExclusiveToCondition.fromJson(json.get("exclusiveTo"));
            }

            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .processingTime(processingTime)
                    .withBiomeCondition(biomeCondition)
                    .exclusiveTo(exclusiveToCondition);

            return builder.build();
        }

        @Override
        public TradingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            TradingRecipeBuilder builder = new TradingRecipeBuilder(id);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            int processingTime = 1;
            BiomeCondition biomeCondition = BiomeCondition.EMPTY;
            ExclusiveToCondition exclusiveToCondition = ExclusiveToCondition.EMPTY;

            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                itemIngredients.add(Ingredient.fromNetwork(buffer));

            ItemStack result = buffer.readItem();
            processingTime = buffer.readInt();
            biomeCondition = BiomeCondition.read(buffer);
            exclusiveToCondition = exclusiveToCondition.read(buffer);


            builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .processingTime(processingTime)
                    .withBiomeCondition(biomeCondition)
                    .exclusiveTo(exclusiveToCondition);


            return builder.build();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TradingRecipe recipe) {
            NonNullList<Ingredient> itemIngredients = recipe.itemIngredients;
            int processingTime = recipe.getProcessingTime();
            BiomeCondition biomeCondition = recipe.getBiomeCondition();
            ExclusiveToCondition exclusiveToCondition = recipe.getExclusiveToCondition();

            buffer.writeVarInt(itemIngredients.size());
            itemIngredients.forEach(i -> i.toNetwork(buffer));
            buffer.writeItem(recipe.getResult());
            buffer.writeInt(processingTime);
            biomeCondition.write(buffer);
            exclusiveToCondition.write(buffer);
        }


    }


}
