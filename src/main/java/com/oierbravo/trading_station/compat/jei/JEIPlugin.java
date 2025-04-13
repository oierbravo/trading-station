package com.oierbravo.trading_station.compat.jei;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import com.oierbravo.trading_station.registrate.ModBlocks;
import com.oierbravo.trading_station.registrate.ModMenus;
import com.oierbravo.trading_station.registrate.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    static RecipeType<TradingRecipe> TRAING_RECIPE =  RecipeType.create("trading_station","trading", TradingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TradingStation.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                TradingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TRADING_STATION.get()),TRAING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TRADING_STATION_UNBREAKABLE.get()),TRAING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_TRADING_STATION.get()),TRAING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_TRADING_STATION_UNBREAKABLE.get()),TRAING_RECIPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        List<TradingRecipe> tradingRecipes = ModRecipes.getAll();
        registration.addRecipes(TRAING_RECIPE, tradingRecipes);

    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        //registration.addRecipeTransferHandler(new TradingRecipeHandler(), TRAING_RECIPE);
        registration.addRecipeTransferHandler(TradingStationMenu.class, ModMenus.TRADING_STATION.get(), TRAING_RECIPE, 36, 2, 0, 36);

    }
}
