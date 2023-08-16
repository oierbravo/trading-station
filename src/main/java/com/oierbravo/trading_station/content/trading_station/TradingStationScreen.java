package com.oierbravo.trading_station.content.trading_station;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.gui.AbstractTradingScreen;
import com.oierbravo.trading_station.foundation.render.FakeItemRenderer;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.Optional;

public class TradingStationScreen extends AbstractTradingScreen<TradingStationMenu> {

    protected static int[] progressArrowCoords = {79,47};
    protected static int[] targetSelectButtonCoords = {131,18};
    private static final ResourceLocation TEXTURE = TradingStation.asResource("textures/gui/trading_station.png");

    public TradingStationScreen(TradingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

    }



    @Override
    public int[] getProgressArrowCoords() {
        return progressArrowCoords;
    }

    @Override
    protected int[] getTargetSelectButtonCoords() {
        return targetSelectButtonCoords;
    }


    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}