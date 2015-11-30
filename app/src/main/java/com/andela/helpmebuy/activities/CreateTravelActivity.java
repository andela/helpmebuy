package com.andela.helpmebuy.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.fragments.TravelArrivalFragment;
import com.andela.helpmebuy.fragments.TravelDepartureFragment;
import com.andela.helpmebuy.models.Address;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;

import org.joda.time.DateTime;

public class CreateTravelActivity extends AppCompatActivity {

    public static final String TAG = "Save Status";

    private TravelDepartureFragment travelDepartureFragment;

    private TravelArrivalFragment travelArrivalFragment;

    private LinearLayout parentLayout;

    private Travel travel;

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

    public void saveDetails(View view) {
        travel =  new Travel();
        Address departureAddress =  new Address();
        Address arrivalAddress =  new Address();

        //User user  =  new User();
        travel.setUserId("c655fd62-41e0-4ac1-8bbb-737c03666a42");
        travel.setId("123456");

        Location departureLocation = travelDepartureFragment.getDepartureLocation();
        departureAddress.setLocation(departureLocation);
        DateTime departureDateTIme = travelDepartureFragment.getDepartureDateTime();
        travel.setDepartureDate(departureDateTIme);
        travel.setDepartureAddress(departureAddress);

        Location arrivalLocation = travelArrivalFragment.getArrivalLocation();
        arrivalAddress.setLocation(arrivalLocation);
        travel.setArrivalAddress(arrivalAddress);

        FirebaseCollection<Travel> firebaseCollection = new FirebaseCollection<Travel>(Constants.TRAVELS,Travel.class);
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
}
