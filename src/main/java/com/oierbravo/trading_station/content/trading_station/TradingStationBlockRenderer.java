package com.oierbravo.trading_station.content.trading_station;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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
            if (blockEntity.getRecipe().isPresent()) {
                TradingRecipe recipe = (TradingRecipe)  blockEntity.getRecipe().get().value();
                pPoseStack.pushPose();
                pPoseStack.translate(.5, .5, .5);
                float rot = 0F;
                if(blockEntity.isWorking()){
                    rot = ((blockEntity.getLevel().getGameTime() + pPartialTick) % 90F) * 8;
                }
                pPoseStack.mulPose(Axis.YP.rotationDegrees(rot));
                pPoseStack.translate(0, .7d, 0);

                renderBlock(pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, recipe.getResult(),pBlockEntity);
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
