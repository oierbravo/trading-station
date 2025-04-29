package com.oierbravo.trading_station.network.packets.handler;

import com.oierbravo.trading_station.content.trading_station.ITradingStationBlockEntity;
import com.oierbravo.trading_station.network.packets.data.ItemSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ItemSyncPacket {
    public static final ItemSyncPacket INSTANCE = new ItemSyncPacket();

    public static ItemSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final ItemSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isLoaded(payload.pos())) {
                var be = level.getBlockEntity(payload.pos());
                if (be instanceof ITradingStationBlockEntity controller) {
                    controller.setItemStack(payload.slot(),payload.itemStack(), payload.slotType());
                }
            }
        });
    }
}
