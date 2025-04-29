package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class ModBlocks {
    private static final Registrate REGISTRATE = TradingStation.registrate();//.defaultCreativeTab(MechanicalLemonLib);
    public static final BlockEntry<TradingStationBlock> TRADING_STATION = TradingStation.registrate()
            .block("trading_station", TradingStationBlock::new)
            .lang("Trading Station")
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .blockstate((ctx, prov) ->
                    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                        String modelFileName = "trading_station:block/trading_station";
                        if(state.getValue(BlockStateProperties.POWERED))
                            modelFileName += "_powered";
                        if(state.getValue(BlockStateProperties.LIT))
                            modelFileName += "_lit";
                        return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(ResourceLocation.tryParse(modelFileName)))
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build();

                    })
            )
            .simpleItem()
            .blockEntity(TradingStationBlockEntity::new)
            .build()
            .register();
    public static final BlockEntry<TradingStationBlock> TRADING_STATION_UNBREAKABLE = TradingStation.registrate()
            .block("trading_station_unbreakable", TradingStationBlock::new)
            .lang("Trading Station (Unbreakable)")
            .properties((ctx)-> ctx.strength(-1.0f, 3_600_000.0f))
            .blockstate((ctx, prov) ->
                    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                        String modelFileName = "trading_station:block/trading_station";
                        if(state.getValue(BlockStateProperties.POWERED))
                            modelFileName += "_powered";
                        if(state.getValue(BlockStateProperties.LIT))
                            modelFileName += "_lit";
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
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
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
    public static final BlockEntry<PoweredTradingStationBlock> POWERED_TRADING_STATION_UNBREAKABLE = TradingStation.registrate()
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


    public static void register() {

    }
}
