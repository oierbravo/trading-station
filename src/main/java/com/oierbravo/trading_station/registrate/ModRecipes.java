package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
        return find(pInv, pLevel, null);
    }

    public static Optional<TradingRecipe> find(SimpleContainer pInv, Level pLevel,@Nullable Biome biome) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter((tradingRecipe -> tradingRecipe.matchesBiome(biome, pLevel)))
                .findAny();

    }

    public static List<ItemStack> getAllOutputs(Level pLevel,@Nullable Biome biome, String machineType){
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter((tradingRecipe -> tradingRecipe.matchesBiome(biome, pLevel)))
                .filter((tradingRecipe -> tradingRecipe.matchesExclusiveTo(machineType)))
                .sorted((recipe1, recipe2) -> recipe1.getId().compareNamespaced(recipe2.getId()))
                .map(TradingRecipe::getResult)
                .toList();

    }
    public static Optional<TradingRecipe> findByOutput(Level pLevel,ItemStack targetedOutput){
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matchesOutput(targetedOutput)).findFirst();

    }
    public static void register(IEventBus eventBus) {

        SERIALIZERS.register(eventBus);

        RECIPE_TYPES.register(eventBus);
    }
}
