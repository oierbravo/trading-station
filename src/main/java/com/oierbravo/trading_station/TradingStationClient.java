package com.oierbravo.trading_station;

import com.mojang.datafixers.util.Either;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.gui.TradingRecipeTooltipComponent;
import com.oierbravo.trading_station.foundation.gui.TradingRecipeTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class TradingStationClient {
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(TradingStationClient::onRegisterClientTooltipEvent);
    }
    private static void onRegisterClientTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(TradingRecipeTooltipComponent.class, TradingRecipeTooltipRenderer::new);
    }

    @SubscribeEvent
    public static void gatherTooltips(RenderTooltipEvent.GatherComponents event)
    {
        CompoundTag tag = event.getItemStack().getOrCreateTag();
        String tradingRecipeId = tag.getString("tradingRecipeId");
        if(!tradingRecipeId.isEmpty()){
            Optional<TradingRecipe> recipe = (Optional<TradingRecipe>) Minecraft.getInstance().level.getRecipeManager().byKey(new ResourceLocation(tradingRecipeId));
            recipe.ifPresent(tradingRecipe -> event.getTooltipElements().add(Either.right(new TradingRecipeTooltipComponent(tradingRecipe))));
        }
    }
}
