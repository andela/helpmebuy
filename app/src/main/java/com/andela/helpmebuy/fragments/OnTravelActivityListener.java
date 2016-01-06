package com.andela.helpmebuy.fragments;

import android.view.View;

import com.andela.helpmebuy.models.Location;

import java.util.ArrayList;

public interface OnTravelActivityListener {
    void onNextButtonClicked(View view, ArrayList<String> departureDetails, Location travelLocation);
    void onPreviousButtonClicked(ArrayList<String> departureDetails, Location travelLocation);
    void onSaveButtonClicked(View view, ArrayList<String> arrivalDetails, Location travelLocation);
}
