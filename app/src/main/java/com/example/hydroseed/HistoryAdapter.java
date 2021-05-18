package com.example.hydroseed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<CalculationObject> mHistoryList;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView mAcreage;
        public TextView mCompost;
        public TextView mSeedLayer;
        public TextView mMulchLayer;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mAcreage = itemView.findViewById(R.id.hist1);
            mCompost = itemView.findViewById(R.id.hist2);
            mSeedLayer = itemView.findViewById(R.id.hist3);
            mMulchLayer = itemView.findViewById(R.id.hist4);
        }
    }

    public HistoryAdapter(ArrayList<CalculationObject> historyList) {
        mHistoryList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);
        HistoryViewHolder co = new HistoryViewHolder(v);
        return co;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CalculationObject currentItem = mHistoryList.get(position);

        holder.mAcreage.setText(String.format("For %d acres of land:", (int) currentItem.getAcres()));
        holder.mCompost.setText(String.format("%d cubic yards of Compost", currentItem.getCompostLayer()));
        holder.mSeedLayer.setText(String.format("%d lbs of Hydroseed", currentItem.getHydroSeedLayer()));
        holder.mMulchLayer.setText(String.format("%d lbs of Hydromulch", currentItem.getHydroMulchLayer()));
    }


    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }
}