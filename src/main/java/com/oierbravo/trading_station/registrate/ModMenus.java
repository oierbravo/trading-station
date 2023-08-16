package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import com.oierbravo.trading_station.content.trading_station.TradingStationScreen;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationMenu;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationScreen;
import com.tterrag.registrate.util.entry.MenuEntry;

public class ModMenus {
    public static final MenuEntry<TradingStationMenu> TRADING_STATION =  TradingStation.registrate()
            .menu("trading_station",TradingStationMenu::factory, () -> TradingStationScreen::new)
            .register();
    public static final MenuEntry<PoweredTradingStationMenu> POWERED_TRADING_STATION =  TradingStation.registrate()
            .menu("powered_trading_station", PoweredTradingStationMenu::factory, () -> PoweredTradingStationScreen::new)
            .register();

    public static void register() {

    }
}
