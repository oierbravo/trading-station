package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TradingStation.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TradingStation.MODID);

    public static final RegistryObject<RecipeType<TradingRecipe>> TRADING_TYPE =
            RECIPE_TYPES.register("trading",() -> TradingRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<TradingRecipe>> TRADING_SERIALIZER =
            SERIALIZERS.register("trading", () -> TradingRecipe.Serializer.INSTANCE);

    public static Optional<TradingRecipe> find(SimpleContainer pInv, Level pLevel) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return pLevel.getRecipeManager().getRecipeFor(TradingRecipe.Type.INSTANCE,pInv,pLevel);
    }
    /*public static List<ItemStack> getAllOutputs(Level pLevel){
        List<ItemStack> allOutputs =  pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .map(TradingRecipe::getResult).toList();
        allOutputs.add(ItemStack.EMPTY);
        return allOutputs;

    }*/
    public static List<ItemStack> getAllOutputs(Level pLevel){
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .map(TradingRecipe::getResult).toList();

    }
    public static Optional<TradingRecipe> findByOutput(Level pLevel,ItemStack targetedOutput){
        //if(pLevel.isClientSide())
        //    return Optional.empty();
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matchesOutput(targetedOutput)).findFirst();

    }
    /*public static Optional<TradingRecipe> findWithPreferdOutput(SimpleContainer pInv, Level pLevel, ItemStack preferedOutput){
        if(pLevel.isClientSide())
            return Optional.empty();
        List<TradingRecipe> allTradingRecipes = pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE);

        return allTradingRecipes.stream().filter(extrudingRecipe -> TradingRecipe.matches(tradingStation,tradingRecipe)).findFirst();

    }*/
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);

        RECIPE_TYPES.register(eventBus);
    }
}
