package com.oierbravo.trading_station;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.oierbravo.trading_station.registrate.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Lazy;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
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


    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    public TradingStation()
    {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        Config.register();
        ModCreativeTab.register(modEventBus);

        ModBlocks.register();
        ModBlockEntities.register();

        ModRecipes.register(modEventBus);
        ModMessages.register();

        ModMenus.register();
        modEventBus.addListener(this::addCreative);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> TradingStationClient.onCtorClient(modEventBus, forgeEventBus));

        modEventBus.addListener(EventPriority.LOWEST, TradingStation::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(TradingStationClient.class);
        });

    }
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ModCreativeTab.MAIN_TAB.getKey()){
            for (RegistryEntry<Item> entry : TradingStation.registrate().getAll(Registries.ITEM)) {
                event.accept(entry.get());
            }
        }
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
        registerLangCustom("itemGroup.trading_station", "Trading Station");
        registerLang("trading.recipe", "Trading recipe");
        registerLang("trading.recipe.biome", "%s biome required");
        registerLang("trading.recipe.biomeRequired", "Specific biome requeriment");
        registerLang("tooltip.progress", "Progress: %d%%");
        registerLang(("select_target.title"), "Select an output target");
        registerLang("select_target.button", "Select target");
        registerLang("screen.lock.lock", "Lock input slots to recipe");
        registerLang("screen.lock.unlock", "Unlock input slots");
        registerLang("select_target.clear", "Clear");
        registerLang("select_target.back", "Back");
        registerLang("confirm.button", "Confirm");
        registerLangCustom("config.jade.plugin_trading_station.trading_station_data", "Trading Station data");

        registerLang("screen.redstone.ignored", "Redstone: Ignored");
        registerLang("screen.redstone.low", "Redstone: Low");
        registerLang("screen.redstone.high", "Redstone: High");
    }
    public static Registrate registrate() {
        return REGISTRATE.get();
    }


    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void registerLang(String id, String text){
        registerLangCustom(MODID.toLowerCase() + "." + id, text);
    }

    public static void registerLangCustom(String id, String text){
        registrate().addRawLang(id, text);
    }


}
