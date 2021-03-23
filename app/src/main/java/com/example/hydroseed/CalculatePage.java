package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static com.example.hydroseed.Global.ACRE_TO_SQFT;
import static com.example.hydroseed.Global.applicationRates;


public class CalculatePage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_page);
        //Retrieve user input from Global.java ::: Main page input is passed to a container in Global.java so every page can access userInput
        double input;
        if(Global.userInputSqft != 0) {
            input = Global.userInputSqft / ACRE_TO_SQFT;
        } else {
            input = Global.userInputAcres;
        }
        //Retriever user input tank size from Global.java
        double tankSize = Global.tankSize;

        //Round up input to highest fourth increment in decimals for user input in acres
        input = Math.ceil(4 * input) / 4;

        //Calculations where we the input is multiplied with all of the DEFAULT application rates
        double inputAcresToCubic = input * ACRE_TO_SQFT;
        double compostCalc = Math.ceil((inputAcresToCubic * ((double)2/12)) / (applicationRates[0]/10));

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
        int compostLayer = (int)compostCalc;
        int hydroSeedLayer = (int)(seedCalc + fiberSeedCalc + fertilizer + endroAdditive);
        int hydroMulchLayer = (int)(fiberMulchCalc + tackCalc);


        double bagsOfFertilizer = Math.ceil(fertilizer / 50);
        double bagsOfHydroMulch = Math.ceil((double)hydroMulchLayer / 50);
        double tanksNeeded = Math.ceil(bagsOfHydroMulch / (tankSize / 100) );



        //Store each calculation into an object with field variables as each 3 steps and the user input
        CalculationObject calculation = new CalculationObject(input, compostLayer, hydroSeedLayer,(int)seedCalc,(int)fertilizer,(int)endroAdditive,hydroMulchLayer,(int)tanksNeeded);

        //Store each calculation object into the global history list/linked hash set if meets range requirements
        if (Global.historyList.size() == 0) {
            Global.historyList.add(0,calculation);
            Global.dupe = new LinkedHashSet<>(Global.historyList);
            Global.historyList2 = new ArrayList<>(Global.dupe);
        } else if (Global.historyList.size() < Global.historySize) {
            Global.historyList.add(0,calculation);
            Global.dupe = new LinkedHashSet<>(Global.historyList);
            Global.historyList2 = new ArrayList<>(Global.dupe);
        } else {
            Global.historyList.remove(4);
            Global.historyList.add(0,calculation);
            Global.dupe = new LinkedHashSet<>(Global.historyList);
            Global.historyList2 = new ArrayList<>(Global.dupe);
        }


        //Displaying of the results of calculations from previous steps above
        String acreageAmount = "Results for " + input + " acres or " + (int)(input*ACRE_TO_SQFT) + " square foot";
        TextView a = findViewById(R.id.acreage);
        a.setText(acreageAmount);

        String compostText = "You need " + compostLayer + " cubic yards of compost";
        ((TextView)findViewById(R.id.compost)).setText(compostText);

        String hydroSeedText = "You need " + hydroSeedLayer + " lbs of hydro seed";
        ((TextView)findViewById(R.id.hydroSeed)).setText(hydroSeedText);

        String hydroSeedMix = "In the HydroSeed Mixture you need:\n" + (int)seedCalc + "lbs of seed\n" +
                (int)fertilizer + "lbs of fertilizer which is " + (int)bagsOfFertilizer + " bags in 50lb increments\n" + (int)endroAdditive + "lbs of other additive";
        ((TextView)findViewById(R.id.hydroSeedMore)).setText(hydroSeedMix);

        String hydroMulchText = "You need " + hydroMulchLayer + " lbs of hydro mulch";
        ((TextView)findViewById(R.id.hydroMulch)).setText(hydroMulchText);

        String tankText = "You need " + (int)tanksNeeded + " tanks for " + (int)bagsOfHydroMulch + " bags of Hydromulch with a tank size of " + (int)tankSize + " gallons";
        ((TextView)findViewById(R.id.tanksNeeded)).setText(tankText);



        String applicationRate0 = "" + applicationRates[0];
        String applicationRate1 = "" + applicationRates[1];
        String applicationRate2 = "" + applicationRates[2];
        String applicationRate3 = "" + applicationRates[3];
        String applicationRate4 = "" + applicationRates[4];
        String applicationRate5 = "" + applicationRates[5];
        String applicationRate6 = "" + applicationRates[6];

        ((TextView)findViewById(R.id.textRate0)).setText(applicationRate0);
        ((TextView)findViewById(R.id.textRate1)).setText(applicationRate1);
        ((TextView)findViewById(R.id.textRate2)).setText(applicationRate2);
        ((TextView)findViewById(R.id.textRate3)).setText(applicationRate3);
        ((TextView)findViewById(R.id.textRate4)).setText(applicationRate4);
        ((TextView)findViewById(R.id.textRate5)).setText(applicationRate5);
        ((TextView)findViewById(R.id.textRate6)).setText(applicationRate6);

        ((TextView)findViewById(R.id.tankSize)).setText(""+tankSize);

    }




}