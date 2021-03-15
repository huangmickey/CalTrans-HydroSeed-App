package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class History extends AppCompatActivity {

    @Override
    //Steven's task
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        int id = 0;
        for(CalculationObject calculation : Global.historyList) {
            String compostLayer = "You need " + calculation.getCompostLayer() + " cubic yards of compost";
            String hydroSeedLayer = "You need " + calculation.getHydroSeedLayer() + " lbs of hydro seed";
            String hydroMuchLayer = "You need " + calculation.getHydroMulchLayer() + "lbs of hydro mulch";
            String builder = compostLayer + "\n" + hydroSeedLayer + "\n" + hydroMuchLayer;
            ((TextView)findViewById(R.id.textView3 + id)).setText(builder);
            id++;
        }
    }


}