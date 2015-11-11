package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.LocationAdapter;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.locations.FirebaseCountries;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.views.LocationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    private RecyclerView locationRecyclerView;
    private RecyclerView.Adapter locationAdapter;
    private RecyclerView.LayoutManager locationLayoutManager;

    private LocationView<Country> countriesView;
    private EditText searchViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        countriesView = (LocationView<Country>) findViewById(R.id.location_view);

        FirebaseCountries countries = new FirebaseCountries();
        countries.getAll(new DataCallback<List<Country>>() {
            @Override
            public void onSuccess(List<Country> data) {
                countriesView.setLocations(data);
            }

            @Override
            public void onError(String errorMessage) {
            }
        });

        searchViewText = (EditText) findViewById(R.id.search_view);
        searchViewText.addTextChangedListener(countriesView.locationFilter());
    }

}
