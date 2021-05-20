package com.example.hydroseed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.hydroseed.Global.historyList;

public class History extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ArrayList<CalculationObject> histList = new ArrayList<>();
        for (CalculationObject calculation : Global.historyList) {
            double acres = calculation.getAcres();
            int compost = calculation.getCompostLayer();
            int seed = calculation.getHydroSeedLayer();
            int mulch = calculation.getHydroMulchLayer();
            histList.add(new CalculationObject(acres,compost,seed,mulch));
        }

        mRecyclerView = findViewById(R.id.historyRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HistoryAdapter(histList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(History.this, HistoryCalculate.class);
                intent.putExtra("hist", histList.get(position));
                startActivity(intent);
            }
        });
    }




    public void onClick(View v) {
        Intent intent = new Intent(this,CalculatePage.class);
        startActivity(intent);
    }
}
