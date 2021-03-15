package com.example.hydroseed;

public class CalculationObject {
    private double acres;

    private int compostLayer;
    private int hydroSeedLayer;
    private int hydroMulchLayer;

    public CalculationObject(double acres, int compostLayer, int hydroSeedLayer, int hydroMulchLayer) {
        this.acres = acres;
        this.compostLayer = compostLayer;
        this.hydroMulchLayer = hydroMulchLayer;
        this.hydroSeedLayer = hydroSeedLayer;
    }

    public double getAcres() {
        return acres;
    }

    public void setAcres(int acres) {
        this.acres = acres;
    }

    public int getCompostLayer() {
        return compostLayer;
    }

    public void setCompostLayer(int compostLayer) {
        this.compostLayer = compostLayer;
    }

    public int getHydroSeedLayer() {
        return hydroSeedLayer;
    }

    public void setHydroSeedLayer(int hydroSeedLayer) {
        this.hydroSeedLayer = hydroSeedLayer;
    }

    public int getHydroMulchLayer() {
        return hydroMulchLayer;
    }

    public void setHydroMulchLayer(int hydroMulchLayer) {
        this.hydroMulchLayer = hydroMulchLayer;
    }


}
