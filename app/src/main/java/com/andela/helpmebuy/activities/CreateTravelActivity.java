package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.fragments.OnTravelActivityListener;
import com.andela.helpmebuy.fragments.TravelArrivalFragment;
import com.andela.helpmebuy.fragments.TravelDepartureFragment;

import java.util.ArrayList;

public class CreateTravelActivity extends AppCompatActivity implements OnTravelActivityListener {

    private static final int TRANSIT_FORWARD = 0;

    private static final int TRANSIT_BACKWARD = 1;

    private TravelDepartureFragment travelDepartureFragment;

    private TravelArrivalFragment travelArrivalFragment;

    FrameLayout parentLayout;

    ArrayList<String> departureDetails;

    ArrayList<String> arrivalDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_travel);

        initializeComponents();

        initializeTravelFragments();

        initializeTravelDetails();

        addTravelFragment(parentLayout.getId(), travelDepartureFragment);

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

    public void replaceTravelFragment(int layout, Fragment fragment){

        try {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(layout, fragment)
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
      public void onNextButtonClicked(ArrayList<String> depatureVal) {

        departureDetails = depatureVal;

        if (arrivalDetails.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(TravelArrivalFragment.TRAVEL_ARRIVAL_KEY, arrivalDetails);
            travelArrivalFragment.getArguments().putAll(bundle);
        }

        replaceTravelFragment(parentLayout.getId(), travelArrivalFragment, TRANSIT_FORWARD);

    }

    @Override
    public void onPreviousButtonClicked(ArrayList<String> arrivalVal) {

        arrivalDetails = arrivalVal;

        if (departureDetails.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(TravelDepartureFragment.TRAVEL_DEPARTURE_KEY, departureDetails);
            travelDepartureFragment.getArguments().putAll(bundle);
        }

        replaceTravelFragment(parentLayout.getId(), travelDepartureFragment, TRANSIT_BACKWARD);

    }

    @Override
    public void onSaveButtonClicked(ArrayList<String> detartureDetails) {

    }

    public void closeWindow(View view) {
        finish();
        System.exit(0);
    }






//    public void setTravelDetails() {
//        Address departureAddress = new Address();
//        Address arrivalAddress = new Address();
//
//        travel.setUserId("c655fd62-41e0-4ac1-8bbb-737c03666a42");
//        travel.setId("123456");
//
//        departureAddress.setLocation(departureLocation);
//        arrivalAddress.setLocation(arrivalLocation);
//
//        travel.setDepartureDate(departureDateTime);
//        travel.setDepartureAddress(departureAddress);
//        travel.setArrivalDate(arrivalDateTime);
//        travel.setArrivalAddress(arrivalAddress);
//    }

//    public void saveTravelDetails(Travel travel) {
//        FirebaseCollection<Travel> firebaseCollection = new FirebaseCollection<Travel>(Constants.TRAVELS, Travel.class);
//        firebaseCollection.save(travel, new DataCallback<Travel>() {
//            @Override
//            public void onSuccess(Travel data) {
//                new AlertDialog.Builder(CreateTravelActivity.this).setTitle("Travel Details")
//                        .setMessage("Travel Details Successfully saved")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//
//                        }).show();
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.d(TAG, errorMessage);
//            }
//        });
//    }

//    public boolean isValidTravelDeuytails(List details, View view) {
//        TravelFragment fragment = (TravelFragment) details.get(0);
//        Location location = (Location) details.get(1);
//        DateTime dateTime = (DateTime) details.get(2);
//
//        if (location == null) {
//            fragment.setLocationError(view);
//
//            return false;
//        }
//
//       else if (dateTime == null || dateTime.isBeforeNow() ) {
//           fragment.setTimeError(view);
//           fragment.setDateError(view);
//            return false;
//        }
//
//        return true;
//    }



}
