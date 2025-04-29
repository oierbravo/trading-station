package com.oierbravo.trading_station.infrastructure.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;


public class ModDataGen {
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(
                event.includeServer(),
                new TradingRecipeGen(output, lookupProvider)
        );

        /*if (event.includeServer()) {

        }*/
        //event.getGenerator().addProvider(true, TradingStation.registrate().setDataProvider(new RegistrateDataProvider(CreateSifter.registrate(), MODID, event)));

    }
}