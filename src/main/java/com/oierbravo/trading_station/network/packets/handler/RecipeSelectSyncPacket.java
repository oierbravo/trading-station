package com.oierbravo.trading_station.network.packets.handler;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import com.oierbravo.trading_station.network.packets.data.RecipeSelectSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RecipeSelectSyncPacket {
    public static final RecipeSelectSyncPacket INSTANCE = new RecipeSelectSyncPacket();

    public static RecipeSelectSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final RecipeSelectSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isLoaded(payload.pos())) {
                var be = level.getBlockEntity(payload.pos());
                if (be instanceof ITradingStationBlockEntity controller) {
                    controller.setTargetedRecipeById(payload.recipeId());
                }
            }
        });
    }
}
