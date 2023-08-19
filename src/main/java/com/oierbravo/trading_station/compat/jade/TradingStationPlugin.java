package com.oierbravo.trading_station.compat.jade;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;

@WailaPlugin
public class TradingStationPlugin implements IWailaPlugin {
    public static final ResourceLocation TRADING_STATION_DATA = TradingStation.asResource("trading_station_data");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ProgressComponentProvider(), TradingStationBlockEntity.class);
        //registration.registerBlockDataProvider(new ProgressComponentProvider(), PoweredTradingStationBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ProgressComponentProvider(), TradingStationBlock.class);
        //registration.registerBlockComponent(new ProgressComponentProvider(), PoweredTradingStationBlock.class);
    }
}
