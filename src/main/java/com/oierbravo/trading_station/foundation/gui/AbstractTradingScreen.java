package com.oierbravo.trading_station.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_station.TradingStationTargetSelectScreen;
import com.oierbravo.trading_station.foundation.render.FakeItemRenderer;
import com.oierbravo.trading_station.foundation.util.MiscTools;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.foundation.util.MouseUtil;
import com.oierbravo.trading_station.network.packets.RedstoneModeSyncC2SPacket;
import com.oierbravo.trading_station.registrate.ModMessages;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractTradingScreen<MENU extends AbstractTradingMenu> extends AbstractContainerScreen<MENU> {
    protected Map<String, Button> buttons = new HashMap<>();
    protected byte currentRedstoneMode;


    public AbstractTradingScreen(MENU pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 4;
        this.titleLabelY = 13;
        this.inventoryLabelY = 100000;
        this.imageWidth = 176;
        this.imageHeight = 146;

        addTargetSelectButton();

        currentRedstoneMode = menu.getCurrentRedstoneMode();
        addRedstoneButton();

        for (Map.Entry<String, Button> button : buttons.entrySet()) {
            addRenderableWidget(button.getValue());
        }

    }
    protected abstract ResourceLocation getTexture();

      @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, getTexture());
        PoseStack poseStack = pGuiGraphics.pose();
        pGuiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        pGuiGraphics.blit(getTexture(), x, y,  0,0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            pGuiGraphics.blit(getTexture(), x + getProgressArrowCoords().x, y + getProgressArrowCoords().y, 179, 0, menu.getScaledProgress(), 7);
        }
        renderSlotPlaceholder(pGuiGraphics,getGuiLeft() +  this.menu.getInputSlotCoords()[0].x -1,getGuiTop() +  this.menu.getInputSlotCoords()[0].y -1);
        renderSlotPlaceholder(pGuiGraphics,getGuiLeft() +  this.menu.getInputSlotCoords()[1].x -1,getGuiTop() +  this.menu.getInputSlotCoords()[1].y -1);
        renderSlotPlaceholder(pGuiGraphics,getGuiLeft() +  this.menu.getOutputSlotCoords().x -1,getGuiTop() +  this.menu.getOutputSlotCoords().y -1);




        if(!menu.blockEntity.getTargetItemStack().isEmpty()){
            Optional<TradingRecipe> recipe = ModRecipes.findByOutput(menu.getLevel(),menu.blockEntity.getTargetItemStack());
            if(recipe.isPresent()){
                renderFakeRecipe(recipe.get(), pGuiGraphics);
            }
        }
    }
    private void renderItem( ItemStack pItemStack, int pX, int pY){

      FakeItemRenderer.renderFakeItem(pItemStack, pX, pY);
    }
    private void renderSlotPlaceholder(GuiGraphics pGuiGraphics, int pX, int pY){
        pGuiGraphics.blit(getTexture(), pX , pY , 0, 164,18, 18);

    }

    private void renderInputRecipePlaceholder(GuiGraphics pGuiGraphics, int pX, int pY){
        pGuiGraphics.blit(getTexture(), pX , pY , 18, 146,18, 23);

    }
    private void renderFakeRecipe(TradingRecipe recipe, GuiGraphics pGuiGraphics){
        for(int index = 0; index < recipe.getIngredients().size(); index++){
            Ingredient ingredient = recipe.getIngredients().get(index);

            if(!ingredient.isEmpty())
                renderInputRecipePlaceholder(pGuiGraphics, getGuiLeft() + this.menu.getInputRecipeCoords()[index].x, getGuiTop() + this.menu.getInputRecipeCoords()[index].y);
                renderItem(ingredient.getItems()[0],getGuiLeft() + this.menu.getInputRecipeCoords()[index].x +  1,getGuiTop() + this.menu.getInputRecipeCoords()[index].y + 1);
        }
        //renderItem(recipe.getResultItem(),getGuiLeft() +  this.menu.getOutputSlotCoords().x,getGuiTop() +  this.menu.getOutputSlotCoords().y);
    }


    protected abstract Coords2D getProgressArrowCoords();

    protected abstract  Coords2D getTargetSelectButtonCoords();
    protected abstract Coords2D getRedstoneButtonCoords();


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        Button redstoneMode = buttons.get("redstoneMode");
        currentRedstoneMode = this.menu.getCurrentRedstoneMode();
        ((ToggleButton) redstoneMode).setTexturePosition(currentRedstoneMode);
        if (MiscTools.inBounds(redstoneMode.getX(), redstoneMode.getY(), redstoneMode.getWidth(), redstoneMode.getHeight(), pMouseX, pMouseY)) {
            MutableComponent translatableComponents[] = new MutableComponent[3];
            translatableComponents[0] = ModLang.translate("screen.redstone.ignored");
            translatableComponents[1] = ModLang.translate("screen.redstone.low");
            translatableComponents[2] = ModLang.translate("screen.redstone.high");

            pGuiGraphics.renderTooltip(this.font, ModLang.translate("screen.redstone.redstoneMode").append(translatableComponents[currentRedstoneMode]), pMouseX, pMouseY);

        }
        Button targetSelect = buttons.get("targetSelect");
        if (MiscTools.inBounds(targetSelect.getX(), targetSelect.getY(), targetSelect.getWidth(), targetSelect.getHeight(), pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(this.font, ModLang.translate("select_target.title"), pMouseX, pMouseY);
        }

    }


    protected boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
    public void addRedstoneButton() {
        ResourceLocation[] redstoneTextures = new ResourceLocation[3];
        redstoneTextures[0] = new ResourceLocation(TradingStation.MODID, "textures/gui/buttons/redstoneignore.png");
        redstoneTextures[1] = new ResourceLocation(TradingStation.MODID, "textures/gui/buttons/redstonelow.png");
        redstoneTextures[2] = new ResourceLocation(TradingStation.MODID, "textures/gui/buttons/redstonehigh.png");
        buttons.put("redstoneMode", new ToggleButton(getGuiLeft() + getRedstoneButtonCoords().x, getGuiTop() + getRedstoneButtonCoords().y, 16, 16, redstoneTextures, currentRedstoneMode, (button) -> {
            currentRedstoneMode = (byte) (currentRedstoneMode == 2 ? 0 : currentRedstoneMode + 1);
            ModMessages.sendToServer(new RedstoneModeSyncC2SPacket(currentRedstoneMode,this.menu.getBlockPos()));
            //((ToggleButton) button).setTexturePosition(currentRedstoneMode);
        }));
    }

    private void addTargetSelectButton(){
        buttons.put("targetSelect", new ExtendedButton(leftPos + getTargetSelectButtonCoords().x,topPos + getTargetSelectButtonCoords().y,16,16, ModLang.translate("select_target.button"), btn ->{
            TradingStationTargetSelectScreen screen = new TradingStationTargetSelectScreen( this.menu.blockEntity, this.menu.getBlockPos());
            Minecraft.getInstance().pushGuiLayer(screen);

        }));
    }
}
