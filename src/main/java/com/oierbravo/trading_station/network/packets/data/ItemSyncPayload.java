package com.oierbravo.trading_station.network.packets.data;

import com.oierbravo.trading_station.ModConstants;
import net.createmod.catnip.codecs.CatnipCodecs;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.ItemStack;

import java.util.function.IntFunction;

public record ItemSyncPayload(
        ItemStack itemStack,
        BlockPos pos,
        int slot,
        SlotType slotType
    ) implements CustomPacketPayload {

    public static final Type<ItemSyncPayload> TYPE = new Type<>(ModConstants.asResource("itemsync_payload"));

    @Override
    public Type<ItemSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, ItemSyncPayload::itemStack,
            BlockPos.STREAM_CODEC, ItemSyncPayload::pos,
            ByteBufCodecs.INT, ItemSyncPayload::slot,
            ByteBufCodecs.idMapper(SlotType.BY_ID, SlotType::getId), ItemSyncPayload::slotType,
            ItemSyncPayload::new
    );
		//DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(s -> s.patch),

    public enum SlotType {
        INPUT(0), OUTPUT(1), TARGET(2);
        private int id;
        SlotType(int id){
            this.id = id;
        };
        public int getId(){
            return id;
        }
        public static final IntFunction<SlotType> BY_ID =
                ByIdMap.continuous(
                        SlotType::getId,
                        SlotType.values(),
                        ByIdMap.OutOfBoundsStrategy.ZERO
                );
    }
}
