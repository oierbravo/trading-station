package com.oierbravo.trading_station.content.trading_station.powered;

import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationConfig;
import com.oierbravo.trading_station.foundation.util.ModEnergyStorage;
import com.oierbravo.trading_station.foundation.util.ModLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PoweredTradingStationBlockEntity extends TradingStationBlockEntity {

    private final ModEnergyStorage energyStorage = createEnergyStorage();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    public PoweredTradingStationBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }
    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(PoweredTradingStationConfig.ENERGY_CAPACITY.get(), PoweredTradingStationConfig.ENERGY_TRANSFER.get()) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }
    public IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Override
    public String getTraderType() {
        return "powered";
    }

    public void setRemoved() {
        super.setRemoved();
    }
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("energy", energyStorage.getEnergyStored());

    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energyStorage.setEnergy(tag.getInt("energy"));

    }

    public Component getDisplayName() {
        return Component.translatable("block.trading_station.powered_trading_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PoweredTradingStationMenu(pContainerId, pPlayerInventory, this, this.containerData);
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        super.tick(pLevel, pPos, pState);
    }

    @Override
    protected void updateProgress() {
        this.progress += PoweredTradingStationConfig.PROGRESS_PER_TICK.get();
        extractEnergy();
    }
    private void extractEnergy() {
        this.energyStorage.extractEnergy(PoweredTradingStationConfig.ENERGY_PER_TICK.get(), false);
    }

    @Override
    public boolean canCraftItem() {
        Level level = this.getLevel();
        if(level == null)
            return false;

        if(this.energyStorage.getEnergyStored() < PoweredTradingStationConfig.ENERGY_PER_TICK.get()){
            return false;
        }
        return super.canCraftItem();
    }
}
