package com.oierbravo.trading_station.content.trading_station;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.network.packets.GhostItemSyncC2SPacket;
import com.oierbravo.trading_station.network.packets.RecipeSelectC2SPacket;
import com.oierbravo.trading_station.registrate.ModMessages;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

/**
 * Scroll code and loop adapted from Alchemistry/src/main/java/com/smashingmods/alchemistry/client/container/RecipeSelectorScreen.java
 */
public class TradingStationTargetSelectScreen extends Screen {
    private static final ResourceLocation TEXTURE = TradingStation.asResource("textures/gui/trade_select.png");
    private ITradingStationBlockEntity blockEntity;
    private List<TradingRecipe> allPossibleRecipes;
    private LinkedList<TradingRecipe> displayedRecipes = new LinkedList<>();


    private float scrollOffset;
    private boolean scrolling;
    private int startIndex;
    private static int MAX_DISPLAYED_RECIPES = 24;
    private static final int COLUMNS = 8;
    private static final int TARGET_BOX_SIZE = 17;

    private int targetBoxLeftPosOffset = 7;
    private  int targetBoxTopPosOffset = 19;

    private int scrollBarXOffset = 148;
    private int scrollBarYOffset = 22;

    protected int titleLabelX;
    protected int titleLabelY;
    protected int imageWidth = 165;
    protected int imageHeight = 83;
    protected int leftPos;
    protected int topPos;

    private BlockPos blockPos;
    protected TradingStationTargetSelectScreen(Component pTitle) {
        super(pTitle);
    }
    public TradingStationTargetSelectScreen(ITradingStationBlockEntity pBlockEntity, BlockPos pBlockPos) {
        this(ModLang.translate("select_target.title"));
        this.blockEntity = pBlockEntity;
        this.blockPos = pBlockPos;
        //this.allPossibleRecipes = ModRecipes.getAllOutputs(pBlockEntity.getLevel(),pBlockEntity.getBiome(),pBlockEntity.getTraderType());
        this.allPossibleRecipes = ModRecipes.getAllRecipesForMachine(pBlockEntity.getLevel(),pBlockEntity.getBiome(),pBlockEntity.getTraderType());
        resetDisplayedTargets();

    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 4;
        this.titleLabelY = 4;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        addRenderableWidget(new Button(getGuiLeft() - 25, getGuiTop() + 1, 25, 20, Component.literal("<--"), (button) -> {
            Minecraft.getInstance().popGuiLayer();
        }));
        addRenderableWidget(new Button(getGuiLeft() - 25, getGuiTop() + 30, 25, 20, ModLang.translate("select_target.clear"), (button) -> {
            ModMessages.sendToServer(new GhostItemSyncC2SPacket(ItemStack.EMPTY,getBlockPos()));
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
            Minecraft.getInstance().popGuiLayer();
        }));
    }

    public int getGuiLeft() { return leftPos; }
    public int getGuiTop() { return topPos; }
    @Override
    public void tick() {
            if (displayedRecipes.size() < MAX_DISPLAYED_RECIPES) {
                mouseScrolled(0, 0, 0);
                scrollOffset = 0.0f;
            }
            if (displayedRecipes.size() <= MAX_DISPLAYED_RECIPES) {
                startIndex = 0;
                scrollOffset = 0;
            }
        super.tick();
    }
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        renderBackground(pPoseStack);

        int lastDisplayedIndex = startIndex + MAX_DISPLAYED_RECIPES;

