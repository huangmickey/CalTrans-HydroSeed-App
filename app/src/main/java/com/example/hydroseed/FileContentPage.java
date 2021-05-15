package com.example.hydroseed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileContentPage extends AppCompatActivity implements View.OnClickListener {
    private Button button_share;
    private ArrayList<FileContentItem> FileContentItemList;
    private RecyclerView rRecyclerView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private String fileName;
    private String taggedFileData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_contents);
        Bundle b = getIntent().getExtras();
        initVars();
        createFileManagerItemList();
        buildRecyclerView();
        initVars();
        fileName = b.getString("fileName");
        showFileContent();
    }

    public void initVars() {
        button_share = findViewById(R.id.button_share);
        button_share.setOnClickListener(this);
    }

    public void createFileManagerItemList() {
        FileContentItemList = new ArrayList<>();
    }

    public void insertFileContentItemList(
            String header,
            String r1_c1, String r1_c2, String r1_c3,
            String r2_c1, String r2_c2, String r2_c3,
            String r3_c1, String r3_c2, String r3_c3,
            String r4_c1, String r4_c2, String r4_c3,
            String r5_c1, String r5_c2, String r5_c3

    ) {
        FileContentItemList.add(new FileContentItem(header,
                r1_c1, r1_c2, r1_c3,
                r2_c1, r2_c2, r2_c3,
                r3_c1, r3_c2, r3_c3,
                r4_c1, r4_c2, r4_c3,
                r5_c1, r5_c2, r5_c3));
    }

    public void buildRecyclerView() {
        rRecyclerView = findViewById(R.id.recyclerView);
        rRecyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this);

        rAdapter = new FileContentItemAdapter(FileContentItemList);
        rRecyclerView.setLayoutManager(rLayoutManager);
        rRecyclerView.setAdapter(rAdapter);
    }

    public void showFileContent() {
        //attach file contents to layout text view, file needs to be in tagged form otherwise it will not read the file
        //Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
        //Read Lines of file using string builder Check if csv or textFile
        String textLine = "";
        String header = "";
        String[][] rowByCol = new String[5][3];//5 Rows, 3 Columns

        int rowCounter = 0;
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((textLine = br.readLine()) != null) {
                sb.append(textLine).append("\n");
                //Read Line Need to Strip tags and delimiter
                if (textLine.contains("<T ")) {
                    //rowCounter = 0;
                } else if (textLine.contains("<H ")) {
                    header = textLine.substring(textLine.indexOf("<H ") + 3, textLine.indexOf(" H>"));
                    //Toast.makeText(this, header, Toast.LENGTH_SHORT).show();
                } else if (textLine.contains("<R ")) {
                    String rowData = textLine.substring(textLine.indexOf("<R ") + 3, textLine.indexOf(" R>"));
                    //Toast.makeText(this, rowData, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, rowData.substring(0, rowData.indexOf(",")), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this,  rowData.substring(rowData.indexOf(",")+1, rowData.lastIndexOf(",")), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, rowData.substring(rowData.lastIndexOf(",")+1), Toast.LENGTH_SHORT).show();
                    rowByCol[rowCounter][0] = rowData.substring(0, rowData.indexOf(","));
                    rowByCol[rowCounter][1] = rowData.substring(rowData.indexOf(",") + 1, rowData.lastIndexOf(","));
                    rowByCol[rowCounter][2] = rowData.substring(rowData.lastIndexOf(",") + 1);
                    rowCounter++; // updated for next row
                } else if (textLine.contains(" T>")) {
                    //END OF TABLE add to LIST, reset counter;
                    insertFileContentItemList(header,
                            rowByCol[0][0], rowByCol[0][1], rowByCol[0][2],
                            rowByCol[1][0], rowByCol[1][1], rowByCol[1][2],
                            rowByCol[2][0], rowByCol[2][1], rowByCol[2][2],
                            rowByCol[3][0], rowByCol[3][1], rowByCol[3][2],
                            rowByCol[4][0], rowByCol[4][1], rowByCol[4][2]
                    );
                    rowCounter = 0;
                }

            }
            taggedFileData = sb.toString();
        } catch (FileNotFoundException e) { // file not found error
            e.printStackTrace();
        } catch (IOException e) { // reading error
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {//Unable to close file error
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_share:
                //OPEN DIALOG
                openDialog();
                break;
        }
    }

    public void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = "NOTICE";
        String dialogMessage = String.format("Share %s to google drive or send via email?", fileName);
        builder.setTitle(title)
                .setMessage(dialogMessage)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Share File -> CSV / TXT?
                        //FORMAT
                        formatFile(fileName.contains(".csv"), false);
                        //SEND
                        sendFile();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    public void formatFile(boolean csvFile, boolean taggedFormat) {
        StringBuilder sb = new StringBuilder();
        if (taggedFormat) {//INTERNAL SAVE
            Log.d("TAGGEDDATA", taggedFileData);
            sb.append(taggedFileData);
            writeToFile(sb.toString(), false);
        } else if (csvFile) { //CSV TABLE
            Log.d("Tagged\n", taggedFileData);
            String temp = String.valueOf(sb.append(taggedFileData))
                    .replace("<T \n", "").replace("T>", "")
                    .replace("<H", "").replace("H>", "")
                    .replace("<R", "").replace("R>", "");
            Log.d("NEXT", temp);
            writeToFile(temp, true);
        } else {//TXT TABLE
            for (int outer = 0; outer < FileContentItemList.size(); outer += 3) {
                //Longest String in tables
                int whiteSpace = -FileContentItemList.get(outer + 2).getR4_c2().length();
                for (int inner = outer; inner < outer + 3; inner++) {
                    sb.append(textTable(inner, whiteSpace));
                }
            }
            Log.d("TEXTTABLE", sb.toString());
            writeToFile(sb.toString(), true);
        }
    }

    public String textTable(int table, int whiteSpace) {
        //Longest string on average used to define whitespace padding
        String header = buildString(whiteSpace, "",
                FileContentItemList.get(table).getHeader(), "");
        String row1Data = buildString(whiteSpace, FileContentItemList.get(table).getR1_c1(),
                FileContentItemList.get(table).getR1_c2(), FileContentItemList.get(table).getR1_c3());
        String row2Data = buildString(whiteSpace, FileContentItemList.get(table).getR2_c1(),
                FileContentItemList.get(table).getR2_c2(), FileContentItemList.get(table).getR2_c3());
        String row3Data = buildString(whiteSpace, FileContentItemList.get(table).getR3_c1(),
                FileContentItemList.get(table).getR3_c2(), FileContentItemList.get(table).getR3_c3());
        String row4Data = buildString(whiteSpace, FileContentItemList.get(table).getR4_c1(),
                FileContentItemList.get(table).getR4_c2(), FileContentItemList.get(table).getR4_c3());
        String row5Data = buildString(whiteSpace, FileContentItemList.get(table).getR5_c1(),
                FileContentItemList.get(table).getR5_c2(), FileContentItemList.get(table).getR5_c3());
        if (table % 3 == 0) {
            return String.format("%s%s%s%s%s\n", header, row1Data, row2Data, row3Data, row4Data);
        } else {
            return String.format("%s%s%s%s%s%s\n", header, row1Data, row2Data, row3Data, row4Data, row5Data);
        }
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

    //Add Omit message when exporting
    public void writeToFile(String content, boolean omitMessage) {
        FileOutputStream writeScanner = null;
        try {
            writeScanner = openFileOutput(fileName, MODE_PRIVATE);
            writeScanner.write(content.getBytes());
            if (!omitMessage) {
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

    public void sendFile() {
        //Check Extension for activity req code
        Context context = getApplicationContext();
        File fileLocation = new File(getFilesDir(), fileName);
        Uri path = FileProvider.getUriForFile(context, "com.example.Hydroseed.FileProvider", fileLocation);
        Intent fileIntent = new Intent(Intent.ACTION_SEND);
        fileIntent.setType("txt/csv");
        fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Calculation File: " + fileName);
        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivityForResult(Intent.createChooser(fileIntent, "send mail"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 1) {
           formatFile(false, true);
        }
    }

    public void exitActivity() {
        finish();
        super.onBackPressed();
    }

}
