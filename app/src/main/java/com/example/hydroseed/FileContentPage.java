package com.example.hydroseed;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileContentPage extends AppCompatActivity {
    private TextView contentEditText;
    private Button shareFile;
    private ArrayList<FileContentItem> FileContentItemList;
    private RecyclerView rRecyclerView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_contents);
        Bundle b = getIntent().getExtras();
        initVars();
        createFileManagerItemList();
        buildRecyclerView();
        fileName = b.getString("fileName");
        showFileContent(fileName);
        //Implement ON CLICK LISTENER to share file
    }

    public void initVars() {
        //
        //shareFile.setOnClickListener((View.OnClickListener) this);
    }

    public void createFileManagerItemList() {
        FileContentItemList = new ArrayList<>();
        //call for loop method to scan through internal storage files saved
        //insertFileContentItemList();
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

    public void showFileContent(String fileName) {
        //attach file contents to layout text view, file needs to be in tagged form otherwise it will not read the file
        //Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
        parseFile(fileName);
    }

    public void parseFile(String fileName) {
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
                    rowByCol[rowCounter][1] = rowData.substring(rowData.indexOf(",")+1, rowData.lastIndexOf(","));
                    rowByCol[rowCounter][2] = rowData.substring(rowData.lastIndexOf(",")+1);
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



}
