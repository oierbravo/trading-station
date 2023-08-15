package com.oierbravo.trading_station.compat.jade;

import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;

public class ProgressComponentProvider  implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //CompoundTag serverData = accessor.getServerData();
        if (accessor.getServerData().contains("trading_station.progress")) {
            int progress = accessor.getServerData().getInt("trading_station.progress");
            IElementHelper elementHelper = tooltip.getElementHelper();
            IProgressStyle progressStyle = elementHelper.progressStyle();
            if(progress > 0)
                tooltip.add(elementHelper.progress((float)progress / 100, Component.translatable("trading_station.tooltip.progress", progress), progressStyle,elementHelper.borderStyle()));
        }

    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if(blockEntity instanceof TradingStationBlockEntity){
            TradingStationBlockEntity trading_station = (TradingStationBlockEntity) blockEntity;
            compoundTag.putInt("trading_station.progress",trading_station.getProgressPercent());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return TradingStationPlugin.TRADING_STATION_DATA;
    }
}
