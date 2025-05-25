package com.oierbravo.trading_station.compat.jei;

import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import com.oierbravo.trading_station.registrate.ModMenus;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TradingRecipeHandler  implements IRecipeTransferHandler<TradingStationMenu, TradingRecipe> {

    @Override
    public Class<? extends TradingStationMenu> getContainerClass() {
        return TradingStationMenu.class;
    }

    @Override
    public Optional<MenuType<TradingStationMenu>> getMenuType() {
        return Optional.of(ModMenus.TRADING_STATION.get());
    }

    @Override
    public RecipeType<TradingRecipe> getRecipeType() {
        return JEIPlugin.TRAING_RECIPE;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(TradingStationMenu container, TradingRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
        if(!doTransfer)
            return null;
        return null;
    }
/*@Override
    public @Nullable IRecipeTransferError transferRecipe(TradingStationMenu container, TradingRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
       // for( index = 0; index < recipe.)
       if(!doTransfer)
            return null;
        Optional<IRecipeSlotView> input_0 = recipeSlots.findSlotByName("input_0");
        List<ItemStack> itemStacks= input_0.get().getItemStacks().toList();
        return null;
    }*/
}
