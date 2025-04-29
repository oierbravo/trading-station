package com.oierbravo.trading_station.network.packets.data;

import com.oierbravo.trading_station.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RecipeSelectSyncPayload(
        ResourceLocation recipeId,
        BlockPos pos
    ) implements CustomPacketPayload {

    public static final Type<RecipeSelectSyncPayload> TYPE = new Type<>(ModConstants.asResource("recipe_select_payload"));

    @Override
    public Type<RecipeSelectSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSelectSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, RecipeSelectSyncPayload::recipeId,
            BlockPos.STREAM_CODEC, RecipeSelectSyncPayload::pos,
            RecipeSelectSyncPayload::new
    );
}
