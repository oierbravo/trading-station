package com.oierbravo.trading_station.network.packets.handler;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import com.oierbravo.trading_station.network.packets.data.LockInputSyncPayload;
import com.oierbravo.trading_station.network.packets.data.RecipeClearSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RecipeClearSyncPacket {
    public static final RecipeClearSyncPacket INSTANCE = new RecipeClearSyncPacket();

    public static RecipeClearSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final RecipeClearSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isLoaded(payload.pos())) {
                var be = level.getBlockEntity(payload.pos());
                if (be instanceof ITradingStationBlockEntity controller) {
                    controller.clearTargetedRecipe();
                }
            }
        });
    }
}
