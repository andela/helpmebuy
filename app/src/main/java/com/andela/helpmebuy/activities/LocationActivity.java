package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.LocationAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    private RecyclerView locationRecyclerView;
    private RecyclerView.Adapter locationAdapter;
    private RecyclerView.LayoutManager locationLayoutManager;
    private List<String> locations =  new ArrayList<>(Arrays.asList("Togo","Nigeria","South Africa","New York","Kenya"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

//        locationRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
//        locationRecyclerView.setHasFixedSize(true);
//
//        locationLayoutManager = new LinearLayoutManager(this);
//        locationRecyclerView.setLayoutManager(locationLayoutManager);
//
//        locationAdapter = new LocationAdapter(this,locations);
    }

}
