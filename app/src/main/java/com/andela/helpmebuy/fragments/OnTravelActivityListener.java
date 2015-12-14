package com.andela.helpmebuy.fragments;


import android.app.Activity;
import android.view.View;

import java.util.ArrayList;

public interface OnTravelActivityListener {
    void onNextButtonClicked(ArrayList<String> detartureDetails);
    void onPreviousButtonClicked(ArrayList<String> detartureDetails);
    void onSaveButtonClicked(ArrayList<String> detartureDetails);
}
