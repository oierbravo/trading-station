package com.oierbravo.trading_station.foundation.util;

import com.oierbravo.mechanical_lemon_ui.foundation.utility.Lang;
import com.oierbravo.mechanical_lemon_ui.foundation.utility.LangBuilder;
import com.oierbravo.trading_station.TradingStation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public class ModLang extends Lang {
    public ModLang() {
        super();
    }

    public static LangBuilder builder() {
        return new LangBuilder(TradingStation.MODID);
    }
    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }

}
