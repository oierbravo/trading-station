package com.oierbravo.trading_station.foundation.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BasicButton extends Button {

    public BasicButton(int x, int y, int widthIn, int heightIn, Component buttonText, OnPress action) {
        super(x, y, widthIn, heightIn, buttonText, action, Button.DEFAULT_NARRATION);
    }
}
