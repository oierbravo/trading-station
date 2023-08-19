package com.oierbravo.trading_station.foundation.gui;

public class Coords2D {
    public int x;
    public int y;
    public Coords2D(int pX, int pY){
        x = pX;
        y = pY;
    }
    public static Coords2D of(int pX, int pY){
        return new Coords2D(pX,pY);
    }
}
