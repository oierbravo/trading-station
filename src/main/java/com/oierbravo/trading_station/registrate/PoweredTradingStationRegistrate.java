package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockRenderer;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationMenu;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationScreen;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class PoweredTradingStationRegistrate {
    public static final BlockEntry<PoweredTradingStationBlock> POWERED_TRADING_STATION_BLOCK = TradingStation.registrate()
            .block("powered_trading_station", PoweredTradingStationBlock::new)
            .lang("Powered Trading Station")
            .blockstate((ctx, prov) ->
                    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                        String modelFileName = "trading_station:block/powered_trading_station";
                        if(state.getValue(BlockStateProperties.POWERED))
                            modelFileName += "_powered";
                        return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(ResourceLocation.tryParse(modelFileName)))
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build();

                    })
            )
            .simpleItem()
            .blockEntity(PoweredTradingStationBlockEntity::new)
            .build()
            .register();

    public static final BlockEntityEntry<PoweredTradingStationBlockEntity> POWERED_TRADING_STATION_BLOCK_ENTITY = TradingStation.registrate()
            .blockEntity("powered_trading_station", PoweredTradingStationBlockEntity::new)
            .validBlocks(POWERED_TRADING_STATION_BLOCK)
            .renderer(() -> TradingStationBlockRenderer::new)
            .register();

    public static final MenuEntry<PoweredTradingStationMenu> POWERED_TRADING_STATION_MENU =  TradingStation.registrate()
            .menu("powered_trading_station", PoweredTradingStationMenu::factory, () -> PoweredTradingStationScreen::new)
            .register();
    public static void register() {

    }
}
