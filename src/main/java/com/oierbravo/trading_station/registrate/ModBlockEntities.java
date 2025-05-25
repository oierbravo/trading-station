package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockRenderer;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {
    private static final Registrate REGISTRATE = TradingStation.registrate();//.defaultCreativeTab(MechanicalLemonLib);


    public static final BlockEntityEntry<TradingStationBlockEntity> TRADING_STATION_BLOCK_ENTITY = REGISTRATE
            .blockEntity("trading_station", TradingStationBlockEntity::new)
            .validBlocks(ModBlocks.TRADING_STATION, ModBlocks.TRADING_STATION_UNBREAKABLE)
            .renderer(() -> TradingStationBlockRenderer::new)
            .register();

    public static final BlockEntityEntry<PoweredTradingStationBlockEntity> POWERED_TRADING_STATION_BLOCK_ENTITY = TradingStation.registrate()
            .blockEntity("powered_trading_station", PoweredTradingStationBlockEntity::new)
            .validBlocks(ModBlocks.POWERED_TRADING_STATION, ModBlocks.POWERED_TRADING_STATION_UNBREAKABLE)
            .renderer(() -> TradingStationBlockRenderer::new)
            .register();

    public static void register() {

    }
}
