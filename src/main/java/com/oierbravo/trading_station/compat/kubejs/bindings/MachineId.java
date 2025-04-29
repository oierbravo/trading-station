package com.oierbravo.trading_station.compat.kubejs.bindings;

import com.oierbravo.trading_station.content.trading_recipe.MachineRequirement;

public class MachineId {
    public static MachineRequirement of(String... machineId){
        return MachineRequirement.of(machineId);
    }
}
