package com.oierbravo.trading_station.network.packets.handler;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import com.oierbravo.trading_station.network.packets.data.RecipeSelectSyncPayload;
import com.oierbravo.trading_station.network.packets.data.RedstoneModeSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RedstoneModeSyncPacket {
    public static final RedstoneModeSyncPacket INSTANCE = new RedstoneModeSyncPacket();

    public static RedstoneModeSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final RedstoneModeSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isLoaded(payload.pos())) {
                var be = level.getBlockEntity(payload.pos());
                if (be instanceof ITradingStationBlockEntity controller) {
                    controller.setRedstoneMode(payload.mode());
                }
            }
        });
    }
}
