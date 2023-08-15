package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import com.oierbravo.trading_station.content.trading_station.TradingStationScreen;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {
    public static final MenuEntry<TradingStationMenu> TRADING_STATION =  TradingStation.registrate()
            .menu("trading_station",TradingStationMenu::factory, () -> TradingStationScreen::new)
            .register();

    public static final MenuEntry<TradingStationMenu> TRADING_STATION_TRADE_SELECT =  TradingStation.registrate()
            .menu("trading_station_trade_select",TradingStationMenu::factory, () -> TradingStationScreen::new)
            .register();
    public static void register() {

    }
}
