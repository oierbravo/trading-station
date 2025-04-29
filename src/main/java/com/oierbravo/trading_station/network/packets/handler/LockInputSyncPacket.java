package com.oierbravo.trading_station.network.packets.handler;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import com.oierbravo.trading_station.network.packets.data.ItemSyncPayload;
import com.oierbravo.trading_station.network.packets.data.LockInputSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class LockInputSyncPacket {
    public static final LockInputSyncPacket INSTANCE = new LockInputSyncPacket();

    public static LockInputSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final LockInputSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isLoaded(payload.pos())) {
                var be = level.getBlockEntity(payload.pos());
                if (be instanceof ITradingStationBlockEntity controller) {
                    controller.setInputLock(payload.lock());
                }
            }
        });
    }
}
