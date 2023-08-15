package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab  extends CreativeModeTab {
    public static ModCreativeTab MAIN;


    public ModCreativeTab() {
            super(TradingStation.DISPLAY_NAME);
            MAIN = this;
        }

    @Override
    public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.TRADING_STATION.get());
        }

}
