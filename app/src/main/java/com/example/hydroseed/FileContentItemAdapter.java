package com.example.hydroseed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FileContentItemAdapter extends RecyclerView.Adapter<FileContentItemAdapter.FileContentViewHolder> {
    private ArrayList<FileContentItem> FileContentItemArrayList;

    public static class FileContentViewHolder extends RecyclerView.ViewHolder {

        public TextView header,
                r1_c1, r1_c2, r1_c3,
                r2_c1, r2_c2, r2_c3,
                r3_c1, r3_c2, r3_c3,
                r4_c1, r4_c2, r4_c3,
                r5_c1, r5_c2, r5_c3;

        public FileContentViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tableTitle);
            r1_c1 = itemView.findViewById(R.id.row1_col1);
            r1_c2 = itemView.findViewById(R.id.row1_col2);
            r1_c3 = itemView.findViewById(R.id.row1_col3);

            r2_c1 = itemView.findViewById(R.id.row2_col1);
            r2_c2 = itemView.findViewById(R.id.row2_col2);
            r2_c3 = itemView.findViewById(R.id.row2_col3);

            r3_c1 = itemView.findViewById(R.id.row3_col1);
            r3_c2 = itemView.findViewById(R.id.row3_col2);
            r3_c3 = itemView.findViewById(R.id.row3_col3);

            r4_c1 = itemView.findViewById(R.id.row4_col1);
            r4_c2 = itemView.findViewById(R.id.row4_col2);
            r4_c3 = itemView.findViewById(R.id.row4_col3);

            r5_c1 = itemView.findViewById(R.id.row5_col1);
            r5_c2 = itemView.findViewById(R.id.row5_col2);
            r5_c3 = itemView.findViewById(R.id.row5_col3);
        }
    }

    public FileContentItemAdapter(ArrayList<FileContentItem> fileContentItemArrayList) {
        this.FileContentItemArrayList = fileContentItemArrayList;
    }

    @NonNull
    @Override
    public FileContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_file_contents, parent, false);
        FileContentViewHolder FCVH = new FileContentViewHolder(v);
        return FCVH;
    }

    @Override
    public void onBindViewHolder(@NonNull FileContentViewHolder holder, int position) {
        FileContentItem currentItem = FileContentItemArrayList.get(position);
        holder.header.setText(currentItem.getHeader());

        holder.r1_c1.setText(currentItem.getR1_c1());
        holder.r1_c2.setText(currentItem.getR1_c2());
        holder.r1_c3.setText(currentItem.getR1_c3());

        holder.r2_c1.setText(currentItem.getR2_c1());
        holder.r2_c2.setText(currentItem.getR2_c2());
        holder.r2_c3.setText(currentItem.getR2_c3());

        holder.r3_c1.setText(currentItem.getR3_c1());
        holder.r3_c2.setText(currentItem.getR3_c2());
        holder.r3_c3.setText(currentItem.getR3_c3());

        holder.r4_c1.setText(currentItem.getR4_c1());
        holder.r4_c2.setText(currentItem.getR4_c2());
        holder.r4_c3.setText(currentItem.getR4_c3());

        holder.r5_c1.setText(currentItem.getR5_c1());
        holder.r5_c2.setText(currentItem.getR5_c2());
        holder.r5_c3.setText(currentItem.getR5_c3());
    }

    @Override
    public int getItemCount() {
        return FileContentItemArrayList.size();
    }
}