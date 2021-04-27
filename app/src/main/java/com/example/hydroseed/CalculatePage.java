package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static com.example.hydroseed.Global.ACRE_TO_SQFT;
import static com.example.hydroseed.Global.applicationRates;
import static com.example.hydroseed.Global.tankSize;

public class CalculatePage extends AppCompatActivity {
    private static String projectName = "";
    private static final String historyData = "historyData.txt";
    EditText nameEditText, numbEditText;
// ADD ALERT DIALOG TO ASK USER TO SAVE / EXPORT FILE AS .txt, .csv, or BOTH

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_page);
        //Retrieve user input from Global.java ::: Main page input is passed to a container in Global.java so every page can access userInput

        nameEditText = findViewById(R.id.calc_name);//Project name Edit Text Field
        numbEditText = findViewById(R.id.calc_number);//Project name Edit Text Field

        double input;
        if (Global.userInputSqft != 0) {
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
        double compostCalc = Math.ceil((inputAcresToCubic * ((double) 2 / 12)) / (applicationRates[0] / 10));

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
        int compostLayer = (int) compostCalc;
        int hydroSeedLayer = (int) (seedCalc + fiberSeedCalc + fertilizer + endroAdditive);
        int hydroMulchLayer = (int) (fiberMulchCalc + tackCalc);


        double bagsOfFertilizer = Math.ceil(fertilizer / 50);
        double bagsOfHydroMulch = Math.ceil((double) hydroMulchLayer / 50);
        double tanksNeeded = Math.ceil(bagsOfHydroMulch / (tankSize / 100));


        //Store each calculation into an object with field variables as each 3 steps and the user input
        CalculationObject calculation = new CalculationObject(input, compostLayer, hydroSeedLayer, (int) seedCalc, (int) fertilizer, (int) endroAdditive, hydroMulchLayer, (int) tanksNeeded);

        //Store each calculation object into the global history list/linked hash set if meets range requirements
        if (Global.historyList.size() == 0) {
            Global.historyList.add(0, calculation);
        } else if (Global.historyList.size() < Global.historySize) {
            Global.historyList.add(0, calculation);
        } else {
            Global.historyList.remove(4);
            Global.historyList.add(0, calculation);
        }


        //Displaying of the results of calculations from previous steps above
        String acreageAmount = String.format("%s or %d square foot", calculation.toStringAcreageAmount(), (int) (input * ACRE_TO_SQFT));
        TextView a = findViewById(R.id.acreage);
        a.setText(acreageAmount);

        ((TextView) findViewById(R.id.compost)).setText(calculation.toStringCompostAmount());

        ((TextView) findViewById(R.id.hydroSeed)).setText(calculation.toStringHydroSeedAmount());
        ((TextView) findViewById(R.id.hydroSeed_seed)).setText(calculation.toStringHydroSeedAmount_seed());
        ((TextView) findViewById(R.id.hydroSeed_fiber)).setText(String.valueOf(fiberSeedCalc) + " lbs");
        String hydroSeedAmount_fiber = String.format("%.2f lbs / acre", applicationRates[2]);
        ((TextView) findViewById(R.id.textRate2)).setText(hydroSeedAmount_fiber);
        String hydroSeed_fertilizerBags = String.format("%d bag(s)", (int) bagsOfFertilizer);
        ((TextView) findViewById(R.id.hydroSeed_fertilizer)).setText(hydroSeed_fertilizerBags);
        ((TextView) findViewById(R.id.hydroSeed_additive)).setText(calculation.toStringHydroSeedAmount_additive());

        ((TextView) findViewById(R.id.hydroMulch)).setText(calculation.toStringHydroMulchAmount());
        ((TextView) findViewById(R.id.hydroMulch_fiber)).setText(String.valueOf(fiberMulchCalc) + " lbs");
        ((TextView) findViewById(R.id.hydroMulch_tackifier)).setText(String.valueOf(tackCalc) + " lbs");
        String hydroMulchAmount_bags = String.format("%d tanks of size %d for %d bag(s)", (int) tanksNeeded, (int) tankSize, (int) bagsOfHydroMulch);
        ((TextView) findViewById(R.id.hydroMulch_tankNeeded)).setText(hydroMulchAmount_bags);

        String applicationRate0 = String.format("%.2f CY / acre", applicationRates[0]);
        String applicationRate1 = String.format("%.2f lbs / acre", applicationRates[1]);
        //String applicationRate2 = "" + applicationRates[2];
        String applicationRate3 = String.format("%.2f lbs / acre", applicationRates[3]);
        String applicationRate4 = String.format("%.2f lbs / acre", applicationRates[4]);
        String applicationRate5 = String.format("%.2f lbs / acre", applicationRates[5]);
        String applicationRate6 = String.format("%.2f lbs / acre", applicationRates[6]);

        ((TextView) findViewById(R.id.textRate0)).setText(applicationRate0);
        ((TextView) findViewById(R.id.textRate1)).setText(applicationRate1);
        //((TextView)findViewById(R.id.textRate2)).setText(applicationRate2);
        ((TextView) findViewById(R.id.textRate3)).setText(applicationRate3);
        ((TextView) findViewById(R.id.textRate4)).setText(applicationRate4);
        ((TextView) findViewById(R.id.textRate5)).setText(applicationRate5);
        ((TextView) findViewById(R.id.textRate6)).setText(applicationRate6);
        try {
            save_historyData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
//ASK USER IF THE CALC SHOULD BE SAVED AS .txt or .csv or both?
    public void save_calc(View v) {
        String numberRef = numbEditText.getText().toString();
        String projName = nameEditText.getText().toString();

        //Check if Number is ### - #######, Check if Nick Name is Alphabetic or empty

        boolean isNumberRefValid = false;
        if (numberRef.length() == 9) {
            char[] charArr = numberRef.toCharArray();
            while (!isNumberRefValid) {
                if (!(charArr[0] >= '0' && charArr[0] <= '9')) break;
                if (!(charArr[1] >= 'A' && charArr[1] <= 'Z')) break;
                if ((charArr[2] != '-')) break;
                if (!(charArr[3] >= '0' && charArr[3] <= '9')) break;
                if (!(charArr[4] >= '0' && charArr[4] <= '9')) break;
                if (!(charArr[5] >= '0' && charArr[5] <= '9')) break;
                if (!(charArr[6] >= '0' && charArr[6] <= '9')) break;
                if (!(charArr[7] >= '0' && charArr[7] <= '9')) break;
                if (!(charArr[8] >= '0' && charArr[8] <= '9')) break;
                isNumberRefValid = true;
            }
        }
        if (isNumberRefValid && (projName.matches("^[a-zA-Z]*$"))) {
            FileOutputStream writeScanner = null;
            try {
                if (projName.equals("")) {
                    projectName = String.format("%s.txt", numberRef);
                } else {
                    projectName = String.format("%s_%s.txt", numberRef, projName);
                }
                String saveText = String.format("Project Number: %s\nProject Name: %s\n%s", numberRef, projName, taggedFileFormat());//fileOutputFormat());
                writeScanner = openFileOutput(projectName, MODE_PRIVATE);
                writeScanner.write(saveText.getBytes());
                Toast.makeText(this, "Saved to " + getFilesDir() + "/" + projectName, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) { // open file exception
                e.printStackTrace();
            } catch (IOException e) { // for write file exception
                e.printStackTrace();
            } finally {
                if (writeScanner != null) {
                    try {
                        writeScanner.close();
                        finish();// calculate page finish
                    } catch (IOException e) { // Trouble closing
                        e.printStackTrace();
                    }
                }
            }

            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);

        } else { // Error with User's File name Number-ProjectName
            Toast.makeText(this, "Unaccepted Filename:" + numberRef + "_" + projName + "\n File Format: #A-######\nOnly letters allowed for project name!", Toast.LENGTH_SHORT).show();
        }
    }
// Saves data in tagged file, to make file manager viewer easier
    public void save_historyData() throws FileNotFoundException {
        String saveHistory = taggedFileFormat();//fileOutputFormat();

        FileOutputStream historyScanner = null;
        try {
            historyScanner = openFileOutput(historyData, MODE_APPEND);
            historyScanner.write(saveHistory.getBytes()); //append to file if it exists
            // Debug Toast Notification to see history file path
            //           Toast.makeText(this,"Saved to " + getFilesDir() + "/" + historyData, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) { //can't open/create file
            e.printStackTrace();
        } catch (IOException e) { // error trying to write to file
            e.printStackTrace();
        }
    }
//CREATE DIALOG OPTION TO EXPORT .txt , .csv or both as email or both
    public void export(View view) {

        StringBuilder data = new StringBuilder();
        data.append("Acres: " + String.valueOf(Global.userInputAcres) + ", Tank Size: " + tankSize + " Gallons");
        data.append("\nMaterials,Input,Output");
        data.append("\nCompost," + String.valueOf(applicationRates[0]) + "," + ((TextView) findViewById(R.id.compost)).getText().toString());
        data.append("\nHydroseed,Refer to Figure 1," + ((TextView) findViewById(R.id.hydroSeed)).getText().toString());
        data.append("\nHydromulch,Refer to Figure 2," + ((TextView) findViewById(R.id.hydroMulch)).getText().toString());
        data.append("\n");
        data.append("\nHydroseed,Figure 1");
        data.append("\nMaterials,Amount,Rate(lbs/acre)");
        data.append("\nSeed," + ((TextView) findViewById(R.id.hydroSeed_seed)).getText().toString() + " ," + String.valueOf(applicationRates[1]));
        data.append("\nFiber," + ((TextView) findViewById(R.id.hydroSeed_fiber)).getText().toString() + " ," + String.valueOf(applicationRates[2]));
        data.append("\nFertilizer," + ((TextView) findViewById(R.id.hydroSeed_fertilizer)).getText().toString() + " ," + String.valueOf(applicationRates[3]));
        data.append("\nAdditive," + ((TextView) findViewById(R.id.hydroSeed_additive)).getText().toString() + " ," + String.valueOf(applicationRates[4]));
        data.append("\n");
        data.append("\nHydromulch,Figure 2");
        data.append("\nMaterials,Amount,Rate(lbs/acre)");
        data.append("\nFiber," + ((TextView) findViewById(R.id.hydroMulch_fiber)).getText().toString() + ", " + String.valueOf(applicationRates[5]));
        data.append("\nTackifier," + ((TextView) findViewById(R.id.hydroMulch_tackifier)).getText().toString() + "," + String.valueOf(applicationRates[6]));
        data.append("\n");
        data.append("\n" + ((TextView) findViewById(R.id.hydroMulch_tankNeeded)).getText().toString());


        try {
            //save the file


            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //export
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.Hydroseed.FileProvider", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "" + numbEditText.getText().toString() + " " + nameEditText.getText().toString());
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "send mail"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String taggedFileFormat() {
        StringBuilder fileFormat = new StringBuilder();
        String t1 = taggedTableBuilder(1);
        String t2 = taggedTableBuilder(2);
        String t3 = taggedTableBuilder(3);
        return fileFormat.append(t1).append(t2).append(t3).toString();
    }

    public String taggedTableBuilder(int tableType) {
        String table = "";
        switch (tableType) {
            case (1):
                return String.format("<T \n" +
                                "<H %s H>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                " T>\n",
                        ((TextView) findViewById(R.id.acreage)).getText().toString(),
                        "Materials", "INPUT", "OUTPUT",
                        "Compost", ((TextView) findViewById(R.id.textRate0)).getText().toString(), ((TextView) findViewById(R.id.compost)).getText().toString(),
                        "Hydroseed", "Refer to Figure 1", ((TextView) findViewById(R.id.hydroSeed)).getText().toString(),
                        "Hydromulch", "Refer to Figure 2", ((TextView) findViewById(R.id.hydroMulch)).getText().toString(),
                        " ", " ", " "
                );
            case (2):
                return String.format("<T \n" +
                                "<H %s H>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                " T>\n",
                        "FIG 1 - HYDROSEED",
                        "Materials", "Amount", "Rate",
                        "SEED", ((TextView) findViewById(R.id.hydroSeed_seed)).getText().toString(), ((TextView) findViewById(R.id.textRate1)).getText().toString(),
                        "FIBER", ((TextView) findViewById(R.id.hydroSeed_fiber)).getText().toString(), ((TextView) findViewById(R.id.textRate2)).getText().toString(),
                        "FERTILIZER", ((TextView) findViewById(R.id.hydroSeed_fertilizer)).getText().toString(), ((TextView) findViewById(R.id.textRate3)).getText().toString(),
                        "ADDITIVE", ((TextView) findViewById(R.id.hydroSeed_additive)).getText().toString(), ((TextView) findViewById(R.id.textRate4)).getText().toString()
                );
            case (3):
                return String.format("<T \n" +
                                "<H %s H>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                "<R %s:%s:%s R>\n" +
                                " T>\n",
                        "FIG 2 - HYDROMULCH",
                        "Materials", "Amount", "Rate",
                        "FIBER", ((TextView) findViewById(R.id.hydroMulch_fiber)).getText().toString(), ((TextView) findViewById(R.id.textRate5)).getText().toString(),
                        "TACKIFIER", ((TextView) findViewById(R.id.hydroMulch_tackifier)).getText().toString(), ((TextView) findViewById(R.id.textRate6)).getText().toString(),
                        " ", ((TextView) findViewById(R.id.hydroMulch_tankNeeded)).getText().toString(), " ",
                        " ", " ", " "
                );
        }
        return table;
    }

    //Creates a formatted output file using the calculation details, arranges a table like format
    //Use to export data, unlike tagged this is a table formatted output
    public String fileOutputFormat() {
        //Longest string on average used to define whitespace padding
        String tankNeeded = ((TextView) findViewById(R.id.hydroMulch_tankNeeded)).getText().toString();
        int whiteSpace = -tankNeeded.length();

        String calculationAreaInput = buildString(whiteSpace, "",
                ((TextView) findViewById(R.id.acreage)).getText().toString(), "");
        String calculationTableHeader = buildString(whiteSpace, "Materials", "Input", "Output");
        String calculationCompost = buildString(whiteSpace, "Compost", ((TextView) findViewById(R.id.textRate0)).getText().toString(),
                ((TextView) findViewById(R.id.compost)).getText().toString());
        String calculationHydroSeed = buildString(whiteSpace, "HydroSeed", "Refer to Fig 1",
                ((TextView) findViewById(R.id.hydroSeed)).getText().toString());
        String calculationHydroMulch = buildString(whiteSpace, "HydroMulch", "Refer to Fig 2",
                ((TextView) findViewById(R.id.hydroMulch)).getText().toString());
        String calculationTable = String.format("%s%s%s%s\n", calculationTableHeader,
                calculationCompost, calculationHydroSeed, calculationHydroMulch);

        String hydroSeedTableName = buildString(whiteSpace, "", "Figure 1", "");
        String hydroSeedHeader = buildString(whiteSpace, "Materials", "Amount", "Rate");
        String seedAmount = buildString(whiteSpace, "Seed",
                ((TextView) findViewById(R.id.hydroSeed_seed)).getText().toString(),
                ((TextView) findViewById(R.id.textRate1)).getText().toString());
        String hydroSeedFiber = buildString(whiteSpace, "Fiber",
                ((TextView) findViewById(R.id.hydroSeed_fiber)).getText().toString(),
                ((TextView) findViewById(R.id.textRate2)).getText().toString());
        String hydroSeedFertilizer = buildString(whiteSpace, "Fertilizer",
                ((TextView) findViewById(R.id.hydroSeed_fertilizer)).getText().toString(),
                ((TextView) findViewById(R.id.textRate3)).getText().toString());
        String hydroSeedAdditive = buildString(whiteSpace, "Additive",
                ((TextView) findViewById(R.id.hydroSeed_additive)).getText().toString(),
                ((TextView) findViewById(R.id.textRate4)).getText().toString());
        String hydroSeedTable = String.format("%s%s%s%s%s%s\n", hydroSeedTableName, hydroSeedHeader,
                seedAmount, hydroSeedFiber, hydroSeedFertilizer, hydroSeedAdditive);

        String hydroMulchTableName = buildString(whiteSpace, "", "Figure 2", "");
        String hydroMulchHeader = buildString(whiteSpace, "Materials", "Amount", "Rate");
        String hydroMulchFiber = buildString(whiteSpace, "Fiber",
                ((TextView) findViewById(R.id.hydroMulch_fiber)).getText().toString(),
                ((TextView) findViewById(R.id.textRate5)).getText().toString());
        String hydroMulchTackifier = buildString(whiteSpace, "Tackifier",
                ((TextView) findViewById(R.id.hydroMulch_tackifier)).getText().toString(),
                ((TextView) findViewById(R.id.textRate6)).getText().toString());
        String hydroMulchTable = String.format("%s%s%s%s\n", hydroMulchTableName, hydroMulchHeader,
                hydroMulchFiber, hydroMulchTackifier);
        String hydroMulchTanks = buildString(whiteSpace, " ",
                tankNeeded, " ");
        return String.format("%s%s\n%s\n%s%s\n\n\n\n", calculationAreaInput, calculationTable, hydroSeedTable, hydroMulchTable, hydroMulchTanks);
    }

    //Builds string based on padding whitespace and 3 strings to be separated by pipes
    public String buildString(int whiteSpace, String column1, String column2, String column3) {
        String s = String.format("|%" + whiteSpace + "s" + "|%" + whiteSpace + "s" + "|%" + whiteSpace + "s|\n", column1, column2, column3);
        String divider = buildDivider(s.length());
        return s + divider;
    }

    //Builds a divider using a char array and a for loop, ending has carriage return
    public String buildDivider(int length) {
        char[] b = new char[length];
        b[length - 1] = '\r';
        for (int i = 0; i < length - 1; i++) {
            b[i] = '-';
        }
        return new String(b);
    }

    public void shareData(View v) {

    }
}
