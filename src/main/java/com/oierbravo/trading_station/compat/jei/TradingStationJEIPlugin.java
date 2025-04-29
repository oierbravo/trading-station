package com.oierbravo.trading_station.compat.jei;

import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationScreen;
import com.oierbravo.trading_station.registrate.ModBlocks;
import com.oierbravo.trading_station.registrate.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

@JeiPlugin
public class TradingStationJEIPlugin implements IModPlugin {
    public static RecipeType<TradingRecipe> TRADING_RECIPE =  RecipeType.create("trading_station","trading", TradingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ModConstants.asResource( "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                TradingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TRADING_STATION.get()), TRADING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TRADING_STATION_UNBREAKABLE.get()), TRADING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_TRADING_STATION.get()), TRADING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_TRADING_STATION_UNBREAKABLE.get()), TRADING_RECIPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        List<RecipeHolder<TradingRecipe>> tradingRecipes = ModRecipes.getAll();
        registration.addRecipes(TRADING_RECIPE, tradingRecipes.stream().map(RecipeHolder::value).toList());

    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TradingStationScreen.class, 160, 17, 10, 12,
                TRADING_RECIPE);
    }
}
