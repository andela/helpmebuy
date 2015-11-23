package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.fragments.TravelDepartureFragment;

public class CreateTravelActivity extends AppCompatActivity {

    private TravelDepartureFragment travelDepartureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_travel);

        travelDepartureFragment = new TravelDepartureFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.create_travel_fragment_container, travelDepartureFragment)
                .addToBackStack(null)
                .commit();
    }

}
