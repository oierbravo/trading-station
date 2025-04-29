package com.oierbravo.trading_station.network.packets.data;

import com.oierbravo.trading_station.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RecipeClearSyncPayload(
        BlockPos pos
    ) implements CustomPacketPayload {

    public static final Type<RecipeClearSyncPayload> TYPE = new Type<>(ModConstants.asResource("recipe_clear_payload"));

    @Override
    public Type<RecipeClearSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeClearSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RecipeClearSyncPayload::pos,
            RecipeClearSyncPayload::new
    );
}
