package com.oierbravo.trading_station.compat.jei;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationScreen;
import com.oierbravo.trading_station.registrate.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

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
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TRADING_STATION.get()),new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<TradingRecipe> tradingRecipes = rm.getAllRecipesFor(TradingRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class), tradingRecipes);

    }
}
