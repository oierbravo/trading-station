package com.oierbravo.trading_station;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.oierbravo.trading_station.compat.Mods;
import com.oierbravo.trading_station.content.trading_station.TradingStationConfig;
import com.oierbravo.trading_station.foundation.util.ModLang;
import com.oierbravo.trading_station.registrate.*;
import com.tterrag.registrate.Registrate;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.util.Lazy;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("trading_station")
public class TradingStation
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "trading_station";
    public static final String DISPLAY_NAME = "Trading Station";

    public static IEventBus modEventBus;

    private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(MODID));

    public static final boolean withCreate = ModList.get().isLoaded("create");


    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    public TradingStation()
    {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModCreativeTab modTab = new ModCreativeTab();
        Config.register();

        TradingStationRegistrate.register();
        PoweredTradingStationRegistrate.register();

        ModRecipes.register(modEventBus);
        ModMessages.register();

        modEventBus.addListener(EventPriority.LOWEST, TradingStation::gatherData);

        Mods.CREATE.executeIfInstalled(() -> MechanicalTradingStationRegistrate::register);
    }
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            registerLanguageKeys();
        }
        if (event.includeServer()) {

        }
    }
    private static void registerLanguageKeys(){
        registrate().addRawLang("itemGroup.trading_station", "Trading Station");
        registrate().addRawLang(ModLang.key("trading.recipe"), "Trading recipe");
        registrate().addRawLang(ModLang.key("trading.recipe.biome"), "%s biome required");
        registrate().addRawLang(ModLang.key("trading.recipe.biomeRequired"), "Specific biome requeriment");
        registrate().addRawLang(ModLang.key("tooltip.progress"), "Progress: %d%%");
        registrate().addRawLang(ModLang.key("select_target.title"), "Select an output target");
        registrate().addRawLang(ModLang.key("select_target.button"), "Select target");
        registrate().addRawLang(ModLang.key("select_target.clear"), "Clear");
        registrate().addRawLang("config.jade.plugin_trading_station.trading_station_data", "Trading Station data");

        registrate().addRawLang(ModLang.key("screen.redstone.redstoneMode"), "Redstone: ");
        registrate().addRawLang(ModLang.key("screen.redstone.ignored"), "Ignored");
        registrate().addRawLang(ModLang.key("screen.redstone.low"), "Low");
        registrate().addRawLang(ModLang.key("screen.redstone.high"), "High");
    }
    public static Registrate registrate() {
        return REGISTRATE.get();
    }


    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
