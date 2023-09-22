package com.oierbravo.trading_station.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Copyright: Direwolf20
 * Source: https://github.com/Direwolf20-MC/LaserIO
 * License: MIT
 */
public class ToggleButton extends Button {
    private ResourceLocation[] textures;
    private int texturePosition;

    public ToggleButton(int x, int y, int width, int height, ResourceLocation[] textures, int texturePosition, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);

        this.textures = textures;
        setTexturePosition(texturePosition);
    }

    /*public ToggleButton(int x, int y, int width, int height, ResourceLocation[] textures, int texturePosition, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, width, height, Component.empty(), onPress, onTooltip);

        this.textures = textures;
        setTexturePosition(texturePosition);
    }*/

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        //fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, 0xFFa8a8a8);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, textures[texturePosition]);
        guiGraphics.blit(textures[texturePosition], this.getX(), this.getY(), 0, 0, width, height, width, height);
    }

    /*@Override
    public void renderToolTip(PoseStack stack, int x, int y) {
        super.renderToolTip(stack, x, y);
    }*/

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        super.onClick(p_onClick_1_, p_onClick_3_);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return super.mouseClicked(x, y, button);
    }

    /*@Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }*/

    public int getTexturePosition() {
        return texturePosition;
    }

    public void setTexturePosition(int texturePosition) {
        if (texturePosition > textures.length)
            this.texturePosition = textures.length;
        else
            this.texturePosition = texturePosition;
    }

    public void nextTexturePosition() {
        if (texturePosition == textures.length)
            texturePosition = 0;
        else
            texturePosition++;
    }
}