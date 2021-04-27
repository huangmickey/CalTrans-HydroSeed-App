package com.example.hydroseed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class FilesFragment extends Fragment {

    private ArrayList<FileManagerItem> FileManagerItemList;
    private RecyclerView rRecyclerView;
    private FileManagerItemAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private final String rootPath = "/data/data/com.example.hydroseed/files/";
    private File fileDirectory;
    private File[] storedFiles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        initFileDirectory(rootPath);
        createFileManagerItemList();
        buildRecyclerView(view);
        return view;
    }

    public void initFileDirectory(String rootPath) {
        fileDirectory = new File(rootPath);
        storedFiles = fileDirectory.listFiles();
    }


    public void createFileManagerItemList() {
        FileManagerItemList = new ArrayList<>();
        //call for loop method to scan through internal storage files saved
        populateFileManagerItemList();
    }

    public void populateFileManagerItemList() {
        for (int i = 0; i < storedFiles.length; i++) {
            //Toast.makeText(this.getContext(), "FILE: " + storedFiles[i].getAbsolutePath(), Toast.LENGTH_LONG).show();
            insertItem(storedFiles[i].getAbsolutePath());
        }
    }

    public void buildRecyclerView(View view) {
        rRecyclerView = view.findViewById(R.id.recyclerView);
        rRecyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this.getContext());
        rAdapter = new FileManagerItemAdapter(FileManagerItemList);

        rRecyclerView.setLayoutManager(rLayoutManager);
        rRecyclerView.setAdapter(rAdapter);

        rAdapter.setOnItemClickListener(new FileManagerItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Card has been clicked, show its contents
                showFileContent(position);
            }

            @Override
            public void onDeleteBinClick(int position) {
                //Card's delete bin is clicked, Ask user to confirm delete to delete file
                String fileName = FileManagerItemList.get(position).getFileName();
                openDialog(position, fileName);
            }
        });
    }

    public void insertItem(String absolutePath) {
        String fileNameDelimiter = "/files/";
        String fileExtensionDelimiter = ".";
        String fileName = absolutePath.substring(absolutePath.indexOf(fileNameDelimiter) + fileNameDelimiter.length());
        String fileExtension = absolutePath.substring(absolutePath.lastIndexOf(fileExtensionDelimiter) + fileExtensionDelimiter.length());
        if (fileExtension.equals("csv")) {
            FileManagerItemList.add(new FileManagerItem(R.drawable.ic_csv_extension, fileName, absolutePath));
        } else if (fileExtension.equals("txt")) {
            FileManagerItemList.add(new FileManagerItem(R.drawable.ic_text_extension, fileName, absolutePath));
        }
        //Toast.makeText(this.getContext(), "FILE NAME: " + fileName, Toast.LENGTH_LONG).show();
        //Toast.makeText(this.getContext(), "FILE EXTEN: " + fileExtension, Toast.LENGTH_LONG).show();
        //Toast.makeText(this.getContext(), "FILE Path: " + absolutePath, Toast.LENGTH_LONG).show();
    }

    public void deleteListItem(int listPosition) {
        //Get File's path to delete
        File deleteFile = new File(FileManagerItemList.get(listPosition).getFilePath());
        String fileName = FileManagerItemList.get(listPosition).getFileName();
        if (deleteFile.delete()) {
            Toast.makeText(this.getContext(), "Deleted File " + fileName + " Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getContext(), "Unable to Delete File: " + fileName, Toast.LENGTH_SHORT).show();
        }
        FileManagerItemList.remove(listPosition);
        rAdapter.notifyItemRemoved(listPosition);

    }

    public void openDialog(int position, String fileName) {
        //Toast.makeText(getContext(), "Dialog Open", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = "NOTICE";
        String message = "You're about to permanently delete:\n" + fileName + "\nAre you sure you want to continue?";
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //deleteItem Logic
                        //Toast.makeText(getContext(), "Delete File", Toast.LENGTH_LONG).show();
                        deleteListItem(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete Cancelled, close dialog
                        //Toast.makeText(getContext(), "Cancel Delete File", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    public void showFileContent(int position) {
        //make sure constructor calls new intent to show content of file[FileManagerItem]
        String fileName = FileManagerItemList.get(position).getFileName();
        Intent showContent = new Intent(getActivity(), FileContentPage.class);
        Bundle b = new Bundle();
        b.putString("fileName", fileName);
        showContent.putExtras(b);
        startActivity(showContent);

    }
}
