package com.example.hydroseed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.hydroseed.Global.ACRE_TO_SQFT;
import static com.example.hydroseed.Global.applicationRates;
import static com.example.hydroseed.Global.tankSize;

public class CalculatePage extends AppCompatActivity {
    private static String projectName = "";
    private static final String historyData = "historyData.txt";
    EditText nameEditText, numbEditText;

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
        ((TextView) findViewById(R.id.hydroSeed_fiber)).setText(String.valueOf((int)fiberSeedCalc) + " lbs");
        String hydroSeedAmount_fiber = String.format("%.2f lbs", applicationRates[2]);
        ((TextView) findViewById(R.id.textRate2)).setText(hydroSeedAmount_fiber);
        String hydroSeed_fertilizerBags = String.format("%d bag(s)", (int) bagsOfFertilizer);
        ((TextView) findViewById(R.id.hydroSeed_fertilizer)).setText(hydroSeed_fertilizerBags);
        ((TextView) findViewById(R.id.hydroSeed_additive)).setText(calculation.toStringHydroSeedAmount_additive());

        ((TextView) findViewById(R.id.hydroMulch)).setText(calculation.toStringHydroMulchAmount());
        ((TextView) findViewById(R.id.hydroMulch_fiber)).setText(String.valueOf((int)fiberMulchCalc) + " lbs");
        ((TextView) findViewById(R.id.hydroMulch_tackifier)).setText(String.valueOf((int)tackCalc) + " lbs");
        String hydroMulchAmount_bags = String.format("%d tanks of size %d for %d bag(s)", (int) tanksNeeded, (int) tankSize, (int) bagsOfHydroMulch);
        ((TextView) findViewById(R.id.hydroMulch_tankNeeded)).setText(hydroMulchAmount_bags);

        String applicationRate0 = String.format("%.2f CY / acre", applicationRates[0]);
        String applicationRate1 = String.format("%.2f lbs", applicationRates[1]);
        //String applicationRate2 = "" + applicationRates[2];
        String applicationRate3 = String.format("%.2f lbs", applicationRates[3]);
        String applicationRate4 = String.format("%.2f lbs", applicationRates[4]);
        String applicationRate5 = String.format("%.2f lbs", applicationRates[5]);
        String applicationRate6 = String.format("%.2f lbs", applicationRates[6]);

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

