package com.example.hydroseed;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class FilesFragment extends Fragment {

    ArrayList<FileManagerItem> FileManagerItemList;
    RecyclerView rRecyclerView;
    FileManagerItemAdapter rAdapter;
    RecyclerView.LayoutManager rLayoutManager;
    final String rootPath = "/data/data/com.example.hydroseed/files/";
    File fileDirectory;
    File[] storedFiles;

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
        popuplateFileManagerItemList();
    }

    public void popuplateFileManagerItemList() {
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
                //Initiate Calculate Page with data
                showFileContent(position);
            }

            @Override
            public void onDeleteBinClick(int position) {
                //Delete Item
                deleteListItem(position);
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
        if (deleteFile.delete()) {
            Toast.makeText(this.getContext(), "Deleted File" + FileManagerItemList.get(listPosition).getFileName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this.getContext(), "Unable to Delete File" + FileManagerItemList.get(listPosition).getFileName(), Toast.LENGTH_LONG).show();
        }
        FileManagerItemList.remove(listPosition);
        rAdapter.notifyItemRemoved(listPosition);
    }

    public void showFileContent(int position) {
        //make sure constructor calls new intent to show content of file[FileManagerItem]
        FileManagerItemList.get(position).showFileContent("Show Contents");
        rAdapter.notifyItemChanged(position);
    }
}
