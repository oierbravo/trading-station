package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.foundation.gui.AbstractTradingMenu;
import com.oierbravo.trading_station.registrate.TradingStationRegistrate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class  TradingStationMenu extends AbstractTradingMenu {
    protected final int[] outputSlotCoords = {131,38};

    public TradingStationMenu(int pContainerId, Inventory pInv, BlockEntity pBlockEntity, ContainerData pData) {
        super(TradingStationRegistrate.TRADING_STATION_MENU.get(), pContainerId, pInv, pBlockEntity, pData);
    }

    public TradingStationMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        super(TradingStationRegistrate.TRADING_STATION_MENU.get(), pContainerId, inventory, buf);
    }

    public static TradingStationMenu factory(@Nullable MenuType<TradingStationMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        return new TradingStationMenu(pContainerId, inventory, buf);
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, TradingStationRegistrate.TRADING_STATION_BLOCK.get());
    }
    @Override
    public int[][] getInputSlotCoords() {
        return new int[][]{{19,38},{42,38}};
    }
    @Override
    public int[] getOutputSlotCoords() {
        return new int[]{131,38};
    }
  }
