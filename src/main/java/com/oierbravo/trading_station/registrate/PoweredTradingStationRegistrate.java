package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
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
    public static final BlockEntry<PoweredTradingStationBlock> BLOCK = TradingStation.registrate()
            .block("powered_trading_station", PoweredTradingStationBlock::new)
            .lang("Powered Trading Station")
            .blockstate((ctx, prov) ->
                    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                        String modelFileName = "trading_station:block/powered_trading_station";
                        if(state.getValue(BlockStateProperties.POWERED))
                            modelFileName += "_powered";
                        if(state.getValue(BlockStateProperties.LIT))
                            modelFileName += "_lit";
                        return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(ResourceLocation.tryParse(modelFileName)))
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build();

                    })
            )
            .simpleItem()
            .blockEntity(PoweredTradingStationBlockEntity::new)
            .build()
            .register();
    public static final BlockEntry<PoweredTradingStationBlock> BLOCK_UNBREAKABLE = TradingStation.registrate()
            .block("powered_trading_station_unbreakable", PoweredTradingStationBlock::new)
            .lang("Powered Trading Station (Unbreakable)")
            .properties((ctx)-> ctx.strength(-1.0f, 3_600_000.0f))
            .blockstate((ctx, prov) ->
                    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                        String modelFileName = "trading_station:block/powered_trading_station";
                        if(state.getValue(BlockStateProperties.POWERED))
                            modelFileName += "_powered";
                        if(state.getValue(BlockStateProperties.LIT))
                            modelFileName += "_lit";
                        return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(ResourceLocation.tryParse(modelFileName)))
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build();

                    })
            )
            .simpleItem()
            .blockEntity(PoweredTradingStationBlockEntity::new)
            .build()
            .register();

    public static final BlockEntityEntry<PoweredTradingStationBlockEntity> BLOCK_ENTITY = TradingStation.registrate()
            .blockEntity("powered_trading_station", PoweredTradingStationBlockEntity::new)
            .validBlocks(BLOCK, BLOCK_UNBREAKABLE)
            .renderer(() -> TradingStationBlockRenderer::new)
            .register();

    public static final MenuEntry<PoweredTradingStationMenu> MENU =  TradingStation.registrate()
            .menu("powered_trading_station", PoweredTradingStationMenu::factory, () -> PoweredTradingStationScreen::new)
            .register();
    public static void register() {

    }
}
