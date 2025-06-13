package com.oierbravo.trading_station.content.trading_station;

import com.mojang.blaze3d.systems.RenderSystem;
import com.oierbravo.mechanicals.foundation.gui.MechanicalIcons;
import com.oierbravo.mechanicals.foundation.gui.widget.IconButton;
import com.oierbravo.trading_station.ModLang;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.foundation.component.TradingRecipeComponent;
import com.oierbravo.trading_station.network.packets.data.RecipeClearSyncPayload;
import com.oierbravo.trading_station.network.packets.data.RecipeSelectSyncPayload;
import com.oierbravo.trading_station.registrate.ModGuiTextures;
import com.oierbravo.trading_station.registrate.ModItemComponents;
import com.oierbravo.trading_station.registrate.ModMessages;
import com.oierbravo.trading_station.registrate.ModRecipes;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * Scroll code and loop adapted from Alchemistry/src/main/java/com/smashingmods/alchemistry/client/container/RecipeSelectorScreen.java
 */
public class TradingStationTargetSelectScreen extends AbstractSimiScreen {

    private static final ModGuiTextures BG = ModGuiTextures.TRADING_SELECT;
    private ITradingStationBlockEntity blockEntity;
    private List<RecipeHolder<TradingRecipe>> allPossibleRecipes;
    private LinkedList<RecipeHolder<TradingRecipe>> displayedRecipes = new LinkedList<>();


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

    private IconButton backButton;
    private IconButton clearButton;

    protected TradingStationTargetSelectScreen(Component pTitle) {
        super(pTitle);
    }
    public TradingStationTargetSelectScreen(ITradingStationBlockEntity pBlockEntity, BlockPos pBlockPos) {
        this(ModLang.translate("select_target.title").component());
        this.blockEntity = pBlockEntity;
        this.blockPos = pBlockPos;
        this.allPossibleRecipes = ModRecipes.getAllRecipesForMachine(pBlockEntity.getLevel(), (BlockEntity) pBlockEntity);
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

        backButton = new IconButton(getGuiLeft() - 20, getGuiTop() + 1, MechanicalIcons.ARROW_LEFT);
        backButton.withCallback(() -> {
            Minecraft.getInstance().popGuiLayer();
        });
        backButton.setToolTip(ModLang.translate("select_target.back").component());
        addRenderableWidget(backButton);

        clearButton = new IconButton(getGuiLeft() - 20, getGuiTop() + 30, MechanicalIcons.TRASH);
        clearButton.withCallback(() -> {
            ModMessages.sendToServer(new RecipeClearSyncPayload(getBlockPos()));
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
            Minecraft.getInstance().popGuiLayer();
        });
        clearButton.setToolTip(ModLang.translate("select_target.clear").component());
        addRenderableWidget(clearButton);

    }

    public int getGuiLeft() { return leftPos - 25; }
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
    protected void renderWindowForeground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int lastDisplayedIndex = startIndex + MAX_DISPLAYED_RECIPES;

        renderScrollbar(pGuiGraphics);
        renderSelectedRecipe(pGuiGraphics, pMouseX, pMouseY, lastDisplayedIndex);
        renderRecipe(pGuiGraphics, pMouseX, pMouseY, lastDisplayedIndex);
        renderLabels(pGuiGraphics, pMouseX, pMouseY);

