package com.nnedimkpa.greenhouse.model;

/**
 * Created by Nnedimkpa on 1/31/2017.
 */

public class Reading {

    private long date;
    private long innerTemperature;
    private long humidity;
    private long light;
    private long waterLevel;

    public Reading() {

    }

    public float getDate() {
        return date;
    }

    public void setDate(String date) {

        this.date = convertDate(date);
    }

    public long getInnerTemperature() {
        return innerTemperature;
    }

    public void setInnerTemperature(long innerTemperature) {
        this.innerTemperature = innerTemperature;
    }

    public long getHumidity() {
        return humidity;
    }

    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public long getLight() {
        return light;
    }

    public void setLight(long light) {
        this.light = light;
    }

    public long getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(long waterLevel) {

        this.waterLevel = waterLevel;
    }

    private Long convertDate(String dateText){

        return null;
    }

}
