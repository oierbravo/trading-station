package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockRenderer;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {
    public static final BlockEntityEntry<TradingStationBlockEntity> TRADING_STATION = TradingStation.registrate()
            .blockEntity("trading_station", TradingStationBlockEntity::new)
            .validBlocks(ModBlocks.TRADING_STATION)
            .renderer(() -> TradingStationBlockRenderer::new)
            .register();

    public static final BlockEntityEntry<PoweredTradingStationBlockEntity> POWERED_TRADING_STATION = TradingStation.registrate()
            .blockEntity("powered_trading_station", PoweredTradingStationBlockEntity::new)
            .validBlocks(ModBlocks.POWERED_TRADING_STATION)
            .renderer(() -> TradingStationBlockRenderer::new)
            .register();
    public static void register() {

    }
}
