package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalculatePage extends AppCompatActivity {

    static final int ACRE_TO_CY = 43560;
    static final int COMPOST_RATE = 27;
    static final int SEED_RATE = 65;
    static final int FIBER_HYDRO_SEED_RATE = 1000;
    static final int FIBER_HYDRO_MULCH_RATE = 2500;
    static final int FERTILIZER_RATE = 1700;
    static final int TACK_RATE = 175;
    static final int ENDRO_RATE = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_page);

        Intent getAcreage = getIntent();

        double inputAcres = getAcreage.getIntExtra("Acreage", 0);

        inputAcres = Math.ceil(4 * inputAcres) / 4;

        double inputAcresToCubic = inputAcres * ACRE_TO_CY;
        double compostCalc = Math.ceil((inputAcresToCubic * ((double)2/12)) / COMPOST_RATE);

        double seedCalc = inputAcres * SEED_RATE;
        double fiberSeedCalc = inputAcres * FIBER_HYDRO_SEED_RATE;
        double fiberMulchCalc = inputAcres * FIBER_HYDRO_MULCH_RATE;
        double fertilizer = inputAcres * FERTILIZER_RATE;
        double endroAdditive = inputAcres * ENDRO_RATE;
        double tackCalc = inputAcres * TACK_RATE;

        int compostLayer = (int)compostCalc;
        int hydroSeedLayer = (int)(seedCalc + fiberSeedCalc + fertilizer + endroAdditive);
        int hydroMulchLayer = (int)(fiberMulchCalc + tackCalc);

        CalculationObject calculation = new CalculationObject(inputAcres, compostLayer, hydroSeedLayer, hydroMulchLayer);


        if(Global.historyList.size() == 5) {
            Global.historyList.remove(0);
        } else if(Global.historyList.size() >= 0 && Global.historyList.size() <= 5) {
            Global.historyList.add(calculation);
        } else {
            for(CalculationObject calc : Global.historyList) {
                if(calc.getAcres() == inputAcres) {
                    break;
                }
            }
        }

        String acreageAmount = "Results for " + inputAcres + " acres";
        TextView a = findViewById(R.id.acreage);
        a.setText(acreageAmount);

        String compostText = "You need " + compostLayer + " cubic yards of compost";
        ((TextView)findViewById(R.id.compost)).setText(compostText);

        String hydroSeedText = "You need " + hydroSeedLayer + " lbs of hydro seed";
        ((TextView)findViewById(R.id.hydroSeed)).setText(hydroSeedText);

        String hydroMulchText = "You need " + hydroMulchLayer + " lbs of hydro mulch";
        ((TextView)findViewById(R.id.hydroMulch)).setText(hydroMulchText);

    }




}