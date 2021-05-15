package com.example.hydroseed;

public class FileManagerItem {
    private int fileIcon;
    private String fileName, filePath, fileMod;

    //Constructor Class for cardView Layout
    public FileManagerItem(int fileIcon, String fileName, String filePath, String fileMod) {
        this.fileIcon = fileIcon;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileMod = fileMod;
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

    public String getFileMod() {
        return this.fileMod;
    }
}
