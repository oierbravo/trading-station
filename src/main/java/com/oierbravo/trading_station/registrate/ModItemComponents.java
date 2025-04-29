package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.foundation.component.TradingRecipeComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.UnaryOperator;

public class ModItemComponents {
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ModConstants.MODID);

    public static final DataComponentType<TradingRecipeComponent> TRADING_RECIPE_ID = register(
            "trading_recipe_id",
            builder -> builder.persistent(TradingRecipeComponent.CODEC).networkSynchronized(TradingRecipeComponent.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}
