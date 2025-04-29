package com.oierbravo.trading_station.registrate;

import com.oierbravo.mechanicals.utility.MechanicalLangIdGenerator;
import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.ModLang;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeTabs {

    private static final net.neoforged.neoforge.registries.DeferredRegister<CreativeModeTab> TAB_REGISTER =
            net.neoforged.neoforge.registries.DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModConstants.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(ModLang.translate(MechanicalLangIdGenerator.creativeTabId("main")).component())
                    .icon(ModBlocks.TRADING_STATION::asStack)
                    .build());

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.get();
    }

    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }
}
