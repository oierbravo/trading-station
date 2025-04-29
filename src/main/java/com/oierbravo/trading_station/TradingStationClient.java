package com.oierbravo.trading_station;

import com.mojang.datafixers.util.Either;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TargetedRecipeUtils;
import com.oierbravo.trading_station.foundation.component.TradingRecipeComponent;
import com.oierbravo.trading_station.foundation.gui.TradingRecipeTooltipComponent;
import com.oierbravo.trading_station.registrate.ModItemComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.util.Optional;
import java.util.function.Function;

@Mod(value = ModConstants.MODID, dist = Dist.CLIENT)
@EventBusSubscriber
public class TradingStationClient {
    public TradingStationClient(net.neoforged.bus.api.IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(TradingStationClient::onRegisterClientTooltipEvent);

    }
    private static void onRegisterClientTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(TradingRecipeTooltipComponent.class, Function.identity());
    }

    @SubscribeEvent
    public static void gatherTooltips(RenderTooltipEvent.GatherComponents event){
        Optional<RecipeHolder<?>> recipeHolder =TargetedRecipeUtils.getRecipeFromItemStack( event.getItemStack());
        if(recipeHolder.isEmpty())
            return;
        TradingRecipe recipe = (TradingRecipe) recipeHolder.get().value();
        event.getTooltipElements().add(Either.right(new TradingRecipeTooltipComponent(recipe)));
    }
}
