package com.oierbravo.trading_station.compat.jei;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import com.oierbravo.trading_station.foundation.gui.AbstractTradingMenu;
import com.oierbravo.trading_station.registrate.ModRecipes;
import com.oierbravo.trading_station.registrate.PoweredTradingStationRegistrate;
import com.oierbravo.trading_station.registrate.TradingStationRegistrate;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    static RecipeType<TradingRecipe> TRAING_RECIPE =  new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class);

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
        registration.addRecipeCatalyst(new ItemStack(TradingStationRegistrate.BLOCK.get()),new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class));
        registration.addRecipeCatalyst(new ItemStack(TradingStationRegistrate.BLOCK_UNBREAKABLE.get()),new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class));
        registration.addRecipeCatalyst(new ItemStack(PoweredTradingStationRegistrate.BLOCK.get()),new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class));
        registration.addRecipeCatalyst(new ItemStack(PoweredTradingStationRegistrate.BLOCK_UNBREAKABLE.get()),new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<TradingRecipe> tradingRecipes = rm.getAllRecipesFor(TradingRecipe.Type.INSTANCE);
        registration.addRecipes(TRAING_RECIPE, tradingRecipes);

    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        //registration.addRecipeTransferHandler(new TradingRecipeHandler(), TRAING_RECIPE);
        //registration.addRecipeTransferHandler(TradingStationMenu.class, TradingStationRegistrate.MENU.get(), new RecipeType<>(TradingRecipeCategory.UID, TradingRecipe.class), 36, 2, 0, 36);

    }
}
