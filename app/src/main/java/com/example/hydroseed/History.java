package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        int id = 0;
        for (CalculationObject calculation : Global.historyList) {
            String getAcres = "For " + calculation.getAcres() + " acres of land you need: ";
            String compostLayer = "       " + calculation.getCompostLayer() + " yds³ of Compost";
            String hydroSeedLayer = "       " + calculation.getHydroSeedLayer() + " lbs of Hydroseed";
            String hydroMuchLayer = "       " + calculation.getHydroMulchLayer() + " lbs of Hydromulch";
            String historyExtend = " -- Click Here for Extended Info --";
            String builder = getAcres + "\n" + compostLayer + "\n" + hydroSeedLayer + "\n" + hydroMuchLayer + "\n" + historyExtend;
            ((TextView) findViewById(R.id.textView5 - id)).setText(builder);
            TextView txt = ((TextView) findViewById(R.id.textView5 - id));
            if ((TextView) findViewById(R.id.textView5 - id) != null)
                txt.setVisibility(View.VISIBLE);
            id++;
        }
    }
    public void onClick(View v) {
        Intent intent = new Intent(this,CalculatePage.class);
        startActivity(intent);

    }
}