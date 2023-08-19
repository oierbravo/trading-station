package com.oierbravo.trading_station.content.trading_station.powered;

import net.minecraftforge.common.ForgeConfigSpec;

public class PoweredTradingStationConfig {
    public static ForgeConfigSpec.IntValue PROGRESS_PER_TICK;
    public static ForgeConfigSpec.IntValue ENERGY_CAPACITY;
    public static ForgeConfigSpec.IntValue ENERGY_TRANSFER;
    public static ForgeConfigSpec.IntValue ENERGY_PER_TICK;

    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
       COMMON_BUILDER.comment("Settings for the Powered Trading Station").push("powered_trading_station");
        PROGRESS_PER_TICK = COMMON_BUILDER
                .comment("How much progress per tick")
                .defineInRange("progressPerTick", 5, 1, Integer.MAX_VALUE);
        ENERGY_CAPACITY = COMMON_BUILDER
                .comment("How much energy capacity has")
                .defineInRange("energyCapacity", 64000, 1, Integer.MAX_VALUE);
        ENERGY_TRANSFER = COMMON_BUILDER
                .comment("How much energy can transfer")
                .defineInRange("energyTransfer", 200, 1, Integer.MAX_VALUE);
        ENERGY_PER_TICK = COMMON_BUILDER
                .comment("How much energy can transfer")
                .defineInRange("energyPerTick", 1000, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }

}
