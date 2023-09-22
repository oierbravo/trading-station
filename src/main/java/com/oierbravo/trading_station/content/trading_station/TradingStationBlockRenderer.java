package com.oierbravo.trading_station.content.trading_station;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class  TradingStationBlockRenderer<TSBE extends BlockEntity> implements BlockEntityRenderer<TSBE> {
    public TradingStationBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(pBlockEntity instanceof ITradingStationBlockEntity) {
            ITradingStationBlockEntity blockEntity = (ITradingStationBlockEntity) pBlockEntity;
            if (!blockEntity.getTargetItemStack().isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5d, 1.1d, 0.5d);
                renderBlock(pPoseStack, pBufferSource, LightTexture.FULL_BRIGHT, pPackedOverlay, blockEntity.getTargetItemStack(),pBlockEntity);
                pPoseStack.popPose();
            }
        }
    }
    protected void renderBlock(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack, BlockEntity pBlockEntity) {
        Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer,pBlockEntity.getLevel(), 0);
    }
}
