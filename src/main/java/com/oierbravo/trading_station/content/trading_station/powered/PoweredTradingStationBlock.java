package com.oierbravo.trading_station.content.trading_station.powered;

import com.oierbravo.trading_station.content.trading_station.TradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.registrate.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PoweredTradingStationBlock extends TradingStationBlock {
    public PoweredTradingStationBlock(Properties pProperties) {
        super(pProperties);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModBlockEntities.POWERED_TRADING_STATION.create(pPos, pState);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.POWERED_TRADING_STATION.get(),
                TradingStationBlockEntity::tick);
    }
}
