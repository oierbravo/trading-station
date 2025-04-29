package com.oierbravo.trading_station.content.trading_recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oierbravo.mechanicals.foundation.recipe.IRecipeRequirement;
import com.oierbravo.mechanicals.foundation.recipe.RecipeRequirementType;
import com.oierbravo.trading_station.ModLang;
import com.oierbravo.trading_station.registrate.ModRecipeRequirementTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public record MachineRequirement(List<String> machineId) implements IRecipeRequirement {
    public static String ID = "machine_id";

    public static MapCodec<MachineRequirement> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(Codec.STRING.listOf().optionalFieldOf("value", List.of()).forGetter(MachineRequirement::machineId)).apply(builder, MachineRequirement::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MachineRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list(16)), MachineRequirement::machineId,
            MachineRequirement::new
    );

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.MACHINE_ID.get();
    }

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        if(machineId.isEmpty())
            return true;
        if(blockEntity instanceof IHaveMachineId) {
            for (String id : machineId) {
                if (id.equals(((IHaveMachineId) blockEntity).getMachineId()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if(machineId.isEmpty())
            return ModLang.translate("ui.recipe_requirement.machine_id.any").toString();
        return String.join(",", machineId);
    }
    @Override
    public String getIdString() {
        return ID;
    }

    public static MachineRequirement of(String... machineId){
        return new MachineRequirement(List.of(machineId));
    }
}
