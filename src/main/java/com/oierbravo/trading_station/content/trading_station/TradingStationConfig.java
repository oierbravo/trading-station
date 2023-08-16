package com.oierbravo.trading_station.content.trading_station;

import net.minecraftforge.common.ForgeConfigSpec;

public class TradingStationConfig {
    public static ForgeConfigSpec.IntValue PROGRESS_PER_TICK;

    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
       COMMON_BUILDER.comment("Settings for the trading_station").push("trading_station");
        PROGRESS_PER_TICK = COMMON_BUILDER
                .comment("How much progress per tick")
                .defineInRange("progressPerTick", 1, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }


}
