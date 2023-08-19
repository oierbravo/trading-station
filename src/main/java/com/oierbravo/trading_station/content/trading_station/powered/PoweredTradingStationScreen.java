package com.oierbravo.trading_station.content.trading_station.powered;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import com.oierbravo.trading_station.content.trading_station.TradingStationTargetSelectScreen;
import com.oierbravo.trading_station.foundation.gui.AbstractTradingScreen;
import com.oierbravo.trading_station.foundation.gui.Coords2D;
import com.oierbravo.trading_station.foundation.render.EnergyDisplayTooltipArea;
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

public class PoweredTradingStationScreen extends AbstractTradingScreen<PoweredTradingStationMenu> {

    private static final ResourceLocation TEXTURE = TradingStation.asResource("textures/gui/trading_station.png");
    private EnergyDisplayTooltipArea energyInfoArea;


    public PoweredTradingStationScreen(PoweredTradingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
    }
    private void assignEnergyInfoArea() {
        //energyInfoArea = new EnergyDisplayTooltipArea(0,0, menu.blockEntity.getEnergyStorage());
        energyInfoArea = new EnergyDisplayTooltipArea(((width - imageWidth) / 2) + 8,((height - imageHeight) / 2) + 24, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {

        super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
        RenderSystem.setShaderTexture(0, getTexture());

        renderSlotPlaceholder(pPoseStack,((width - imageWidth) / 2) + 7,((height - imageHeight) / 2) + 23);

        energyInfoArea.render(pPoseStack);


    }
    private void renderSlotPlaceholder(PoseStack pPoseStack, int pX, int pY){
        this.blit(pPoseStack, pX , pY , 0, 182,11, 32);

    }
    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltip(pPoseStack, pMouseX, pMouseY, x, y);
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }
    private void renderEnergyAreaTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 8, 24, 9, 30)) {
            this.renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }
    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }


    @Override
    protected Coords2D getProgressArrowCoords() {
        return Coords2D.of(79, 47);
    }

    @Override
    protected Coords2D getTargetSelectButtonCoords() {
        return Coords2D.of(131, 29);
    }
    protected Coords2D getRedstoneButtonCoords() { return Coords2D.of(151,28); }
}