        super.renderWindowForeground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void renderWindowBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {

    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font,this.title.getString(), (float)this.titleLabelX + getGuiLeft(), (float)this.titleLabelY + getGuiTop(), 4210752,false);
    }
    private void renderSelectedRecipe(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int pLastDisplayedIndex) {
        LinkedList<RecipeHolder<TradingRecipe>> displayedRecipes = getDisplayedRecipes();
        for (int index = startIndex; index >= 0 && index < pLastDisplayedIndex && index < displayedRecipes.size(); index++) {

            int firstDisplayedIndex = index - startIndex;
            ResourceLocation targetId = allPossibleRecipes.get(index).id();
            int xStart = getGuiLeft() + targetBoxLeftPosOffset + firstDisplayedIndex % COLUMNS * TARGET_BOX_SIZE + 1;
            int yStart = getGuiTop() + targetBoxTopPosOffset + (firstDisplayedIndex / COLUMNS) * TARGET_BOX_SIZE + 3;

            if(targetId.toString().equals(this.blockEntity.getTargetedRecipeId()))
                ModGuiTextures.TRADING_SELECT_SELECTED.render(pGuiGraphics, xStart, yStart);
        }
    }
    private void renderRecipe(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int pLastDisplayedIndex) {
        LinkedList<RecipeHolder<TradingRecipe>> displayedRecipes = getDisplayedRecipes();

        for (int index = startIndex; index >= 0 && index < pLastDisplayedIndex && index < displayedRecipes.size(); index++) {

            int firstDisplayedIndex = index - startIndex;
            TradingRecipe target = allPossibleRecipes.get(index).value();
            ResourceLocation targetId = allPossibleRecipes.get(index).id();

            int xStart = getGuiLeft() + targetBoxLeftPosOffset + firstDisplayedIndex % COLUMNS * TARGET_BOX_SIZE + 1;
            int yStart = getGuiTop() + targetBoxTopPosOffset + (firstDisplayedIndex / COLUMNS) * TARGET_BOX_SIZE + 3;
            renderFloatingItem(pGuiGraphics, target.getResult(), xStart, yStart );

            if (pMouseX >= xStart - 1 && pMouseX <= xStart + 16 && pMouseY >= yStart - 1 && pMouseY <= yStart + 16) {
                ItemStack result = target.getResult();
                result.set(ModItemComponents.TRADING_RECIPE_ID, new TradingRecipeComponent(targetId));
                pGuiGraphics.renderTooltip(this.font,result, pMouseX, pMouseY);
            }
        }
    }

    private LinkedList<RecipeHolder<TradingRecipe>> getDisplayedRecipes() {
        return displayedRecipes;
    }

    private void renderFloatingItem(GuiGraphics pGuiGraphics,ItemStack pItemStack, int pX, int pY) {
        RenderSystem.applyModelViewMatrix();
        pGuiGraphics.renderFakeItem(pItemStack, pX, pY);
    }


    public void renderBackground(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        pGuiGraphics.fillGradient( 0, 0, this.width, this.height, -1072689136, -804253680);
        BG.render(pGuiGraphics, getGuiLeft(),getGuiTop());
    }

    private void renderScrollbar(GuiGraphics pGuiGraphics) {
        int scrollPosition = (int) (43.0f * scrollOffset);
        if(isScrollBarActive())
            ModGuiTextures.TRADING_SELECT_SCROLL_ACTIVE.render(pGuiGraphics,getGuiLeft() + scrollBarXOffset,getGuiTop() + scrollBarYOffset + scrollPosition);
        else
            ModGuiTextures.TRADING_SELECT_SCROLL_INACTIVE.render(pGuiGraphics,getGuiLeft() + scrollBarXOffset,getGuiTop() + scrollBarYOffset + scrollPosition);
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
                ResourceLocation recipeId = getDisplayedRecipes().get(index).id();
                ModMessages.sendToServer(new RecipeSelectSyncPayload(recipeId,getBlockPos()));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                Minecraft.getInstance().popGuiLayer();
                return true;
            }

            int scrollMinX = getGuiLeft() + 148;
            int scrollMinY = getGuiTop() + 9;
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

    //@Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        //if (pMouseX >= leftPos && pMouseX < leftPos + imageWidth && pMouseY >= topPos && pMouseY < topPos + imageHeight && isScrollBarActive()) {
        if (pMouseX >= getGuiLeft() && pMouseX < getGuiLeft() + imageWidth && pMouseY >= getGuiTop() && pMouseY < getGuiTop() + imageHeight && isScrollBarActive()) {
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
