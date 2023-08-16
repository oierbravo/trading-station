package com.oierbravo.trading_station.content.trading_station.powered;

import com.oierbravo.trading_station.foundation.gui.AbstractTradingMenu;
import com.oierbravo.trading_station.registrate.PoweredTradingStationRegistrate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class PoweredTradingStationMenu extends AbstractTradingMenu {
    public PoweredTradingStationMenu(int pContainerId, Inventory pInv, BlockEntity pBlockEntity, ContainerData pData) {
        super(PoweredTradingStationRegistrate.POWERED_TRADING_STATION_MENU.get(), pContainerId, pInv, pBlockEntity, pData);
    }

    public PoweredTradingStationMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        super(PoweredTradingStationRegistrate.POWERED_TRADING_STATION_MENU.get(), pContainerId, inventory, buf);
    }

    public static PoweredTradingStationMenu factory(@Nullable MenuType<PoweredTradingStationMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        return new PoweredTradingStationMenu(pContainerId, inventory, buf);
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, PoweredTradingStationRegistrate.POWERED_TRADING_STATION_BLOCK.get());
    }
    @Override
    public int[][] getInputSlotCoords() {
        return new int[][]{{28,38},{51,38}};
    }
    @Override
    public int[] getOutputSlotCoords() {
        return new int[]{131,38};
    }
  }
