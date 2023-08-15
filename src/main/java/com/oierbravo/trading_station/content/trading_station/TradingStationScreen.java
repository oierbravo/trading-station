package com.oierbravo.trading_station.content.trading_station;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.Optional;

public class TradingStationScreen extends AbstractContainerScreen<TradingStationMenu> {
    private static final ResourceLocation TEXTURE = TradingStation.asResource("textures/gui/trading_station.png");

    private int progressArrowX = 79;
    private int progressArrowY = 47;

    private int targetSelectButtonX = 131;
    private int targetSelectButtonY = 18;

    public static int inputSlotX[] = {19,42};
    public static int inputSlotY = 38;
    public static int outputSlotX = 131;
    public static int outputSlotY = 38;

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 4;
        this.titleLabelY = 4;
        this.inventoryLabelY = 100000;
        this.addRenderableWidget(new ExtendedButton(leftPos + targetSelectButtonX,topPos + targetSelectButtonY,16,16, ModLang.translate("select_target.button"), btn ->{
            TradingStationTargetSelectScreen screen = new TradingStationTargetSelectScreen( this.menu.blockEntity);
            Minecraft.getInstance().pushGuiLayer(screen);

        }));
    }

    public TradingStationScreen(TradingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.fillGradient(pPoseStack, 0, 0, this.width, this.height, -1072689136, -804253680);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            this.blit(pPoseStack, x + progressArrowX, y + progressArrowY, 179, 0, menu.getScaledProgress(), 7);
        }

        if(!menu.blockEntity.getPreferedItemStack().isEmpty()){
            Optional<TradingRecipe> recipe = ModRecipes.findByOutput(menu.blockEntity.getLevel(),menu.blockEntity.getPreferedItemStack());
            if(recipe.isPresent()){
                renderFakeRecipe(recipe.get(), pPoseStack);
            }
        }
    }
    private void renderFakeRecipe(TradingRecipe recipe, PoseStack pPoseStack){
        for(int index = 0; index < recipe.getIngredients().size(); index++){
            Ingredient ingredient = recipe.getIngredients().get(index);
            if(!ingredient.isEmpty())
                FakeItemRenderer.renderFakeItem(ingredient.getItems()[0],getGuiLeft() + TradingStationScreen.inputSlotX[index],getGuiTop() + TradingStationScreen.inputSlotY, .5f);
        }
        FakeItemRenderer.renderFakeItem(recipe.getResultItem(),getGuiLeft() +  TradingStationScreen.outputSlotX,getGuiTop() +  TradingStationScreen.outputSlotY, .5f);

    }


    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

}