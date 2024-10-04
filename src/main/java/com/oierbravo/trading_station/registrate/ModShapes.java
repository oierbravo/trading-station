package com.oierbravo.trading_station.registrate;

import com.oierbravo.mechanical_lemon_ui.foundation.utility.ShapeBuilder;
import com.oierbravo.mechanical_lemon_ui.foundation.utility.VoxelShaper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModShapes {
    public static final VoxelShape
        TRADING_STATION = ShapeBuilder
            .shape(0,0,0,16,2,16)
            .add(1,2,1,15,14,15)
            .add(0,14,0,16,14,16)
            .add(4,16,3,12,18,13)
            .build();


}
