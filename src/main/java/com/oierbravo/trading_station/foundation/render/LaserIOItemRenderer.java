package com.oierbravo.trading_station.foundation.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;

/** This class is used to make the numbers on items in the FilterCountContainer smaller when greater than 100 **/
public class LaserIOItemRenderer extends ItemRenderer {
    public LaserIOItemRenderer(Minecraft minecraft, TextureManager textureManager, ModelManager modelManager, ItemColors itemColors, BlockEntityWithoutLevelRenderer blockEntityWithoutLevelRenderer) {
        super(minecraft, textureManager, modelManager, itemColors, blockEntityWithoutLevelRenderer);
    }

    private void fillRect(BufferBuilder p_115153_, int p_115154_, int p_115155_, int p_115156_, int p_115157_, int p_115158_, int p_115159_, int p_115160_, int p_115161_) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        p_115153_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        p_115153_.vertex((double) (p_115154_ + 0), (double) (p_115155_ + 0), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        p_115153_.vertex((double) (p_115154_ + 0), (double) (p_115155_ + p_115157_), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        p_115153_.vertex((double) (p_115154_ + p_115156_), (double) (p_115155_ + p_115157_), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        p_115153_.vertex((double) (p_115154_ + p_115156_), (double) (p_115155_ + 0), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        //p_115153_.end();
        BufferUploader.drawWithShader(p_115153_.end());
    }


}
