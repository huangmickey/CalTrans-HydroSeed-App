package com.example.hydroseed;

public class FileManagerItem {
    private int fileIcon;
    private String fileName, filePath;

    //Constructor Class for cardView Layout
    public FileManagerItem(int fileIcon, String fileName, String filePath) {
        this.fileIcon = fileIcon;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void showFileContent(String text) {
        //This calls a new activity to display file's content
        //Needs extension, and maybe add share module Enrique had on Calculate page
        //Make sure new activity / layout is scrollable to accommodate file size
        fileName = text;//placeholder code
    }

    //Get Methods for cardView layout
    public int getFileIcon() {
        return this.fileIcon;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }
}
