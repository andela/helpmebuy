package com.andela.helpmebuy.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.fragments.OnTravelActivityListener;
import com.andela.helpmebuy.fragments.TravelArrivalFragment;
import com.andela.helpmebuy.fragments.TravelDepartureFragment;
import com.andela.helpmebuy.fragments.TravelFragment;
import com.andela.helpmebuy.models.Address;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.andela.helpmebuy.utilities.CurrentUser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CreateTravelActivity extends AppCompatActivity implements OnTravelActivityListener {

    private static final int TRANSIT_FORWARD = 0;

    private static final int TRANSIT_BACKWARD = 1;

    private static final String TAG = "Error";

    private TravelDepartureFragment travelDepartureFragment;

    private TravelArrivalFragment travelArrivalFragment;

    FrameLayout parentLayout;

    ArrayList<String> departureDetails;
    Location departureLocation;

    ArrayList<String> arrivalDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_travel);

        if (travelDepartureFragment == null) {

            initializeComponents();

            initializeTravelFragments();

            initializeTravelDetails();

            addTravelFragment(parentLayout.getId(), travelDepartureFragment);
        }
    }

    private void initializeComponents(){

        parentLayout = (FrameLayout) findViewById(R.id.create_travel_fragment_container);
    }

    private void initializeTravelFragments(){

        travelDepartureFragment = new TravelDepartureFragment();
        travelDepartureFragment.setArguments(new Bundle());
        travelDepartureFragment.setmActivityListener(this);

        travelArrivalFragment = new TravelArrivalFragment();
        travelArrivalFragment.setArguments(new Bundle());
        travelArrivalFragment.setmActivityListener(this);

    }

    private void initializeTravelDetails() {
        departureDetails = new ArrayList<>();
        arrivalDetails = new ArrayList<>();
    }

    public void addTravelFragment(int layout, Fragment fragment){

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(layout, fragment)
                    .commit();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void replaceTravelFragment(int layout, Fragment fragment, int TRANSITION){

        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction = animateFragment(fragmentTransaction, TRANSITION);
            fragmentTransaction.replace(layout, fragment).commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private FragmentTransaction animateFragment(FragmentTransaction fragmentTransaction, int TRANSITION){

        switch (TRANSITION) {
            case TRANSIT_FORWARD:
                fragmentTransaction.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right);
                break;
            case TRANSIT_BACKWARD:
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            default:
                break;
        }

        return fragmentTransaction;
    }

    @Override
      public void onNextButtonClicked(View view, ArrayList<String> departureDetails, Location travelLocation) {
        this.departureDetails = departureDetails;
        departureLocation = travelLocation;

        String message = "Departure date cannot be before current date";
        if (validateDateTime(view, this.departureDetails,message)) {

            if (arrivalDetails.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(TravelArrivalFragment.TRAVEL_ARRIVAL_KEY, arrivalDetails);
                travelArrivalFragment.getArguments().putAll(bundle);
            }
            replaceTravelFragment(parentLayout.getId(), travelArrivalFragment, TRANSIT_FORWARD);
        }
    }

    @Override
    public void onPreviousButtonClicked(ArrayList<String> arrivalVal, Location travelLocation) {
        arrivalDetails = arrivalVal;

        if (departureDetails.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(TravelDepartureFragment.TRAVEL_DEPARTURE_KEY, departureDetails);
            travelDepartureFragment.getArguments().putAll(bundle);
        }

        replaceTravelFragment(parentLayout.getId(), travelDepartureFragment, TRANSIT_BACKWARD);

    }

    @Override
    public void onSaveButtonClicked(View view, ArrayList<String> arrivalDetails, Location travelLocation) {
        ArrayList<Location>travelLocations = new ArrayList<>();
        ArrayList<DateTime>travelDates = new ArrayList<>();

        String message = "Arrival date cannot be before departure date";
        if(validateDateTime(view, departureDetails, arrivalDetails, message)){
            travelLocations.add(departureLocation);
            travelLocations.add(travelLocation);
            travelDates.add(getDateTimeValue(departureDetails.get(1),departureDetails.get(2)));
            travelDates.add(getDateTimeValue(arrivalDetails.get(1),arrivalDetails.get(2)));
            setTravelDetails(travelLocations,travelDates);
        }
    }

    private boolean validateDateTime(View view, ArrayList<String> details,String message) {
        boolean proceed = true;
        if(getDateTimeValue(details.get(1),details.get(2)).isBeforeNow()) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
            proceed = false;
        }
        return proceed;
    }

    private boolean validateDateTime(View view, ArrayList<String> departureDetails, ArrayList<String> arrivalDetails, String message) {
        boolean proceed = true;
        DateTime departureDateTime =  getDateTimeValue(departureDetails.get(1),departureDetails.get(2));
        DateTime arrivalDateTime = getDateTimeValue(arrivalDetails.get(1),departureDetails.get(2));
        if(arrivalDateTime.isBefore(departureDateTime)) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
            proceed = false;
        }
        return proceed;
    }

    private DateTime getDateTimeValue(String date, String time){
        String dateTimeValue = date + " " + time;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm a");
        return formatter.parseDateTime(dateTimeValue);
    }

    public void closeWindow(View view) {
        finish();
        System.exit(0);
    }


    public void setTravelDetails(ArrayList<Location> travelLocation, ArrayList<DateTime> travelDateTime) {
        Travel travel = new Travel();
        Address departureAddress = new Address();
        Address arrivalAddress = new Address();

        //travel.setUserId("c655fd62-41e0-4ac1-8bbb-737c03666a42");
        User currentUser = CurrentUser.get(this);
        travel.setUserId(currentUser.getId());
        travel.setId(travel.getId());
        //travel.setId("123456");

        departureAddress.setLocation(travelLocation.get(0));
        arrivalAddress.setLocation(travelLocation.get(1));

        travel.setDepartureDate(travelDateTime.get(0));
        travel.setDepartureAddress(departureAddress);
        travel.setArrivalDate(travelDateTime.get(1));
        travel.setArrivalAddress(arrivalAddress);
        saveTravelDetails(travel);
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
}
