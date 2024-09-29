package com.oierbravo.trading_station.content.trading_station;

import com.google.common.collect.ImmutableList;
import com.oierbravo.mechanical_lemon_ui.foundation.gui.menu.AbstractSimiContainerScreen;
import com.oierbravo.mechanical_lemon_ui.foundation.gui.widget.EnergyDisplay;
import com.oierbravo.mechanical_lemon_ui.foundation.gui.widget.IconButton;
import com.oierbravo.mechanical_lemon_ui.foundation.gui.widget.ProgressArrow;
import com.oierbravo.mechanical_lemon_ui.foundation.gui.widget.ToggleIconButton;
import com.oierbravo.mechanical_lemon_ui.foundation.utility.FakeItemRenderer;
import com.oierbravo.mechanical_lemon_ui.foundation.utility.ScreenElement;
import com.oierbravo.mechanical_lemon_ui.register.LibGuiTextures;
import com.oierbravo.mechanical_lemon_ui.register.LibIcons;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.network.packets.RedstoneModeSyncC2SPacket;
import com.oierbravo.trading_station.registrate.ModGuiTextures;
import com.oierbravo.trading_station.registrate.ModMessages;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Optional;

public class TradingStationScreen extends AbstractSimiContainerScreen<TradingStationMenu> {
    private static final ModGuiTextures BG = ModGuiTextures.TRADING_STATION;
    protected static final LibGuiTextures PLAYER = LibGuiTextures.PLAYER_INVENTORY;

    protected int imageWidth = 176;
    protected int imageHeight = 176;

    protected byte currentRedstoneMode;


    private IconButton confirmButton;
    private IconButton targetButton;
    private ToggleIconButton redstoneButton;

    private EnergyDisplay energyInfoArea;

    private ProgressArrow progressArrow;


    public TradingStationScreen(TradingStationMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        init();
    }



    @Override
    protected void init() {
        setWindowSize(30 + BG.width, BG.height + PLAYER.height - 24);
        setWindowOffset(-11, 0);
        super.init();
        clearWidgets();


        confirmButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - 24, LibIcons.CHECK);
        confirmButton.withCallback(() -> {
            minecraft.player.closeContainer();
        });
        confirmButton.setToolTip(ModLang.translate("confirm.button").component());

        addRenderableWidget(confirmButton);

        targetButton = new IconButton(leftPos + BG.width/2 -2, topPos + BG.height - 10, LibIcons.SEARCH);
        targetButton.withCallback(() -> {

            TradingStationTargetSelectScreen screen = new TradingStationTargetSelectScreen( this.menu.contentHolder, this.menu.contentHolder.getBlockPos());
            Minecraft.getInstance().pushGuiLayer(screen);
        });

        targetButton.setToolTip(ModLang.translate("select_target.button").component());

        addRenderableWidget(targetButton);

        ScreenElement[] redstoneButtonIcons = new ScreenElement[3];
        redstoneButtonIcons[0] = LibIcons.REDSTONE_IGNORE;
        redstoneButtonIcons[1] = LibIcons.REDSTONE_LOW;
        redstoneButtonIcons[2] = LibIcons.REDSTONE_HIGH;

        MutableComponent[] redstoneButtonLabels = new MutableComponent[3];
        redstoneButtonLabels[0] = ModLang.translate("screen.redstone.ignored").component();
        redstoneButtonLabels[1] = ModLang.translate("screen.redstone.low").component();
        redstoneButtonLabels[2] = ModLang.translate("screen.redstone.high").component();

        currentRedstoneMode = this.menu.contentHolder.getCurrentRedstoneMode();
        redstoneButton = new ToggleIconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - 40,redstoneButtonIcons,redstoneButtonLabels, currentRedstoneMode);
        redstoneButton.withCallback(() -> {
            currentRedstoneMode = (byte) (currentRedstoneMode == 2 ? 0 : currentRedstoneMode + 1);
            this.menu.contentHolder.setRedstoneMode(currentRedstoneMode);
            redstoneButton.setCurrentIndex(currentRedstoneMode);

            ModMessages.sendToServer(new RedstoneModeSyncC2SPacket(currentRedstoneMode,this.menu.contentHolder.getBlockPos()));


        });
        //redstoneButton.setToolTip(ModLang.translate("screen.redstone.redstoneMode"));
        addRenderableWidget(redstoneButton);

        this.menu.contentHolder.getEnergyStorageHandler().ifPresent((iEnergyStorage -> {
            energyInfoArea = new EnergyDisplay(leftPos + 30 + BG.width - 50, topPos + BG.height - 40, iEnergyStorage);
            addRenderableWidget(energyInfoArea);
        }));


        progressArrow = new ProgressArrow(leftPos + BG.width/2 - 8,topPos + BG.height/2 + 13);
        addRenderableWidget(progressArrow);



    }

    @Override
    protected void containerTick() {
        progressArrow.setProgress(this.menu.containerData.get(0),this.menu.containerData.get(1));
        //progressArrow.setProgress(this.menu.contentHolder.getProgressPercent(),this.menu.contentHolder.getProcessingTime());
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = leftPos + imageWidth - BG.width;
        int y = topPos;

        BG.render(pGuiGraphics, x, y);
        pGuiGraphics.drawString(font, title, x + 15, y + 4, 0x592424, false);

        int invX = leftPos;
        int invY = topPos + imageHeight - PLAYER.height;
        renderPlayerInventory(pGuiGraphics, invX, invY);

        if(!menu.contentHolder.getTargetItemStack().isEmpty()){
            //Optional<TradingRecipe> recipe = ModRecipes.findByOutput(menu.contentHolder.getLevel(), menu.contentHolder.getTargetItemStack());
            Optional<TradingRecipe> recipe = menu.contentHolder.getTargetedRecipe();
            if(recipe.isPresent()){
                for(int index = 0; index < recipe.get().getIngredients().size(); index++){
                    Ingredient ingredient = recipe.get().getIngredients().get(index);

                    if(!ingredient.isEmpty()) {
                        FakeItemRenderer.renderFakeItem(pGuiGraphics,ingredient.getItems()[0], leftPos + 20 + 23*index, topPos + 38, true,false);
                    }
                }
            }
        }
    }
}
