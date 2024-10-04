package com.oierbravo.trading_station.registrate;

import com.oierbravo.mechanical_lemon_lib.foundation.recipe.IRecipeTypeInfo;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.RecipeRequirementType;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.BiomeRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MaxHeightRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.MinHeightRequirement;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipeBuilder;
import com.oierbravo.trading_station.foundation.util.ModLang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public enum ModRecipes  implements IRecipeTypeInfo {
    TRADING(TradingRecipe::new, List.of(
            BiomeRequirement.TYPE,
            MinHeightRequirement.TYPE,
            MaxHeightRequirement.TYPE
    ))
    ;
    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    ModRecipes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = ModLang.asId(name());
        id = TradingStation.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
        type = typeObject;
    }
    ModRecipes(TradingRecipeBuilder.TradingRecipeFactory processingFactory, List<RecipeRequirementType<?>> enabledRecipeRequirements) {
        this(() -> new TradingRecipe.TradingRecipeSerializer(processingFactory, enabledRecipeRequirements));
    }



    public static void register(IEventBus modEventBus) {
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }
    @SuppressWarnings("unchecked")

    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    public static Optional<TradingRecipe> find(SimpleContainer pInv, Level pLevel) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return find(pInv, pLevel, null);
    }

    public static Optional<TradingRecipe> find(SimpleContainer pInv, Level pLevel,@Nullable Biome biome) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return pLevel.getRecipeManager().getAllRecipesFor(ModRecipes.TRADING.getType()).stream()
                .filter(containerRecipe -> containerRecipe.matches(pInv,pLevel))
                //.filter((tradingRecipe -> tradingRecipe.matchesBiome(biome, pLevel)))
                .findFirst();

    }

    public static Optional<TradingRecipe> find(SimpleContainer pInv, Level pLevel, @Nullable Biome biome, String traderType) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter((tradingRecipe -> tradingRecipe.matches(pInv, pLevel, biome, traderType)))
                .findFirst();

    }
    public static List<TradingRecipe> getAllRecipesForMachine(Level pLevel, Biome biome, String machineType) {
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter((tradingRecipe -> tradingRecipe.matchesBiome(biome, pLevel)))
                .filter((tradingRecipe -> tradingRecipe.matchesExclusiveTo(machineType)))
                .sorted((recipe1, recipe2) -> recipe1.getId().compareNamespaced(recipe2.getId()))
                .toList();
    }
    public static List<ItemStack> getAllOutputs(Level pLevel, @Nullable Biome biome, String machineType){
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                //.filter((tradingRecipe -> tradingRecipe.matchesBiome(biome, pLevel)))
                //.filter((tradingRecipe -> tradingRecipe.matchesExclusiveTo(machineType)))
                .sorted((recipe1, recipe2) -> recipe1.getId().compareNamespaced(recipe2.getId()))
                .map(TradingRecipe::getResult)
                .toList();

    }
    public static Optional<TradingRecipe> findByOutput(Level pLevel,ItemStack targetedOutput){
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe..INSTANCE).stream()
                .filter(recipe -> recipe.matchesOutput(targetedOutput)).findFirst();

    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TradingStation.MODID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, TradingStation.MODID);
    }
}
