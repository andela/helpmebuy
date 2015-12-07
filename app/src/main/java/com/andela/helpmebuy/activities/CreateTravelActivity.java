package com.andela.helpmebuy.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.andela.helpmebuy.fragments.TravelFragment;
import com.andela.helpmebuy.models.Address;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.utilities.Constants;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

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

    private List departureDetails;

    private List arrivalDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel);

        parentLayout = (LinearLayout)findViewById(R.id.departure_layout);

        travelDepartureFragment = new TravelDepartureFragment();
        travelArrivalFragment = new TravelArrivalFragment();

        departureDetails = new ArrayList<>();
        arrivalDetails = new ArrayList<>();

        displayDepartureDetails(parentLayout);
    }

    public void displayArrivalDetails(View view) {
        try {
            departureDetails = getDetailsFromFragment(travelDepartureFragment);
            if (isValidTravelDetails(departureDetails)) {
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
        arrivalDetails = getDetailsFromFragment(travelArrivalFragment);

        try {
            setDetails(departureDetails, "departure");
            setDetails(arrivalDetails, "arrival");

            if (isValidTravelDetails(arrivalDetails)) {

                if (arrivalDateTime.isBefore(departureDateTime)) {
                    travelArrivalFragment.setDateError();
                    travelArrivalFragment.setTimeError();

                    Snackbar.make(view.getRootView(), "Arrival date should not be before departure date",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                travel = new Travel();

                setTravelDetails();

                saveTravelDetails(travel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List getDetailsFromFragment(TravelFragment fragment) {
        List list = new ArrayList<>();
        list.add(0, fragment);
        list.add(1,fragment.getLocation());
        list.add(2,fragment.getDateTime());

        return list;
    }

    public void setDetails(List details, String tag) {
        if (tag.equals("departure")) {
            departureLocation = (Location) details.get(1);
            departureDateTime = (DateTime) details.get(2);
            return;
        }

        arrivalLocation = (Location) details.get(1);
        arrivalDateTime = (DateTime) details.get(2);
    }

    public void setTravelDetails() {
        Address departureAddress = new Address();
        Address arrivalAddress = new Address();

        travel.setUserId("c655fd62-41e0-4ac1-8bbb-737c03666a42");
        travel.setId("123456");

        departureAddress.setLocation(departureLocation);
        arrivalAddress.setLocation(arrivalLocation);

        travel.setDepartureDate(departureDateTime);
        travel.setDepartureAddress(departureAddress);
        travel.setArrivalDate(arrivalDateTime);
        travel.setArrivalAddress(arrivalAddress);
    }

    public void saveTravelDetails(Travel travel) {
        FirebaseCollection<Travel> firebaseCollection = new FirebaseCollection<Travel>(Constants.TRAVELS, Travel.class);
        firebaseCollection.save(travel, new DataCallback<Travel>() {
            @Override
            public void onSuccess(Travel data) {
                new AlertDialog.Builder(CreateTravelActivity.this).setTitle("Travel Details")
                        .setMessage("Travel Details Successfully saved")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    public boolean isValidTravelDetails(List details) {
        TravelFragment fragment = (TravelFragment) details.get(0);
        Location location = (Location) details.get(1);
        DateTime dateTime = (DateTime) details.get(2);

        if (location == null) {
            fragment.setLocationError();
            return false;
        }

        if (dateTime == null || dateTime.isBeforeNow()) {
            fragment.setDateError();
            fragment.setTimeError();
            return false;
        }

        return true;
    }
}
