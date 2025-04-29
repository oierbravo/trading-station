package com.oierbravo.trading_station.content.trading_recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oierbravo.mechanicals.foundation.ingredient.CountableIngredient;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.oierbravo.trading_station.ModConstants;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TradingRecipeSerializer implements RecipeSerializer<TradingRecipe> {

    public static final TradingRecipeSerializer INSTANCE = new TradingRecipeSerializer();

    public final StreamCodec<RegistryFriendlyByteBuf, TradingRecipe> STREAM_CODEC = StreamCodec.of(this::toNetwork, this::fromNetwork);

    private TradingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        NonNullList<CountableIngredient> input = CatnipStreamCodecBuilders.nonNullList(CountableIngredient.STREAM_CODEC).decode(buffer);
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int processingTime = ByteBufCodecs.VAR_INT.decode(buffer);
        List<IRecipeRequirement> recipeRequirements = IRecipeRequirement.LIST_STREAM_CODEC.decode(buffer);

        return new TradingRecipeBuilder()
                .require(input)
                .output(output)
                .processingTime(processingTime)
                .withRequirements(recipeRequirements)
                .build();
    }

    private void toNetwork(RegistryFriendlyByteBuf buffer, TradingRecipe tradingRecipe) {
        CatnipStreamCodecBuilders.nonNullList(CountableIngredient.STREAM_CODEC).encode(buffer, tradingRecipe.getCountableIngredients());
        ItemStack.STREAM_CODEC.encode(buffer, tradingRecipe.getResult());
        ByteBufCodecs.VAR_INT.encode(buffer, tradingRecipe.getProcessingTime());
        IRecipeRequirement.LIST_STREAM_CODEC.encode(buffer, tradingRecipe.getRecipeRequirements());
    }

    public static final MapCodec<TradingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance
                    .group(
                            NonNullList.codecOf(CountableIngredient.CODEC).fieldOf("ingredients").forGetter(TradingRecipe::getCountableIngredients),
                            ItemStack.CODEC.fieldOf("result").forGetter(TradingRecipe::getResult),
                            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("processingTime", 0).forGetter(TradingRecipe::getProcessingTime),
                            IRecipeRequirement.LIST_CODEC.optionalFieldOf("requirements", List.of()).forGetter(TradingRecipe::getRecipeRequirements),
                            ICondition.LIST_CODEC.optionalFieldOf(ConditionalOps.DEFAULT_CONDITIONS_KEY, List.of()).forGetter(TradingRecipe::getConditions)
                    ).apply(instance, (input, result, processingTime, requirements, iConditions) -> {

                        return new TradingRecipeBuilder()
                                .require(input)
                                .output(result)
                                .processingTime(processingTime)
                                .withRequirements(requirements)
                                .withConditions(iConditions)
                                .build();
                    })
    );

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MODID, TradingRecipe.Type.ID);



    @Override
    public @NotNull MapCodec<TradingRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, TradingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
