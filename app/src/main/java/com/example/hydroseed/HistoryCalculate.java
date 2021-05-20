package com.example.hydroseed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;

import static com.example.hydroseed.Global.ACRE_TO_SQFT;
import static com.example.hydroseed.Global.applicationRates;

public class HistoryCalculate extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        Intent intent = getIntent();
        CalculationObject historyItem = intent.getParcelableExtra("hist");
        double acreage = historyItem.getAcres();
        int compostLayers = historyItem.getCompostLayer();
        int hydroSeed = historyItem.getHydroSeedLayer();
        int lbsOfSeed = historyItem.getLbsOfSeed();
        int lbsOfFertilizer = historyItem.getLbsOfFertilizer();
        int lbsOfAdditives = historyItem.getLbsOfAdditive();
        int hydroMulch = historyItem.getHydroMulchLayer();
        int hydroSeedLayers = historyItem.getHydroSeedLayer();




        double input = Global.userInputAcres;
        //Retriever user input tank size from Global.java
        double tankSize = Global.tankSize;

        //Calculations where we the input is multiplied with all of the DEFAULT application rates
        double inputAcresToCubic = input * ACRE_TO_SQFT;
        double compostCalc = Math.ceil((inputAcresToCubic * ((double) 2 / 12)) / (applicationRates[0] / 10));

        double seedCalc = input * applicationRates[1];
        double fiberSeedCalc = input * applicationRates[2];
        double fertilizer = input * applicationRates[3];
        double endroAdditive = input * applicationRates[4];
        double fiberMulchCalc = input * applicationRates[5];
        double tackCalc = input * applicationRates[6];

        //Calculations where the application rates are added together for each 3 steps
        //Step 1: Compost Layer
        //Step 2: Hydroseed:  Seed + Fiber + Fertilizer + Endomichorrizal
        //Step 3: Hydromulch: Fiber + Tackifier
        int compostLayer = (int) compostCalc;
        int hydroSeedLayer = (int) (seedCalc + fiberSeedCalc + fertilizer + endroAdditive);
        int hydroMulchLayer = (int) (fiberMulchCalc + tackCalc);


        double bagsOfFertilizer = Math.ceil(fertilizer / 50);
        double bagsOfHydroMulch = Math.ceil((double) hydroMulchLayer / 50);
        double tanksNeeded = Math.ceil(bagsOfHydroMulch / (tankSize / 100));


        //Store each calculation into an object with field variables as each 3 steps and the user input
        CalculationObject calculation = new CalculationObject(input, compostLayer, hydroSeedLayer, (int) seedCalc, (int) fertilizer, (int) endroAdditive, hydroMulchLayer, (int) tanksNeeded);

        //Store each calculation object into the global history list/linked hash set if meets range requirements
        if (Global.historyList.size() == 0) {
            Global.historyList.add(0, calculation);
        } else if (Global.historyList.size() < Global.historySize) {
            Global.historyList.add(0, calculation);
        } else {
            Global.historyList.remove(4);
            Global.historyList.add(0, calculation);
        }


        //Displaying of the results of calculations from previous steps above
        String acreageAmount = String.format("%s or %d square foot", historyItem.toStringAcreageAmount(), (int) (input * ACRE_TO_SQFT));
        TextView a = findViewById(R.id.acreage);
        a.setText(acreageAmount);

        ((TextView) findViewById(R.id.compost)).setText(historyItem.toStringCompostAmount());
        ((TextView) findViewById(R.id.hydroSeed)).setText(historyItem.toStringHydroSeedAmount());
        ((TextView) findViewById(R.id.hydroSeed_seed)).setText(historyItem.toStringHydroSeedAmount_seed());
        ((TextView) findViewById(R.id.hydroSeed_fiber)).setText(String.valueOf((int)fiberSeedCalc) + " lbs");
        String hydroSeedAmount_fiber = String.format("%.2f lbs", applicationRates[2]);
        ((TextView) findViewById(R.id.textRate2)).setText(hydroSeedAmount_fiber);
        String hydroSeed_fertilizerBags = String.format("%d bag(s)", (int) bagsOfFertilizer);
        ((TextView) findViewById(R.id.hydroSeed_fertilizer)).setText(hydroSeed_fertilizerBags);
        ((TextView) findViewById(R.id.hydroSeed_additive)).setText(historyItem.toStringHydroSeedAmount_additive());
        ((TextView) findViewById(R.id.hydroMulch)).setText(historyItem.toStringHydroMulchAmount());
        ((TextView) findViewById(R.id.hydroMulch_fiber)).setText(String.valueOf((int)fiberMulchCalc) + " lbs");
        ((TextView) findViewById(R.id.hydroMulch_tackifier)).setText(String.valueOf((int)tackCalc) + " lbs");
        //String hydroMulchAmount_bags = String.format("%d tanks of size %d for %d bag(s)", (int) tanksNeeded, (int) tankSize, (int) bagsOfHydroMulch);
        //((TextView) findViewById(R.id.hydroMulch_tankNeeded)).setText(hydroMulchAmount_bags);

        String applicationRate0 = String.format("%.2f CY / acre", applicationRates[0]);
        String applicationRate1 = String.format("%.2f lbs", applicationRates[1]);
        //String applicationRate2 = "" + applicationRates[2];
        String applicationRate3 = String.format("%.2f lbs", applicationRates[3]);
        String applicationRate4 = String.format("%.2f lbs", applicationRates[4]);
        String applicationRate5 = String.format("%.2f lbs", applicationRates[5]);
        String applicationRate6 = String.format("%.2f lbs", applicationRates[6]);

        ((TextView) findViewById(R.id.textRate0)).setText(applicationRate0);
        ((TextView) findViewById(R.id.textRate1)).setText(applicationRate1);
        //((TextView)findViewById(R.id.textRate2)).setText(applicationRate2);
        ((TextView) findViewById(R.id.textRate3)).setText(applicationRate3);
        ((TextView) findViewById(R.id.textRate4)).setText(applicationRate4);
        ((TextView) findViewById(R.id.textRate5)).setText(applicationRate5);
        ((TextView) findViewById(R.id.textRate6)).setText(applicationRate6);

    }
}
