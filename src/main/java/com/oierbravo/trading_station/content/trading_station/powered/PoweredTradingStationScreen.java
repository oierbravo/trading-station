package com.oierbravo.trading_station.content.trading_station.powered;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.foundation.gui.AbstractTradingScreen;
import com.oierbravo.trading_station.foundation.gui.Coords2D;
import com.oierbravo.trading_station.foundation.render.EnergyInfoArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class PoweredTradingStationScreen extends AbstractTradingScreen<PoweredTradingStationMenu> {

    private static final ResourceLocation TEXTURE = TradingStation.asResource("textures/gui/trading_station.png");
    private EnergyInfoArea energyInfoArea;


    public PoweredTradingStationScreen(PoweredTradingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        RenderSystem.setShaderTexture(0, getTexture());

        renderSlotPlaceholder(pGuiGraphics,((width - imageWidth) / 2) + 7,((height - imageHeight) / 2) + 23);

        energyInfoArea.draw(pGuiGraphics);
    }

    private void assignEnergyInfoArea() {
        //energyInfoArea = new EnergyInfoArea(0,0, menu.blockEntity.getEnergyStorage());
        energyInfoArea = new EnergyInfoArea(((width - imageWidth) / 2) + 8,((height - imageHeight) / 2) + 24, menu.blockEntity.getEnergyStorage());
    }


    private void renderSlotPlaceholder(GuiGraphics pGuiGraphics, int pX, int pY){
        pGuiGraphics.blit(TEXTURE, pX , pY , 0, 182,11, 32);

    }
    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltip(pGuiGraphics.pose(), pMouseX, pMouseY, x, y);
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
    }
    private void renderEnergyAreaTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 8, 24, 9, 30)) {
           // this.renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
           //         Optional.empty(), pMouseX - x, pMouseY - y);
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