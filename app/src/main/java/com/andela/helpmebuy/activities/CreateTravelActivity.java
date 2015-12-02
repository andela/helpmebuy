package com.andela.helpmebuy.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.fragments.TravelArrivalFragment;
import com.andela.helpmebuy.fragments.TravelDepartureFragment;
import com.andela.helpmebuy.models.Address;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.utilities.Constants;

import org.joda.time.DateTime;

public class CreateTravelActivity extends AppCompatActivity {

    public static final String TAG = "Save Status";

    private TravelDepartureFragment travelDepartureFragment;

    private TravelArrivalFragment travelArrivalFragment;

    private LinearLayout parentLayout;

    private Travel travel;

    private DateTime departureDateTime;

    private DateTime arrivalDateTime;

    private Location departureLocation;

    private Location arrivalLocation;

    private TextView departureTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel);

        departureTime = (TextView) findViewById(R.id.time_value);

        parentLayout = (LinearLayout)findViewById(R.id.departure_layout);

        travelDepartureFragment = new TravelDepartureFragment();
        travelArrivalFragment = new TravelArrivalFragment();

        displayDepartureDetails(parentLayout);
    }

    public void displayArrivalDetails(View view) {
        try {
            if (isValidDepartureTravelDetails()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_travel_fragment_container, travelArrivalFragment)
                        .addToBackStack(null).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void saveDetails(View view) {
        if (isValidArrivalTravelDetails()) {
            travel = new Travel();
            Address departureAddress = new Address();
            Address arrivalAddress = new Address();

            travel.setUserId("c655fd62-41e0-4ac1-8bbb-737c03666a42");
            travel.setId("123456");

            departureAddress.setLocation(departureLocation);
            travel.setDepartureDate(departureDateTime);
            travel.setDepartureAddress(departureAddress);

            arrivalAddress.setLocation(arrivalLocation);
            travel.setArrivalDate(arrivalDateTime);
            travel.setArrivalAddress(arrivalAddress);

            saveTravelDetails(travel);
        }
    }

    public void saveTravelDetails(Travel travel) {
        FirebaseCollection<Travel> firebaseCollection = new FirebaseCollection<Travel>(Constants.TRAVELS, Travel.class);
        firebaseCollection.save(travel, new DataCallback<Travel>() {
            @Override
            public void onSuccess(Travel data) {
                Log.d(TAG, "SUCCESS");
                new AlertDialog.Builder(CreateTravelActivity.this).setTitle("Travel Details")
                        .setMessage("Travel Details Successfully saved").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(TAG, errorMessage);
            }
        });
    }

    private boolean isValidDepartureTravelDetails() {
        departureLocation = travelDepartureFragment.getDepartureLocation();
        departureDateTime = travelDepartureFragment.getDepartureDateTime();

        if (departureLocation == null) {
            travelDepartureFragment.setLocationError();
            return false;
        }

        if (departureDateTime == null || departureDateTime.isBeforeNow()) {
            travelDepartureFragment.setDepartureDateError();
            travelDepartureFragment.setDepartureTimeError();
            return false;
        }

        return true;
    }

    public boolean isValidArrivalTravelDetails() {
        arrivalLocation = travelArrivalFragment.getArrivalLocation();
        arrivalDateTime = travelArrivalFragment.getArrivalDateTime();

        if (arrivalLocation == null) {
            travelArrivalFragment.setLocationError();
            return false;
        }

        if (arrivalDateTime == null || arrivalDateTime.isBeforeNow() || arrivalDateTime.isBefore(departureDateTime)) {
            travelArrivalFragment.setDepartureDateError();
            travelArrivalFragment.setDepartureTimeError();
            return false;
        }

        return true;
    }
}
