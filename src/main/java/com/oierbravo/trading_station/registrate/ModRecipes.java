package com.oierbravo.trading_station.registrate;

import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TradingStation.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TradingStation.MODID);

    public static final RegistryObject<RecipeType<TradingRecipe>> TRADING_TYPE =
            RECIPE_TYPES.register("trading",() -> TradingRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<TradingRecipe>> TRADING_SERIALIZER =
            SERIALIZERS.register("trading", () -> new TradingRecipe.TradingRecipeSerializer(TradingRecipe.enabledRecipeRequirements));


    public static List<TradingRecipe> getAllRecipesForMachine(Level pLevel, BlockEntity pBlockEntity) {
        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter((tradingRecipe -> tradingRecipe.checkRequirements(pLevel, pBlockEntity)))
                .sorted((recipe1, recipe2) -> recipe1.getId().compareNamespaced(recipe2.getId()))
                .toList();
    }
    public static CompoundTag getItemTagWithRecipeId(ItemStack item, String tradingRecipeId){
        CompoundTag tag = item.getOrCreateTag();
        if(!tradingRecipeId.isEmpty()){
            tag.putString("tradingRecipeId", tradingRecipeId);
        }
        return tag;
    }

    public static void register(IEventBus eventBus) {

        SERIALIZERS.register(eventBus);

        RECIPE_TYPES.register(eventBus);
    }


    public static Optional<TradingRecipe> findById(Level pLevel, ResourceLocation recipeId) {
        if(pLevel == null)
            return Optional.empty();

        return pLevel.getRecipeManager().getAllRecipesFor(TradingRecipe.Type.INSTANCE).stream()
                .filter(tradingRecipe -> tradingRecipe.matchesId(recipeId))
                .findFirst();
    }
    public static Optional<TradingRecipe> findById(Level pLevel,String pRecipeId) {
        if(pLevel == null)
            return Optional.empty();
        if(pRecipeId.isEmpty())
            return Optional.empty();
        ResourceLocation recipeId = ResourceLocation.tryParse(pRecipeId);
        return findById(pLevel, recipeId);
    }

    public static List<TradingRecipe> getAll() {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<TradingRecipe> list = rm.getAllRecipesFor(ModRecipes.TRADING_TYPE.get());
        return rm.getAllRecipesFor(ModRecipes.TRADING_TYPE.get());
    }
}
