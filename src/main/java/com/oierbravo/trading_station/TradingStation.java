package com.oierbravo.trading_station;

import com.mojang.logging.LogUtils;
import com.oierbravo.mechanicals.utility.RegistrateLangBuilder;
import com.oierbravo.trading_station.content.trading_station.TradingStationBlockEntity;
import com.oierbravo.trading_station.content.trading_station.powered.PoweredTradingStationBlockEntity;
import com.oierbravo.trading_station.infrastructure.config.MConfigs;
import com.oierbravo.trading_station.infrastructure.data.ModDataGen;
import com.oierbravo.trading_station.registrate.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModConstants.MODID)
public class TradingStation
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(ModConstants.MODID).defaultCreativeTab(ModCreativeTabs.MAIN_TAB.getKey()));

    public TradingStation(IEventBus modEventBus, ModContainer modContainer) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        ModCreativeTabs.register(modEventBus);

        ModBlocks.register();
        ModBlockEntities.register();
        modEventBus.addListener(this::registerCapabilities);

        ModItemComponents.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModMessages.register();
        modEventBus.addListener(ModMessages::registerNetworking);

        ModMenus.register();

        MConfigs.register(modLoadingContext,modContainer);
        modEventBus.addListener(ModDataGen::gatherData);

        ModRecipeRequirementTypes.init(modEventBus);

        registerLanguageKeys();
    }
    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        TradingStationBlockEntity.registerCapabilities(event);
        PoweredTradingStationBlockEntity.registerCapabilities(event);
    }

    private static void registerLanguageKeys(){
        new RegistrateLangBuilder<>(ModConstants.MODID,registrate())
                .addCreativeTab(ModConstants.DISPLAY_NAME)
                .add("trading.recipe", "Trading recipe")
                .add("tooltip.progress", "Progress: %d%%")
                .add("select_target.title", "Select an output target")
                .add("select_target.button", "Select target")
                .add("screen.lock.lock", "Lock input slots to recipe")
                .add("screen.lock.unlock", "Unlock input slots")
                .add("select_target.clear", "Clear")
                .add("select_target.back", "Back")
                .add("confirm.button", "Confirm")
                .add("screen.redstone.ignored", "Redstone: Ignored")
                .add("screen.redstone.low", "Redstone: Low")
                .add("screen.redstone.high", "Redstone: High")
                .add("ui.recipe_requirement.machine_id.any", "Any")
                .addJade("Trading Station data")
                .addRecipeRequirementTitle("machine_id", "Machines:")
                .addRecipeRequirementValue( "machine_id", "%s")
                .addRecipeRequirementMissing(  "machine_id", "Incorrect machine");
    }
    public static Registrate registrate() {
        return REGISTRATE.get();
    }

}
