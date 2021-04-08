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