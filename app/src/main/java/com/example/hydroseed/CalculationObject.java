package com.example.hydroseed;

import android.os.Parcel;
import android.os.Parcelable;

public class CalculationObject implements Parcelable {

    private double acres;
    private int compostLayer;
    private int hydroSeedLayer;
    private int lbsOfSeed;
    private int lbsOfFertilizer;
    private int lbsOfAdditive;
    private int hydroMulchLayer;
    private int tanksNeeded;



    public CalculationObject(double acres, int compostLayer, int hydroSeedLayer, int lbsOfSeed, int lbsOfFertilizer, int lbsOfAdditive, int hydroMulchLayer, int tanksNeeded) {
        this.acres = acres;
        this.compostLayer = compostLayer;
        this.hydroMulchLayer = hydroMulchLayer;
        this.lbsOfSeed = lbsOfSeed;
        this.lbsOfFertilizer = lbsOfFertilizer;
        this.lbsOfAdditive = lbsOfAdditive;
        this.hydroSeedLayer = hydroSeedLayer;
        this.tanksNeeded = tanksNeeded;
        
    }


    protected CalculationObject(Parcel in) {
        acres = in.readDouble();
        compostLayer = in.readInt();
        hydroSeedLayer = in.readInt();
        lbsOfSeed = in.readInt();
        lbsOfFertilizer = in.readInt();
        lbsOfAdditive = in.readInt();
        hydroMulchLayer = in.readInt();
        tanksNeeded = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(acres);
        dest.writeInt(compostLayer);
        dest.writeInt(hydroSeedLayer);
        dest.writeInt(lbsOfSeed);
        dest.writeInt(lbsOfFertilizer);
        dest.writeInt(lbsOfAdditive);
        dest.writeInt(hydroMulchLayer);
        dest.writeInt(tanksNeeded);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CalculationObject> CREATOR = new Creator<CalculationObject>() {
        @Override
        public CalculationObject createFromParcel(Parcel in) {
            return new CalculationObject(in);
        }

        @Override
        public CalculationObject[] newArray(int size) {
            return new CalculationObject[size];
        }
    };

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

    public int getLbsOfSeed() { return lbsOfSeed; }

    public void setLbsOfSeed(int lbsOfSeed) {
        this.lbsOfSeed = lbsOfSeed;
    }

    public int getLbsOfFertilizer() {
        return lbsOfFertilizer;
    }

    public void setLbsOfFertilizer(int lbsOfFertilizer) {
        this.lbsOfFertilizer = lbsOfFertilizer;
    }

    public int getLbsOfAdditive() {
        return lbsOfAdditive;
    }

    public void setLbsOfAdditive(int lbsOfAdditive) {
        this.lbsOfAdditive = lbsOfAdditive;
    }

    public int getTanksNeeded() {
        return tanksNeeded;
    }

    public void setTanksNeeded(int tanksNeeded) {
        this.tanksNeeded = tanksNeeded;
    }

    public String toStringAcreageAmount(){ return this.acres + " acres"; }
    public String toStringCompostAmount(){ return this.compostLayer + " CY";}

    public String toStringHydroSeedAmount(){ return this.hydroSeedLayer + " lbs";}
    public String toStringHydroSeedAmount_seed(){return this.lbsOfSeed + " lbs";}
    public String toStringHydroSeedAmount_fertilizer(){return this.lbsOfFertilizer + " lbs";}
    public String toStringHydroSeedAmount_additive(){return this.lbsOfAdditive + " lbs";}

    public String toStringHydroMulchAmount(){ return this.hydroMulchLayer + " lbs";}
    public String toStringHydroMulchAmount_tanks(){return this.tanksNeeded + " tanks";}


}
