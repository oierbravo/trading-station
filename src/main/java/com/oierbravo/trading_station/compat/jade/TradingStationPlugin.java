package com.oierbravo.trading_station.compat.jade;

import com.oierbravo.mechanicals.compat.jade.MechanicalProgressComponentProvider;
import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class TradingStationPlugin implements IWailaPlugin {
    public static final ResourceLocation TRADING_STATION_DATA = ModConstants.asResource("data");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new MechanicalProgressComponentProvider(TRADING_STATION_DATA), TradingStationBlockEntity.class);
        registration.registerBlockDataProvider(new MechanicalProgressComponentProvider(TRADING_STATION_DATA), PoweredTradingStationBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new MechanicalProgressComponentProvider(TRADING_STATION_DATA), TradingStationBlock.class);
        registration.registerBlockComponent(new MechanicalProgressComponentProvider(TRADING_STATION_DATA), PoweredTradingStationBlock.class);
    }
}
