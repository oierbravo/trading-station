package com.oierbravo.trading_station.foundation.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public record TradingRecipeComponent(ResourceLocation tradingRecipeId) {
    public static final Codec<TradingRecipeComponent> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ResourceLocation.CODEC.fieldOf("id")
                    .forGetter(i -> i.tradingRecipeId))
            .apply(instance, TradingRecipeComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TradingRecipeComponent> STREAM_CODEC =
            StreamCodec.composite(ResourceLocation.STREAM_CODEC, i -> i.tradingRecipeId, TradingRecipeComponent::new);

    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof ResourceLocation otherItem && tradingRecipeId.equals(arg0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tradingRecipeId);
    }
}
