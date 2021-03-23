package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.hydroseed.Global.ACRE_TO_SQFT;

public class ApplicationRatePage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_rate_page);

        /*Intent getInput = getIntent();

        double tankSize = getInput.getDoubleExtra("Tank Size", 0.0);

        double inputSquareFoot = getInput.getDoubleExtra("Square Foot", 0.0);
        double inputAcres = getInput.getDoubleExtra("Acreage", 0.0);
        double userInput;

        if(inputSquareFoot != 0) {
            userInput = inputSquareFoot / ACRE_TO_SQFT;
        } else {
            userInput = inputAcres;
        }*/


        /*double compostRate = Double.parseDouble(findViewById(R.id.customCompostRate).toString());

        double seedRate = Double.parseDouble(findViewById(R.id.customSeedRate).toString());
        double fertilizerRate = Double.parseDouble(findViewById(R.id.customFertilizerRate).toString());
        double hydroSeedFiberRate = Double.parseDouble(findViewById(R.id.customHydroSeedFiberRate).toString());
        double additiveRate = Double.parseDouble(findViewById(R.id.customAdditiveRate).toString());

        double hydroMulchFiberRate = Double.parseDouble(findViewById(R.id.customHydroMulchFiberRate).toString());
        double tackifierRate = Double.parseDouble(findViewById(R.id.customTackifierRate).toString());*/
    }

    public void goToCalculatePage(View v) {

        EditText customRateEditText = findViewById(R.id.applicationRate0);
        String customRate = customRateEditText.getText().toString();

        Intent toCalcPage = new Intent(this, CalculatePage.class);

        for(int i = 0; i < 7; i++) {
            customRateEditText = findViewById(R.id.applicationRate0 + i);
            customRate = customRateEditText.getText().toString();

            if(!customRate.equals("")) {
                Global.applicationRates[i] = Double.parseDouble(customRate);
            }
        }


        startActivity(toCalcPage);
    }

}