package com.example.hydroseed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//Custom Adapter Class to hold cardView
public class FileManagerItemAdapter extends RecyclerView.Adapter<FileManagerItemAdapter.FileManagerItemViewHolder> {
    private ArrayList<FileManagerItem> FileManagerItemList;
    private OnItemClickListener Listener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteBinClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.Listener = listener;
    }

    public static class FileManagerItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView FileIcon;
        public TextView FileName;
        public TextView FilePath;
        public ImageView DeleteBin;

        public FileManagerItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            FileIcon = itemView.findViewById(R.id.fileIcon);
            FileName = itemView.findViewById(R.id.fileName);
            FilePath = itemView.findViewById(R.id.filePath);
            DeleteBin = itemView.findViewById(R.id.image_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            DeleteBin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteBinClick(position);
                        }
                    }
                }
            });
        }
    }

    public FileManagerItemAdapter(ArrayList<FileManagerItem> FileManagerItemArrayList) {
        this.FileManagerItemList = FileManagerItemArrayList;
    }

    @NonNull
    @Override
    public FileManagerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_filemanager_items, parent, false);
        FileManagerItemViewHolder FMIVH = new FileManagerItemViewHolder(v, this.Listener);
        return FMIVH;
    }

    @Override
    public void onBindViewHolder(@NonNull FileManagerItemViewHolder holder, int position) {
        FileManagerItem currentFile = this.FileManagerItemList.get(position);

        holder.FileIcon.setImageResource(currentFile.getFileIcon());
        holder.FileName.setText(currentFile.getFileName());
        holder.FilePath.setText(currentFile.getFilePath());
    }

    @Override
    public int getItemCount() {
        return this.FileManagerItemList.size();
    }
}
