package com.oierbravo.trading_station.content.trading_station.powered;

import com.oierbravo.trading_station.foundation.gui.AbstractTradingMenu;
import com.oierbravo.trading_station.foundation.gui.Coords2D;
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
        super(PoweredTradingStationRegistrate.MENU.get(), pContainerId, pInv, pBlockEntity, pData);
    }

    public PoweredTradingStationMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        super(PoweredTradingStationRegistrate.MENU.get(), pContainerId, inventory, buf);
    }

    public static PoweredTradingStationMenu factory(@Nullable MenuType<PoweredTradingStationMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        return new PoweredTradingStationMenu(pContainerId, inventory, buf);
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        if(stillValid(ContainerLevelAccess.create(level, getBlockPos()), pPlayer, PoweredTradingStationRegistrate.BLOCK.get()))
            return true;
        if(stillValid(ContainerLevelAccess.create(level, getBlockPos()), pPlayer, PoweredTradingStationRegistrate.BLOCK_UNBREAKABLE.get()))
            return true;
        return false;
    }
    @Override
    public Coords2D[] getInputSlotCoords() {
        return new Coords2D[]{
            Coords2D.of(28,47),
            Coords2D.of(51,47)
        };
    }

    @Override
    public Coords2D[] getInputRecipeCoords() {
        return new Coords2D[]{
                Coords2D.of(28,20),
                Coords2D.of(51,20)
        };
    }
    @Override
    public Coords2D getOutputSlotCoords() {
        return Coords2D.of(131,47);
    }
    public Coords2D getTargetSlotCoords() {
        return Coords2D.of(87,40);
    }

}
