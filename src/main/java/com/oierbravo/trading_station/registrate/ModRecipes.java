package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipeSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModConstants.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ModConstants.MODID);


    public static final Supplier<RecipeType<TradingRecipe>> TRADING_TYPE =
            RECIPE_TYPES.register("trading",() -> new RecipeType<>() {
                @Override
                public String toString() {
                    return TradingRecipe.Type.ID;
                }
            });

    public static final Supplier<TradingRecipeSerializer> TRADING_SERIALIZER =
            SERIALIZERS.register(TradingRecipe.Type.ID, () -> TradingRecipeSerializer.INSTANCE);


    public static List<RecipeHolder<TradingRecipe>> getAllRecipesForMachine(Level pLevel, BlockEntity pBlockEntity) {
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter((tradingRecipeHolder -> tradingRecipeHolder.value().meetsRequirements(pBlockEntity)))
                .toList();
    }

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }


    public static Optional<RecipeHolder<?>> findById(Level pLevel, ResourceLocation recipeId) {
        if(pLevel == null)
            return Optional.empty();
        return pLevel.getRecipeManager().byKey(recipeId);
    }
    public static Optional<RecipeHolder<?>> findById(Level pLevel, String pRecipeId) {
        if(pLevel == null)
            return Optional.empty();
        if(pRecipeId.isEmpty())
            return Optional.empty();
        return findById(pLevel, ResourceLocation.tryParse(pRecipeId));
    }

    public static List<RecipeHolder<TradingRecipe>> getAll() {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        return rm.getAllRecipesFor(TradingRecipe.Type.INSTANCE);
    }
}
