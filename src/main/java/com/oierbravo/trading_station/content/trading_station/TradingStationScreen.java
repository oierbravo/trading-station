package com.oierbravo.trading_station.content.trading_station;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.gui.AbstractTradingScreen;
import com.oierbravo.trading_station.foundation.gui.Coords2D;
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

    private static final ResourceLocation TEXTURE = TradingStation.asResource("textures/gui/trading_station.png");

    public TradingStationScreen(TradingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

    }



    @Override
    public Coords2D getProgressArrowCoords() {
        return Coords2D.of(79, 47);
    }

    @Override
    protected Coords2D getTargetSelectButtonCoords() {
        return Coords2D.of(131, 31);
    }

    protected Coords2D getRedstoneButtonCoords() { return Coords2D.of(151,31); }


    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}