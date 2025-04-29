package com.oierbravo.trading_station.registrate;

import com.mojang.serialization.MapCodec;
import com.oierbravo.mechanicals.Mechanicals;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirementType;
import com.oierbravo.mechanicals.foundation.recipe.requirements.*;
import com.oierbravo.mechanicals.register.MechanicalRegistries;
import com.oierbravo.trading_station.ModConstants;
import com.oierbravo.trading_station.content.trading_recipe.MachineRequirement;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeRequirementTypes {
    public static final DeferredRegister<RecipeRequirementType<?>> RECIPE_REQUIREMENT_TYPES =
            DeferredRegister.create(MechanicalRegistries.Keys.RECIPE_REQUIREMENT, ModConstants.MODID);

    public static final Supplier<RecipeRequirementType<MachineRequirement>> MACHINE_ID =
            register(MachineRequirement.ID, MachineRequirement.CODEC, MachineRequirement.STREAM_CODEC);

    public static void init(IEventBus modEventBus) {
        RECIPE_REQUIREMENT_TYPES.register(modEventBus);
    }

    private static <RR extends IRecipeRequirement, RRT extends RecipeRequirementType<RR>> Supplier<RRT> register(String name, MapCodec<RR> codec, StreamCodec<RegistryFriendlyByteBuf, RR> streamCodec) {
        //noinspection unchecked
        return RECIPE_REQUIREMENT_TYPES.register(name, () -> (RRT) new RecipeRequirementType<>(codec, streamCodec));
    }
}

