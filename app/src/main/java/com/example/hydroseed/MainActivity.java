package com.example.hydroseed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calculateButton(View v) {

        EditText acreInput = findViewById(R.id.acreageSource);
        String acres = acreInput.getText().toString();
        int number = 0;

        if(!acres.equals("")) {
            number = Integer.parseInt(acres);
        }

        if(number > 0) {
            Intent toCalcPage = new Intent(this, CalculatePage.class);
            toCalcPage.putExtra("Acreage", number);
            startActivity(toCalcPage);

        } else {
            Context context = getApplicationContext();
            CharSequence text = "Error! Please enter a valid number starting from 1";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public void historyButton(View v) {

        Intent toHistoryPage = new Intent(this, History.class);
        startActivity(toHistoryPage);

    }
}