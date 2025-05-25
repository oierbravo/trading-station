package com.oierbravo.trading_station.content.trading_recipe;

import com.google.gson.JsonObject;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.RecipeRequirement;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.RecipeRequirementType;
import com.oierbravo.mechanical_lemon_lib.utility.LibLang;
import com.oierbravo.trading_station.foundation.util.ModLang;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class MachineRequirement extends RecipeRequirement {
    public static final RecipeRequirementType<?> TYPE = new MachineRequirementType();
    public static final MachineRequirement EMPTY = new MachineRequirement();

    private String machineId;

    public MachineRequirement() {

    }

    public MachineRequirement(String machineId) {
        this.machineId = machineId;
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        if(blockEntity instanceof IHaveMachineId)
            return Objects.equals(((IHaveMachineId) blockEntity).getMachineId(), this.machineId);
        return false;
    }

    @Override
    public boolean isPresent() {
        return machineId != null;
    }

    @Override
    public String toString() {
        return machineId;
    }

    public static MachineRequirement of(String machineId){
        return new MachineRequirement(machineId);
    }

    private static class MachineRequirementType extends RecipeRequirementType<MachineRequirement> {

        public MachineRequirementType(){
            this("machine");
        }
        public MachineRequirementType(String id) {
            super(id);
        }

        @Override
        public MachineRequirement fromJson(JsonObject jsonObject) {
            if (GsonHelper.isValidNode(jsonObject, this.getId())) {
                return of(jsonObject.get(this.getId()).getAsString());
            }
            return EMPTY;
        }

        @Override
        public JsonObject toJson(JsonObject jsonObject, RecipeRequirement recipeRequirement) {
            if(!recipeRequirement.isPresent())
                return jsonObject;
            jsonObject.addProperty(this.getId(), recipeRequirement.toString());
            return jsonObject;
        }

        @Override
        public MachineRequirement fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            boolean hasRequirement = friendlyByteBuf.readBoolean();
            if(hasRequirement) {
                return of(friendlyByteBuf.readUtf());
            }
            return MachineRequirement.EMPTY;
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, RecipeRequirement recipeRequirement) {
            if(recipeRequirement == null)
                recipeRequirement = new MachineRequirement();
            if(recipeRequirement instanceof MachineRequirement){
                friendlyByteBuf.writeBoolean(recipeRequirement.isPresent());
                if(recipeRequirement.isPresent())
                    friendlyByteBuf.writeUtf(((MachineRequirement) recipeRequirement).toString());
            }
        }
    }
}
