package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.foundation.gui.AbstractTradingMenu;
import com.oierbravo.trading_station.foundation.gui.Coords2D;
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

    public TradingStationMenu(int pContainerId, Inventory pInv, BlockEntity pBlockEntity, ContainerData pData) {
        super(TradingStationRegistrate.MENU.get(), pContainerId, pInv, pBlockEntity, pData);
    }

    public TradingStationMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        super(TradingStationRegistrate.MENU.get(), pContainerId, inventory, buf);
    }

    public static TradingStationMenu factory(@Nullable MenuType<TradingStationMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        return new TradingStationMenu(pContainerId, inventory, buf);
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, getBlockPos()), pPlayer, TradingStationRegistrate.BLOCK.get());
    }
    @Override
    public Coords2D[] getInputSlotCoords() {
        return new Coords2D[]{
                Coords2D.of(19,49),
                Coords2D.of(42,49)
        };
    }
    @Override
    public Coords2D getOutputSlotCoords() {
        return Coords2D.of(131,49);
    }

    public Coords2D getTargetSlotCoords() {
        return Coords2D.of(87,40);
    }

}