    public void save_historyData() throws FileNotFoundException {
        String saveHistory = taggedFileFormat(false);
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

    public void openDialog(boolean sendAction) {
        //Toast.makeText(getContext(), "Dialog Open", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = "Please Select Extension to Complete Action";
        String[] options = new String[]{"Plain Text (.txt)", "Spreadsheet (.csv)"};
        ArrayList<String> items = new ArrayList<>();
        boolean[] selectedItems = new boolean[options.length];
        builder.setTitle(title)
                .setMultiChoiceItems(options, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (items.contains(options[which])) {
                                items.remove(options[which]);
                            } else {
                                items.add(options[which]);
                            }
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Call Export or Save
                        if (sendAction && items.size() > 0) {
                            //Toast.makeText(CalculatePage.this, "EXPORT", Toast.LENGTH_LONG).show();
                            if (verifyFileName()) {
                                String fileNameCSV = "";
                                String fileNameTXT = "";
                                String numberRef = numbEditText.getText().toString();
                                String projName = nameEditText.getText().toString();
                                if (projName.equals("")) {
                                    fileNameCSV = String.format("%s.%s", numberRef, "csv");
                                    fileNameTXT = String.format("%s.%s", numberRef, "txt");
                                } else {
                                    fileNameCSV = String.format("%s_%s.%s", numberRef, projName, "csv");
                                    fileNameTXT = String.format("%s_%s.%s", numberRef, projName, "txt");
                                }
                                String contentCSV = "";
                                String contentTXT = "";
                                switch (items.size()) {
                                    case 1:
                                        String extension = items.get(0).substring(items.get(0).indexOf("(") + 1, items.get(0).indexOf(")"));
                                        //Toast.makeText(CalculatePage.this, "Chosen Extension " + extension, Toast.LENGTH_SHORT).show();
                                        if (extension.equals(".csv")) {
                                            contentCSV = taggedFileFormat(true); // removes all tags
                                            writeToFile(fileNameCSV, contentCSV, true); // write file
                                            shareFile(fileNameCSV); // share file
                                        } else {
                                            contentTXT = fileOutputFormat();
                                            writeToFile(fileNameTXT, contentTXT, true);
                                            shareFile(fileNameTXT);

                                        }
                                        break;
                                    case 2:
                                        contentCSV = taggedFileFormat(true); // removes all tags
                                        writeToFile(fileNameCSV, contentCSV,true); // write file
                                        contentTXT = fileOutputFormat();
                                        writeToFile(fileNameTXT, contentTXT, true);
                                        shareFile(fileNameCSV, fileNameTXT);
                                        break;
                                }
                            }
                            //Write files (fileOutput for txt) (tagged file without Headers)
                        } else {
                            //Toast.makeText(CalculatePage.this, "SAVE", Toast.LENGTH_SHORT).show();
                            if (verifyFileName()) {
                                String fileNameCSV = "";
                                String fileNameTXT = "";
                                String numberRef = numbEditText.getText().toString();
                                String projName = nameEditText.getText().toString();
                                if (projName.equals("")) {
                                    fileNameCSV = String.format("%s.%s", numberRef, "csv");
                                    fileNameTXT = String.format("%s.%s", numberRef, "txt");
                                } else {
                                    fileNameCSV = String.format("%s_%s.%s", numberRef, projName, "csv");
                                    fileNameTXT = String.format("%s_%s.%s", numberRef, projName, "txt");
                                }
                                String contentCSV = "";
                                String contentTXT = "";
                                switch (items.size()) {
                                    case 1:
                                        String extension = items.get(0).substring(items.get(0).indexOf("(") + 1, items.get(0).indexOf(")"));
                                        //Toast.makeText(CalculatePage.this, "Chosen Extension " + extension, Toast.LENGTH_SHORT).show();
                                        if (extension.equals(".csv")) {
                                            contentCSV = taggedFileFormat(false); // removes all tags
                                            writeToFile(fileNameCSV, contentCSV, false); // write file
                                            returnHome();
                                        } else {
                                            contentTXT = taggedFileFormat(false);
                                            writeToFile(fileNameTXT, contentTXT, false);
                                            returnHome();
                                        }
                                        break;
                                    case 2:
                                        contentCSV = taggedFileFormat(false); // removes all tags
                                        writeToFile(fileNameCSV, contentCSV, false); // write file
                                        contentTXT = taggedFileFormat(false);
                                        writeToFile(fileNameTXT, contentTXT, false);
                                        returnHome();
                                        break;
                                }
                            }
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete Cancelled, close dialog
                        Toast.makeText(CalculatePage.this, "Cancel Save/Export", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    public boolean verifyFileName() {
        String numberRef = numbEditText.getText().toString();
        String projName = nameEditText.getText().toString();

        //Check if Number is ## - #######, Check if Nick Name is Alphabetic or empty

        boolean isNumberRefValid = false;
        StringBuilder errorMessageNumberRef = new StringBuilder();

        if (numberRef.length() == 9) {
            char[] charArr = numberRef.toCharArray();
            while (!isNumberRefValid) {
                if ((charArr[0] >= '2') || charArr[0] == '-') {
                    errorMessageNumberRef.append("First two entries must be digits and between 1-12");
                    break;
                }
                if (charArr[0] == '1' && charArr[1] >= '3' || charArr[1] == '-') {
                    errorMessageNumberRef.append("First two entries must be digits and between 1-12");
                    break;
                }
                if (charArr[0] == '0' && charArr[1] > '9') {
                    errorMessageNumberRef.append("First two entries must be digits and between 1-12");
                }
                if ((charArr[2] != '-')) {
                    errorMessageNumberRef.append("Must include a dash after the first two digits!");
                    break;
                }
                if (!validateCharacter(charArr[3])) break;
                if (!validateCharacter(charArr[4])) break;
                if (!validateCharacter(charArr[5])) break;
                if (!validateCharacter(charArr[6])) break;
                if (!validateCharacter(charArr[7])) break;
                if (!validateCharacter(charArr[8])) break;
                isNumberRefValid = true;
            }
        } else {
            errorMessageNumberRef.append("Must be exactly 9 characters");
        }

        if (isNumberRefValid && (projName.matches("^[a-zA-Z]*$"))) {
            return true;
        } else { // Error with User's File name Number-ProjectName
            Toast.makeText(this, "Unaccepted Filename: " + numberRef +
                    "\nFile Format: ##-######\n" + errorMessageNumberRef, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean validateCharacter(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z');
    }

    public void save_calc(View view) {
        openDialog(false);
    }

    public void export(View view) {
        openDialog(true);
    }

    //Creates .txt table formatted to send / Share
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

    public String taggedFileFormat(boolean export) {
        StringBuilder fileFormat = new StringBuilder();
        String t1 = taggedTableBuilder(1);
        String t2 = taggedTableBuilder(2);
        String t3 = taggedTableBuilder(3);
        if (export) {
            return fileFormat.append(t1).append(t2).append(t3).toString()
                    .replace("<T \n", "").replace("T>", "")
                    .replace("<H", "").replace("H>", "")
                    .replace("<R", "").replace("R>", "");

        }
        return fileFormat.append(t1).append(t2).append(t3).toString();
    }

    public String taggedTableBuilder(int tableType) {
        String table = "";
        switch (tableType) {
            case (1):
                return String.format("<T \n" +
                                "<H %s H>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
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
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
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
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
                                "<R %s,%s,%s R>\n" +
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

    //Add Omit message when exporting
    public void writeToFile(String fileName, String content, boolean omitMessage) {
        FileOutputStream writeScanner = null;
        try {
            writeScanner = openFileOutput(fileName, MODE_PRIVATE);
            writeScanner.write(content.getBytes());
            if(!omitMessage) {
                Toast.makeText(this, "Saved to " + getFilesDir() + "/" + fileName, Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) { // open file exception
            e.printStackTrace();
        } catch (IOException e) { // for write file exception
            e.printStackTrace();
        } finally {
            if (writeScanner != null) {
                try {
                    writeScanner.close();
                } catch (IOException e) { // Trouble closing
                    e.printStackTrace();
                }
            }
        }

    }

    public void shareFile(String fileName) {
        Context context = getApplicationContext();
        File fileLocation = new File(getFilesDir(), fileName);
        Uri path = FileProvider.getUriForFile(context, "com.example.Hydroseed.FileProvider", fileLocation);
        Intent fileIntent = new Intent(Intent.ACTION_SEND);
        fileIntent.setType("txt/csv");
        fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Calculation File: " + fileName);
        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileIntent.putExtra(Intent.EXTRA_STREAM, path);
        if(fileName.contains(".txt")){
            startActivityForResult(Intent.createChooser(fileIntent, "send mail"), 1);
        }
        else if(fileName.contains(".csv")){
            startActivityForResult(Intent.createChooser(fileIntent, "send mail"), 2);
        }
    }

    public void shareFile(String fileNameCSV, String fileNameTXT) {
        Context context = getApplicationContext();
        File fileCSVLocation = new File(getFilesDir(), fileNameCSV);
        File fileTXTLocation = new File(getFilesDir(), fileNameTXT);
        Uri path1 = FileProvider.getUriForFile(context, "com.example.Hydroseed.FileProvider", fileCSVLocation);
        Uri path2 = FileProvider.getUriForFile(context, "com.example.Hydroseed.FileProvider", fileTXTLocation);
        Intent fileIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        fileIntent.setType("txt/csv");
        ArrayList<Uri> files = new ArrayList<Uri>();
        files.add(path1);
        files.add(path2);
        fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Calculation Files: " + fileNameCSV + " " + fileNameTXT);
        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivityForResult(Intent.createChooser(fileIntent, "send mail"), 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            StringBuilder sb = new StringBuilder();
            sb.append(numbEditText.getText().toString());
            if(!nameEditText.getText().toString().equals("")){
                sb.append("_"+nameEditText.getText().toString());
            }
            sb.append(".txt");
            String fileName = sb.toString();
            switch (requestCode){
                case 1:
                    //TEXT File Format
                    reformat(fileName);
                    break;
                case 2:
                    //CSV File Format
                    fileName = fileName.replace(".txt", ".csv");
                    reformat(fileName);
                    break;
                case 3:
                    //Both Files required
                    reformat(fileName);
                    fileName = fileName.replace(".txt", ".csv");
                    reformat(fileName);
                    break;
            }
            returnHome();
        }
    }

    public void reformat(String fileName){
        String content = taggedFileFormat(false);
        writeToFile(fileName, content, false);
    }

    public void returnHome(){
        finish();// calculate page finish
        Intent returnIntent = new Intent(CalculatePage.this, MainActivity.class);
        startActivity(returnIntent);
    }
}
