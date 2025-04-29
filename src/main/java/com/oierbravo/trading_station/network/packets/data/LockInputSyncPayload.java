package com.oierbravo.trading_station.network.packets.data;

import com.oierbravo.trading_station.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.ItemStack;

import java.util.function.IntFunction;

public record LockInputSyncPayload(
        Boolean lock,
        BlockPos pos
    ) implements CustomPacketPayload {

    public static final Type<LockInputSyncPayload> TYPE = new Type<>(ModConstants.asResource("lock_input_payload"));

    @Override
    public Type<LockInputSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LockInputSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, LockInputSyncPayload::lock,
            BlockPos.STREAM_CODEC, LockInputSyncPayload::pos,
            LockInputSyncPayload::new
    );
}
