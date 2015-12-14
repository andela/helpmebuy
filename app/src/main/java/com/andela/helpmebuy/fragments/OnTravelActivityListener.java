package com.andela.helpmebuy.fragments;


import android.app.Activity;
import android.view.View;

import java.util.ArrayList;

public interface OnTravelActivityListener {
    void onNextButtonClicked(View view, ArrayList<String> departureDetails);
    void onPreviousButtonClicked(ArrayList<String> departureDetails);
    void onSaveButtonClicked(View view, ArrayList<String> arrivalDetails);
}
