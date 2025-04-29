package com.oierbravo.trading_station.network.packets.data;

import com.oierbravo.trading_station.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RedstoneModeSyncPayload(
        Byte mode,
        BlockPos pos
    ) implements CustomPacketPayload {

    public static final Type<RedstoneModeSyncPayload> TYPE = new Type<>(ModConstants.asResource("redstone_mode_payload"));

    @Override
    public Type<RedstoneModeSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, RedstoneModeSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, RedstoneModeSyncPayload::mode,
            BlockPos.STREAM_CODEC, RedstoneModeSyncPayload::pos,
            RedstoneModeSyncPayload::new
    );
}
