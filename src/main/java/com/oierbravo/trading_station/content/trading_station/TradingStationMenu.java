package com.oierbravo.trading_station.content.trading_station;

import com.oierbravo.trading_station.foundation.gui.AbstractTradingMenu;
import com.oierbravo.trading_station.registrate.ModBlocks;
import com.oierbravo.trading_station.registrate.ModMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class  TradingStationMenu extends AbstractTradingMenu {
    protected final int[] outputSlotCoords = {131,38};

    public TradingStationMenu(int pContainerId, Inventory pInv, BlockEntity pBlockEntity, ContainerData pData) {
        super(ModMenus.TRADING_STATION.get(), pContainerId, pInv, pBlockEntity, pData);
    }

    public TradingStationMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        super(ModMenus.TRADING_STATION.get(), pContainerId, inventory, buf);
    }

    public static TradingStationMenu factory(@Nullable MenuType<TradingStationMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        return new TradingStationMenu(pContainerId, inventory, buf);
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.TRADING_STATION.get());
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
