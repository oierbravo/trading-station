package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModCreativeTab  extends CreativeModeTab {
    public static ModCreativeTab MAIN;


    public ModCreativeTab() {
            super(TradingStation.MODID);
            MAIN = this;
        }

    @Override
    public ItemStack makeIcon() {
            return new ItemStack(TradingStationRegistrate.TRADING_STATION_BLOCK.get());
        }

}
