package com.oierbravo.trading_station.content.trading_station.powered;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class PoweredTradingStationConfig extends ConfigBase {

    public final ConfigBase.ConfigInt progressPerTick = i(5,1,"progressPerTick", PoweredTradingStationConfig.Comments.progressPerTick);
    public final ConfigBase.ConfigInt energyCapacity = i(64000,1,"energyCapacity", PoweredTradingStationConfig.Comments.energyCapacity);
    public final ConfigBase.ConfigInt energyTransfer = i(2000,1,"energyTransfer", PoweredTradingStationConfig.Comments.energyTransfer);
    public final ConfigBase.ConfigInt energyPerTick = i(500,1,"energyPerTick", PoweredTradingStationConfig.Comments.energyPerTick);


    private static class Comments {
        static String progressPerTick = "How much progress per tick.";
        static String energyCapacity = "How much energy capacity has.";
        static String energyTransfer = "How much energy can transfer.";
        static String energyPerTick = "How much energy consumens per tick.";
    }

    @Override
    public @NotNull String getName() {
        return "Powered Trading station";
    }

}