        renderScrollbar(pPoseStack);
        renderSelectedRecipe(pPoseStack, pMouseX, pMouseY, lastDisplayedIndex);
        renderRecipe(pPoseStack, pMouseX, pMouseY, lastDisplayedIndex);
        renderLabels(pPoseStack, pMouseX, pMouseY);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, this.title, (float)this.titleLabelX + getGuiLeft(), (float)this.titleLabelY + getGuiTop(), 4210752);
    }
    private void renderSelectedRecipe(PoseStack pPoseStack, int pMouseX, int pMouseY, int pLastDisplayedIndex) {
        LinkedList<TradingRecipe> displayedRecipes = getDisplayedRecipes();
        for (int index = startIndex; index >= 0 && index < pLastDisplayedIndex && index < displayedRecipes.size(); index++) {

            int firstDisplayedIndex = index - startIndex;
            TradingRecipe target = allPossibleRecipes.get(index);
            int xStart = getGuiLeft() + targetBoxLeftPosOffset + firstDisplayedIndex % COLUMNS * TARGET_BOX_SIZE + 1;
            int yStart = getGuiTop() + targetBoxTopPosOffset + (firstDisplayedIndex / COLUMNS) * TARGET_BOX_SIZE + 3;
            if(target.getId().toString().equals(this.blockEntity.getTargetedRecipeId()))
                blit(pPoseStack, xStart, yStart, 0, imageHeight + 19, TARGET_BOX_SIZE, TARGET_BOX_SIZE);
        }
    }
    private void renderRecipe(PoseStack pPoseStack, int pMouseX, int pMouseY, int pLastDisplayedIndex) {
        LinkedList<TradingRecipe> displayedRecipes = getDisplayedRecipes();

        for (int index = startIndex; index >= 0 && index < pLastDisplayedIndex && index < displayedRecipes.size(); index++) {

            int firstDisplayedIndex = index - startIndex;
            TradingRecipe target = allPossibleRecipes.get(index);
            int xStart = getGuiLeft() + targetBoxLeftPosOffset + firstDisplayedIndex % COLUMNS * TARGET_BOX_SIZE + 1;
            int yStart = getGuiTop() + targetBoxTopPosOffset + (firstDisplayedIndex / COLUMNS) * TARGET_BOX_SIZE + 3;
           // if(target.getId().toString().equals(this.blockEntity.getTargetedRecipeId())){
           //     blit(pPoseStack, xStart, yStart, 0, imageHeight + 19, TARGET_BOX_SIZE, TARGET_BOX_SIZE);
           // }
            renderFloatingItem(target.getResultItem(), xStart, yStart );

            if (pMouseX >= xStart - 1 && pMouseX <= xStart + 16 && pMouseY >= yStart - 1 && pMouseY <= yStart + 16) {
                renderTooltip(pPoseStack, target.getResultItem(), pMouseX, pMouseY);
            }
        }
    }

    private LinkedList<TradingRecipe> getDisplayedRecipes() {
        return displayedRecipes;
    }

    private void renderFloatingItem(ItemStack pItemStack, int pX, int pY) {
        RenderSystem.applyModelViewMatrix();
        setBlitOffset(2000);
        itemRenderer.blitOffset = 2000.0f;

        itemRenderer.renderAndDecorateItem(pItemStack, pX, pY);
        itemRenderer.renderGuiItemDecorations(font, pItemStack, pX, pY);

        setBlitOffset(0);
        itemRenderer.blitOffset = 0.0f;
    }


    @Override
    public void renderBackground(PoseStack pPoseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.fillGradient(pPoseStack, 0, 0, this.width, this.height, -1072689136, -804253680);
        this.blit(pPoseStack, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);
    }

    private void renderScrollbar(PoseStack pPoseStack) {
        int scrollPosition = (int) (43.0f * scrollOffset);
        blit(pPoseStack, getGuiLeft() + scrollBarXOffset, getGuiTop() + scrollBarYOffset + scrollPosition, 0, imageHeight + 1  + (isScrollBarActive() ? 0 : 9), 7, 9);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        scrolling = false;

        int lastDisplayedIndex = startIndex + MAX_DISPLAYED_RECIPES;

        for (int index = startIndex; index < lastDisplayedIndex; index++) {
            int currentIndex = index - startIndex;
            double boxX = pMouseX - (double)(getGuiLeft() + targetBoxLeftPosOffset + currentIndex % COLUMNS * TARGET_BOX_SIZE);
            double boxY = pMouseY - (double)(getGuiTop() + targetBoxTopPosOffset + currentIndex / COLUMNS * TARGET_BOX_SIZE);

            if (boxX > 0 && boxX <= TARGET_BOX_SIZE + 1 && boxY > 0 && boxY <= TARGET_BOX_SIZE + 1 && isValidRecipeIndex(index)) {
                TradingRecipe recipe = getDisplayedRecipes().get(index);
                //ModMessages.sendToServer(new GhostItemSyncC2SPacket(itemStack,getBlockPos()));
                ModMessages.sendToServer(new RecipeSelectC2SPacket(recipe.getId(),getBlockPos()));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                Minecraft.getInstance().popGuiLayer();
                return true;
            }

            int scrollMinX = leftPos + 148;
            int scrollMinY = topPos + 9;
            int scrollMaxX = scrollMinX + 6;
            int scrollMaxY = scrollMinY + 60;

            if (pMouseX >= scrollMinX && pMouseX < scrollMaxX && pMouseY >= scrollMinY && pMouseY < scrollMaxY) {
                scrolling = true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    private boolean isValidRecipeIndex(int pSlot) {
        return pSlot >= 0 && pSlot < getDisplayedRecipes().size();
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    public void resetDisplayedTargets() {
        this.displayedRecipes.clear();
        this.displayedRecipes.addAll(allPossibleRecipes);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (scrolling && isScrollBarActive()) {
            int scrollbarTopPos = getGuiTop() + 9;
            int scrollbarBottomPos = scrollbarTopPos + 51;
            scrollOffset = ((float) pMouseY - (float) scrollbarTopPos - 7.5f) / ((float) (scrollbarBottomPos - scrollbarTopPos) - 9.0f);
            scrollOffset = Mth.clamp(scrollOffset, 0.0f, 1.0f);
            startIndex = (int) ((double) (scrollOffset * (float) getOffscreenRows()) + 0.5d) * COLUMNS;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pMouseX >= leftPos && pMouseX < leftPos + imageWidth && pMouseY >= topPos && pMouseY < topPos + imageHeight && isScrollBarActive()) {
            scrollOffset = Mth.clamp(scrollOffset - (float) pDelta / (float) getOffscreenRows(), 0.0f, 1.0f);
            startIndex = (int) ((double) (scrollOffset * (float) getOffscreenRows()) + 0.5d) * COLUMNS;
        }
        return true;
    }
    private int getOffscreenRows() {
        return (displayedRecipes.size() + 6 - 1) / 6 - 3;
    }
    private boolean isScrollBarActive() {
        return displayedRecipes.size() > MAX_DISPLAYED_RECIPES;
    }

}
