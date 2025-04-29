package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.foundation.component.TradingRecipeComponent;
import com.oierbravo.trading_station.registrate.ModItemComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

public class TargetedRecipeUtils {
    public static Optional<RecipeHolder<?>> getRecipeFromItemStack(ItemStack itemStack){
        if(itemStack.isEmpty())
            return Optional.empty();
        TradingRecipeComponent component = itemStack.getComponents().get(ModItemComponents.TRADING_RECIPE_ID);
        if(component == null)
            return Optional.empty();
        if(Minecraft.getInstance().level == null)
            return Optional.empty();
        return Minecraft.getInstance().level.getRecipeManager().byKey(component.tradingRecipeId());
    }
}
