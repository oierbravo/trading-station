package com.oierbravo.trading_station.infrastructure.config;

import com.oierbravo.trading_station.content.trading_station.TradingStationConfig;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationConfig;
import net.createmod.catnip.config.ConfigBase;

public class ModConfigServer extends ConfigBase {
    public final TradingStationConfig tradingStation = nested(0, TradingStationConfig::new, "Trading station");
    public final PoweredTradingStationConfig poweredTradingStation = nested(0, PoweredTradingStationConfig::new, "Powered Trading station");

    @Override
    public String getName() {
        return "server";
    }
}
