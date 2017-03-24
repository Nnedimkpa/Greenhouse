package com.nnedimkpa.greenhouse.model;

/**
 * Created by Digz on 18/03/2017.
 */

public class GreenhouseSettings {
    private int automatic;
    private int bulb;
    private int coolingFan;
    private int exhaustFan;
    private int light;
    private int pump;

    public GreenhouseSettings() {
    }

    public void setBulb(int bulb) {
        this.bulb = bulb;
    }

    public void setCoolingFan(int coolingFan) {
        this.coolingFan = coolingFan;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public void setPump(int pump) {
        this.pump = pump;
    }

    public void setExhaustFan(int exhaustFan) {
        this.exhaustFan = exhaustFan;
    }

    public boolean getBulb() {
        return bulb == 1;
    }

    public boolean getCoolingFan() {
        return coolingFan ==1;
    }

    public boolean getExhaustFan() {
        return exhaustFan == 1;
    }

    public boolean getLight() {
        return light == 1;
    }

    public boolean getPump() {
        return pump == 1;
    }

    public boolean getAutomatic() {
        return automatic == 1;
    }

    public void setAutomatic(int automatic) {
        this.automatic = automatic;
    }
}
