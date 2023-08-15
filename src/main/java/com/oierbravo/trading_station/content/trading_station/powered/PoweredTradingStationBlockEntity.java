package com.oierbravo.trading_station.content.trading_station.powered;

import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredTradingStationBlockEntity extends TradingStationBlockEntity {
    public PoweredTradingStationBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }
}
