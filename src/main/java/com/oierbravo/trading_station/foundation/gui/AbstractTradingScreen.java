package com.oierbravo.trading_station.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationScreen;
import com.oierbravo.trading_station.content.trading_station.TradingStationTargetSelectScreen;
import com.oierbravo.trading_station.foundation.render.FakeItemRenderer;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.foundation.util.MouseUtil;
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

public abstract class AbstractTradingScreen<MENU extends AbstractTradingMenu> extends AbstractContainerScreen<MENU> {

    public AbstractTradingScreen(MENU pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 4;
        this.titleLabelY = 4;
        this.inventoryLabelY = 100000;
        this.addRenderableWidget(new ExtendedButton(leftPos + getTargetSelectButtonCoords()[0],topPos + getTargetSelectButtonCoords()[1],16,16, ModLang.translate("select_target.button"), btn ->{
            TradingStationTargetSelectScreen screen = new TradingStationTargetSelectScreen( this.menu.blockEntity);
            Minecraft.getInstance().pushGuiLayer(screen);

        }));
    }
      @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, getTexture());
        this.fillGradient(pPoseStack, 0, 0, this.width, this.height, -1072689136, -804253680);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            this.blit(pPoseStack, x + getProgressArrowCoords()[0], y + getProgressArrowCoords()[1], 179, 0, menu.getScaledProgress(), 7);
        }

        if(!menu.blockEntity.getTargetItemStack().isEmpty()){
            Optional<TradingRecipe> recipe = ModRecipes.findByOutput(menu.blockEntity.getLevel(),menu.blockEntity.getTargetItemStack());
            if(recipe.isPresent()){
                renderFakeRecipe(recipe.get(), pPoseStack);
            }
        }
    }
    private void renderFakeRecipe(TradingRecipe recipe, PoseStack pPoseStack){
        for(int index = 0; index < recipe.getIngredients().size(); index++){
            Ingredient ingredient = recipe.getIngredients().get(index);
            int[][] coords = this.menu.getInputSlotCoords();

            if(!ingredient.isEmpty())
                FakeItemRenderer.renderFakeItem(ingredient.getItems()[0],getGuiLeft() + this.menu.getInputSlotCoords()[index][0],getGuiTop() + this.menu.getInputSlotCoords()[index][1], .5f);
        }
        FakeItemRenderer.renderFakeItem(recipe.getResultItem(),getGuiLeft() +  this.menu.getOutputSlotCoords()[0],getGuiTop() +  this.menu.getOutputSlotCoords()[1], .5f);

    }


    protected abstract int[] getProgressArrowCoords();

    protected abstract  int[] getTargetSelectButtonCoords();


    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    protected abstract ResourceLocation getTexture();

    protected boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
