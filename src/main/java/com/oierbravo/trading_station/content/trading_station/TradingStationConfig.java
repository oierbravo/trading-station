package com.oierbravo.trading_station.content.trading_station;

import net.minecraftforge.common.ForgeConfigSpec;

public class TradingStationConfig {
    //public static ForgeConfigSpec.IntValue MELTER_CAPACITY;
    //public static ForgeConfigSpec.IntValue MELTER_FLUID_PER_TICK;

    public static void registerServerConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
     /*   SERVER_BUILDER.comment("Settings for the trading_station").push("trading_station");
        MELTER_CAPACITY = SERVER_BUILDER
                .comment("How much liquid fits into the trading_station, in mB")
                .defineInRange("capacity", 1000, 1, Integer.MAX_VALUE);
        MELTER_FLUID_PER_TICK = SERVER_BUILDER
                .comment("How much liquid generates per tick, in mB")
                .defineInRange("liquidPerTick", 2, 1, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();*/
    }
}
