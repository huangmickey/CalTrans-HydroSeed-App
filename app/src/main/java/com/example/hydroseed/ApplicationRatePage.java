package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ApplicationRatePage extends AppCompatActivity {
    String customRate;
    EditText customRateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_rate_page);
        setApplicationRateHint();
    }

    public void goToCalculatePage(View view) {
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

    public void getDefaultRates(View view) {
        Global.applicationRates[0] = 270;
        Global.applicationRates[1] = 65;
        Global.applicationRates[2] = 1000;
        Global.applicationRates[3] = 1700;
        Global.applicationRates[4] = 60;
        Global.applicationRates[5] = 2500;
        Global.applicationRates[6] = 175;
        setApplicationRateHint();
    }

    public void setApplicationRateHint() {
        for(int i = 0; i < 7; i++) {
            customRateEditText = findViewById(R.id.applicationRate0 + i);
            customRateEditText.setHint(""+Global.applicationRates[i]);
        }
    }
}