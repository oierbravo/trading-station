package com.oierbravo.trading_station.content.trading_station.powered;

import com.oierbravo.mechanicals.foundation.energy.AbstractEnergyStorage;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlock;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.infrastructure.config.MConfigs;
import com.oierbravo.trading_station.registrate.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class PoweredTradingStationBlockEntity extends TradingStationBlockEntity {

    private final AbstractEnergyStorage energyStorage = createEnergyStorage();

    public PoweredTradingStationBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }
    private AbstractEnergyStorage createEnergyStorage() {
        return new AbstractEnergyStorage(MConfigs.server().poweredTradingStation.energyCapacity.get(), MConfigs.server().poweredTradingStation.energyTransfer.get()) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    @Override
    public IEnergyStorage getEnergyStorageHandler() {
        return energyStorage;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        invalidateCapabilities();
    }
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.POWERED_TRADING_STATION_BLOCK_ENTITY.get(),
                (be, context) -> {
                    Direction localDir = be.getBlockState().getValue(TradingStationBlock.HORIZONTAL_FACING);
                    if(context != null && localDir == context.getCounterClockWise())
                        return be.getInputItemHandler();
                    if(context != null && localDir == context.getClockWise())
                        return be.getOutputItemHandler();
                    if(context == null)
                        return null;
                    return null;
                }
        );
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.POWERED_TRADING_STATION_BLOCK_ENTITY.get(), (be, context) -> be.getEnergyStorageHandler());


    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries);
        tag.putInt("energy", energyStorage.getEnergyStored());

    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag,registries);
        energyStorage.setEnergy(tag.getInt("energy"));

    }

    public Component getDisplayName() {
        return Component.translatable("block.trading_station.powered_trading_station");
    }


    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        super.tick(pLevel, pPos, pState);
    }

    @Override
    protected void updateProgress() {
        this.progress += MConfigs.server().poweredTradingStation.progressPerTick.get();
        extractEnergy();
    }
    private void extractEnergy() {
        this.energyStorage.extractEnergy(MConfigs.server().poweredTradingStation.energyPerTick.get(), false);
    }

    @Override
    public boolean canCraftItem() {
        Level level = this.getLevel();
        if(level == null)
            return false;

        if(this.energyStorage.getEnergyStored() < MConfigs.server().poweredTradingStation.energyPerTick.get()){
            return false;
        }
        return super.canCraftItem();
    }
    @Override
    public String getMachineId() {
        return "powered";
    }
}
