package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.*;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class TradingStationRegistrate {
    private static final Registrate REGISTRATE = TradingStation.registrate()
            .creativeModeTab(() ->  ModCreativeTab.MAIN);

    public static final BlockEntry<TradingStationBlock> TRADING_STATION_BLOCK = TradingStation.registrate()
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
    public static final BlockEntityEntry<TradingStationBlockEntity> TRADING_STATION_BLOCK_ENTITY = REGISTRATE
            .blockEntity("trading_station", TradingStationBlockEntity::new)
            .validBlocks(TRADING_STATION_BLOCK)
            .renderer(() -> TradingStationBlockRenderer::new)
            .register();

    public static final MenuEntry<TradingStationMenu> TRADING_STATION_MENU =  REGISTRATE
            .menu("trading_station",TradingStationMenu::factory, () -> TradingStationScreen::new)
            .register();

    public static void register() {

    }
}