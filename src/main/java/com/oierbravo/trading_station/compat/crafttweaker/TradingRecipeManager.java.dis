package com.oierbravo.trading_station.compat.crafttweaker;


import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.oierbravo.trading_station.TradingStation;
import com.oierbravo.trading_station.content.trading_recipe.BiomeCondition;
import com.oierbravo.trading_station.content.trading_recipe.ExclusiveToCondition;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipeBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.trading_station.TradingManager")
//@Document("mods/trading_station/melting")
public class TradingRecipeManager implements IRecipeManager<TradingRecipe> {
    /**
     * Adds a Trading recipe.
     *
     * @param name     The name of the recipe
     * @param output  The output fluidStack of the recipe.
     * @param input    The input of the recipe.
     * @param processingTime The duration of the recipe (default 100 ticks)
     * @param heatLevel Minimum heat level
     *
     * @docParam name "meltDown"
     * @docParam output <fluid:minecraft:lava> * 100
     * @docParam input <item:minecraft:dirt>
     * @docParam duration 200
     * @docParam heatLevel 2
     */
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient[] itemInputs, int processingTime, String biome, String[] exclusiveTo   ){
        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation(TradingStation.MODID, name);
        TradingRecipeBuilder builder = new TradingRecipeBuilder(resourceLocation);
        List<Ingredient> ingredients = new ArrayList();
        Arrays.stream(itemInputs).forEach((iIngredient) -> {
            ingredients.add(iIngredient.asVanillaIngredient());
        });
        builder.withItemIngredients((Ingredient[])ingredients.toArray(new Ingredient[0]));
        builder.withSingleItemOutput(output.asItemLike());
        builder.processingTime(processingTime);
        builder.withBiomeCondition(BiomeCondition.fromString(biome));
        builder.exclusiveTo(ExclusiveToCondition.fromList(List.of(exclusiveTo)));

        TradingRecipe recipe = builder.build();

        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipe));

    }

    @Override
    public RecipeType<TradingRecipe> getRecipeType() {
        return TradingRecipe.Type.INSTANCE;
    }
}
