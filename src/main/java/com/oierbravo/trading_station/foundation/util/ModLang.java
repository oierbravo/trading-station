package com.oierbravo.trading_station.foundation.util;

import com.oierbravo.trading_station.TradingStation;
import net.minecraft.network.chat.Component;

import java.util.Locale;

public class ModLang {
    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
    public static Component translate(String key){
        return Component.translatable(TradingStation.MODID + '.' + key);
    }
    public static String key(String key){
        return TradingStation.MODID + '.' + key;
    }
}
