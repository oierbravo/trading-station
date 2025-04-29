package com.oierbravo.trading_station.infrastructure.data;

import com.oierbravo.mechanicals.foundation.data.AbstractMechanicalRecipeGenerator;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipe;
import com.oierbravo.trading_station.content.trading_recipe.TradingRecipeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.oierbravo.trading_station.ModConstants.MODID;

public class TradingRecipeGen extends AbstractMechanicalRecipeGenerator<TradingRecipeBuilder> {

    public TradingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String namespace, String recipeTypeId, Supplier<TradingRecipeBuilder> builderSupplier, String displayName) {
        super(output, registries, namespace, recipeTypeId, builderSupplier,displayName);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

    }

    public TradingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this(output, registries,
                MODID,
                TradingRecipe.Type.ID,
                TradingRecipeBuilder::new,
                "Trading recipes"
        );
    }


    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {

        /*create("test")
                .require(CountableIngredient.of(new ItemStack(Items.ACACIA_LOG,5)))
                .output(new ItemStack(Items.DIAMOND))
                .processingTime(500)
                .save(recipeOutput);*/


        /*create("2_ingredients_many_requs")
                .require(CountableIngredient.of(new ItemStack(Items.COBBLESTONE)),CountableIngredient.of(new ItemStack(Items.ANDESITE) ))
                .output(new ItemStack(Items.DIAMOND))
                .processingTime(500)
                .withRequirement(MachineRequirement.of("powered", "mechanical"))
                .withRequirement(RecipeRequirementBuilder.minY(-20))
                .withRequirement(RecipeRequirementBuilder.maxY(20))
                .withRequirement(RecipeRequirementBuilder.biomeTag("minecraft:is_nether"))
                .save(recipeOutput);*/



    }
    public static Holder<Enchantment> toHolder(ResourceKey<Enchantment> r, Level level) {
        return level.registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(r);
    }
}
