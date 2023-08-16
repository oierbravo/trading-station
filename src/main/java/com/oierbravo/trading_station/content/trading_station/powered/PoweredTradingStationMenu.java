package com.oierbravo.trading_station.content.trading_station.powered;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.TradingStationMenu;
import com.oierbravo.trading_station.content.trading_station.TradingStationScreen;
import com.oierbravo.trading_station.foundation.gui.AbstractTradingMenu;
import com.oierbravo.trading_station.registrate.ModBlocks;
import com.oierbravo.trading_station.registrate.ModMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class PoweredTradingStationMenu extends AbstractTradingMenu {
    public PoweredTradingStationMenu(int pContainerId, Inventory pInv, BlockEntity pBlockEntity, ContainerData pData) {
        super(ModMenus.POWERED_TRADING_STATION.get(), pContainerId, pInv, pBlockEntity, pData);
    }

    public PoweredTradingStationMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        super(ModMenus.POWERED_TRADING_STATION.get(), pContainerId, inventory, buf);
    }

    public static PoweredTradingStationMenu factory(@Nullable MenuType<PoweredTradingStationMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        return new PoweredTradingStationMenu(pContainerId, inventory, buf);
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.POWERED_TRADING_STATION.get());
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
