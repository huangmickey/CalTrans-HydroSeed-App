package com.example.hydroseed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class CalculateFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    Button button0, button1, button2, button3,
            button4, button5, button6, button7, button8,
            button9, button10, buttonC, history, calculate;
    //SwitchMaterial switchSquareFoot, switchTankSize;
    EditText editText;

    public CalculateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculate, container, false);
        SwitchMaterial sqftSwitch = view.findViewById(R.id.sqftSwitch);
        SwitchMaterial tankSwitch = view.findViewById(R.id.tankSwitch);
        editText = view.findViewById(R.id.acreageSource);
        calculate = view.findViewById(R.id.calculate);
        history = view.findViewById(R.id.historyButton);
        button0 = view.findViewById(R.id.button0);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);
        button5 = view.findViewById(R.id.button5);
        button6 = view.findViewById(R.id.button6);
        button7 = view.findViewById(R.id.button7);
        button8 = view.findViewById(R.id.button8);
        button9 = view.findViewById(R.id.button9);
        button10 = view.findViewById(R.id.button10);
        buttonC = view.findViewById(R.id.buttonC);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        buttonC.setOnClickListener(this);
        history.setOnClickListener(this);
        sqftSwitch.setOnCheckedChangeListener(this);
        tankSwitch.setOnCheckedChangeListener(this);
        calculate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        editText.setText("");
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                editText.setText((editText.getText()).append("0").toString());
                break;
            case R.id.button1:
                editText.setText((editText.getText()).append("1").toString());
                break;
            case R.id.button2:
                editText.setText((editText.getText()).append("2").toString());
                break;
            case R.id.button3:
                editText.setText((editText.getText()).append("3").toString());
                break;
            case R.id.button4:
                editText.setText((editText.getText()).append("4").toString());
                break;
            case R.id.button5:
                editText.setText((editText.getText()).append("5").toString());
                break;
            case R.id.button6:
                editText.setText((editText.getText()).append("6").toString());
                break;
            case R.id.button7:
                editText.setText((editText.getText()).append("7").toString());
                break;
            case R.id.button8:
                editText.setText((editText.getText()).append("8").toString());
                break;
            case R.id.button9:
                editText.setText((editText.getText()).append("9").toString());
                break;
            case R.id.button10:
                editText.setText((editText.getText()).append(".").toString());
                break;
            case R.id.buttonC:
                editText.setText("");
                break;
            case R.id.historyButton:
                Toast.makeText(getContext(), "History!", Toast.LENGTH_SHORT).show();
                Intent toHistoryPage = new Intent(getContext(), History.class);
                startActivity(toHistoryPage);
                break;
            case R.id.calculate:
               // Toast.makeText(getContext(),"Calc", Toast.LENGTH_SHORT).show();
                String acres = editText.getText().toString();

                double number = 0;

                if (!acres.equals("")) {
                    number = Double.parseDouble(acres);

                    SwitchMaterial sqftSwitch = getView().findViewById(R.id.sqftSwitch);
                    if (sqftSwitch.getText().toString().equals("Acres")) {
                        Global.userInputAcres = number;
                        if(number > 0 && number < 1000) {
                            Intent toApplicationRatePage = new Intent(getContext(), ApplicationRatePage.class);
                            startActivity(toApplicationRatePage);
                        } else {
                            DialogPopUpAcres popup = new DialogPopUpAcres();
                            popup.show(requireActivity().getSupportFragmentManager(), "");
                        }


                    } else {
                        Global.userInputSqft = number;
                        if(number > 0 && number < 43560000) {
                            Intent toApplicationRatePage = new Intent(getContext(), ApplicationRatePage.class);
                            startActivity(toApplicationRatePage);
                        } else {
                            DialogPopUpSqft popup = new DialogPopUpSqft();
                            popup.show(requireActivity().getSupportFragmentManager(), "");
                        }
                    }

                } else {
                    DialogPopUpEmpty popup = new DialogPopUpEmpty();
                    popup.show(requireActivity().getSupportFragmentManager(), "");
                }

                SwitchMaterial switchTankSize = getView().findViewById(R.id.tankSwitch);
                if (switchTankSize.getText().toString().equals("1500")) {
                    Global.tankSize = 1500;
                } else {
                    Global.tankSize = 3000;
                }
                break;
                /*if (number > 0) {

                    SwitchMaterial sqftSwitch = getView().findViewById(R.id.sqftSwitch);
                    if (sqftSwitch.getText().toString().equals("Acres")) {
                        Global.userInputAcres = number;
                    } else {
                        Global.userInputSqft = number;
                    }
                    Intent toApplicationRatePage = new Intent(getContext(), ApplicationRatePage.class);
                    startActivity(toApplicationRatePage);

                } else {
                    CharSequence text = "Error! Please enter a valid input greater than 0";
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }*/
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sqftSwitch:
                if (isChecked) {
                    buttonView.setText("Sqft");
                    editText.setHint("Enter sqft");
                } else {
                    buttonView.setText("Acres");
                    editText.setHint("Enter acres");
                }
                break;
            case R.id.tankSwitch:
                if (isChecked) {
                    buttonView.setText("3000");
                } else {
                    buttonView.setText("1500");
                }
                break;
        }
    }
}