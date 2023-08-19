package com.oierbravo.trading_station.foundation.util;

import com.oierbravo.trading_station.TradingStation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public class ModLang {
    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
    public static MutableComponent translate(String key, Object... args){
        return Component.translatable(TradingStation.MODID + '.' + key,resolveBuilders(args));
    }
    public static String key(String key){
        return TradingStation.MODID + '.' + key;
    }

    public static Object[] resolveBuilders(Object[] args) {
        //      for (int i = 0; i < args.length; i++)
        //    if (args[i]instanceof LangBuilder cb)
        //        args[i] = cb.component();
        return args;
    }
}
