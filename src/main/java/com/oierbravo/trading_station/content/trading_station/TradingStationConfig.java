package com.oierbravo.trading_station.content.trading_station;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class TradingStationConfig extends ConfigBase {

    public final ConfigBase.ConfigInt progressPerTick = i(1,1,"progressPerTick", Comments.progressPerTick);


    private static class Comments {
        static String progressPerTick = "How much progress per tick.";
    }

    @Override
    public @NotNull String getName() {
        return "Trading Station";
    }

}
