package com.example.hydroseed;

public class CalculationObject {

    private double acres;
    private int compostLayer;
    private int hydroSeedLayer;
    private int lbsOfSeed;
    private int lbsOfFertilizer;
    private int lbsOfAdditive;
    private int hydroMulchLayer;
    private int tanksNeeded;


    public CalculationObject(double acres, int compostLayer, int hydroSeedLayer,int lbsOfSeed, int lbsOfFertilizer, int lbsOfAdditive, int hydroMulchLayer, int tanksNeeded) {
        this.acres = acres;
        this.compostLayer = compostLayer;
        this.hydroMulchLayer = hydroMulchLayer;
        this.lbsOfSeed = lbsOfSeed;
        this.lbsOfFertilizer = lbsOfFertilizer;
        this.lbsOfAdditive = lbsOfAdditive;
        this.hydroSeedLayer = hydroSeedLayer;
        this.tanksNeeded = tanksNeeded;

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
