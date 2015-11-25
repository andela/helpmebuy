package com.andela.helpmebuy.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dialogs.DatePickerFragment;
import com.andela.helpmebuy.dialogs.TimePickerFragment;
import com.andela.helpmebuy.fragments.TravelArrivalFragment;
import com.andela.helpmebuy.fragments.TravelDepartureFragment;

public class CreateTravelActivity extends AppCompatActivity {

    private TravelDepartureFragment travelDepartureFragment;

    private TravelArrivalFragment travelArrivalFragment;

    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel);

        parentLayout = (LinearLayout)findViewById(R.id.departure_layout);

        travelDepartureFragment = new TravelDepartureFragment();
        travelArrivalFragment = new TravelArrivalFragment();

        displayDepartureDetails(parentLayout);


    }

    public void displayArrivalDetails(View view) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.create_travel_fragment_container,travelArrivalFragment)
                .addToBackStack(null).commit();
    }

    public void displayDepartureDetails(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.create_travel_fragment_container, travelDepartureFragment)
                .addToBackStack(null)
                .commit();
    }

    public void closeWindow(View view) {
        finish();
        System.exit(0);
    }

}
