package com.example.hydroseed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, buttonC;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Numpad Buttons
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button10 = (Button) findViewById(R.id.button10);
        buttonC = (Button) findViewById(R.id.buttonC);
        editText = (EditText) findViewById(R.id.acreageSource);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "0");
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + ".");
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        Switch switchSquareFoot = findViewById(R.id.sqftSwitch);
        switchSquareFoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText acreInput = findViewById(R.id.acreageSource);
                if(isChecked) {
                    switchSquareFoot.setText("Sqft");
                    acreInput.setHint("Enter sqft");
                } else {
                    switchSquareFoot.setText("Acres");
                    acreInput.setHint("Enter acres");
                }
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchTankSize = findViewById(R.id.tankSwitch);
        switchTankSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    switchTankSize.setText("3000");
                } else {
                    switchTankSize.setText("1500");
                }
            }
        });

    }

    public void calculateButton(View v) {

        EditText acreInput = findViewById(R.id.acreageSource);
        String acres = acreInput.getText().toString();
        double number = 0;

        if(!acres.equals("")) {
            number = Double.parseDouble(acres);
        }

        if(number > 0) {
            Intent toApplicationRatePage = new Intent(this, ApplicationRatePage.class);

            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchTankSize = findViewById(R.id.tankSwitch);
            if(switchTankSize.getText().toString().equals("1500")) {
                Global.tankSize = 1500;
            } else {
                Global.tankSize = 3000;
            }

            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch squareFootSwitch = findViewById(R.id.sqftSwitch);
            if(squareFootSwitch.getText().toString().equals("Acres")) {
                Global.userInputAcres = number;
            } else {
                Global.userInputSqft = number;
            }
            startActivity(toApplicationRatePage);
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