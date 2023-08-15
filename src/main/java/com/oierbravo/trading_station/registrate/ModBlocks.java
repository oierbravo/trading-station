package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class ModBlocks {
    private static final Registrate REGISTRATE = TradingStation.registrate()
            .creativeModeTab(() ->  ModCreativeTab.MAIN);
    public static final BlockEntry<TradingStationBlock> TRADING_STATION = TradingStation.registrate()
            .block("trading_station", TradingStationBlock::new)
            .lang("Trading Station")
            .blockstate((ctx, prov) ->
                    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                        String modelFileName = "trading_station:block/trading_station";
                        if(state.getValue(BlockStateProperties.POWERED))
                            modelFileName += "_powered";
                        return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(ResourceLocation.tryParse(modelFileName)))
                                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build();

                    })
            )
            .simpleItem()
            .blockEntity(TradingStationBlockEntity::new)
            .build()
            .register();
    public static final BlockEntry<PoweredTradingStationBlock> POWERED_TRADING_STATION = TradingStation.registrate()
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
    public static void register() {

    }
}
