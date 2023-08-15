package com.oierbravo.trading_station.foundation.gui;

import com.oierbravo.trading_station.content.trading_station.TradingStationScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class BackButton extends ExtendedButton {
    public BackButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }
   /* public BackButton(int pX, int pY, AbstractContainerScreen<TradingStationScreen> parentScreen){
        this(pX,pY,16,16,Component.literal("Back"), btn->{
            this.
        })
    }*/
}
