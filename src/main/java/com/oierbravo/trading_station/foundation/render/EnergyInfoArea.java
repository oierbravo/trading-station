package com.oierbravo.trading_station.foundation.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  https://github.com/BluSunrize/ImmersiveEngineering/blob/1.19.2/LICENSE
 *
 *  Slightly Modified Version by: oierbravo
 */
public class EnergyInfoArea extends InfoArea
{
    private final IEnergyStorage energy;

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy)
    {
        super(new Rect2i(xMin, yMin, 9, 30));
        this.energy = energy;
    }

    @Override
    protected void fillTooltipOverArea(int mouseX, int mouseY, List<Component> tooltip)
    {
        tooltip.add(Component.literal(energy.getEnergyStored()+"/"+energy.getMaxEnergyStored()+" IF"));
    }

    @Override
    public void draw(GuiGraphics graphics)
    {
        final int height = area.getHeight();
        int stored = (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
        graphics.fillGradient(
                area.getX(), area.getY()+(height-stored),
                area.getX()+area.getWidth(), area.getY()+area.getHeight(),
                0xffb51500, 0xff600b00
        );
    }
}