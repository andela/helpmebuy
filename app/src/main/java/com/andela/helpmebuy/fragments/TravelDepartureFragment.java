package com.andela.helpmebuy.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.helpmebuy.R;

import java.util.ArrayList;

public class TravelDepartureFragment extends TravelFragment implements OnTravelFragmentListener {

    public static final String TRAVEL_DEPARTURE_KEY = "TRAVEL_DEPARTURE_KEY";

    public TravelDepartureFragment(){
        mFragmentListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public int viewLayout() {
        return R.layout.fragment_departure_infos;
    }

    @Override
    public int titleId() {
        return R.id.travel_info_title;
    }

    @Override
    public String titleValue() {
        return getResources().getString(R.string.departure);
    }

    @Override
    public String locationValue() {
        return super.getTravelItem(getArguments(), TRAVEL_DEPARTURE_KEY, 0, getResources().getString(R.string.departure_location_value_summary));
    }

    @Override
    public String dateValue() {
        return super.getTravelItem(getArguments(), TRAVEL_DEPARTURE_KEY, 1, getResources().getString(R.string.departure_date_value_summary));
    }

    @Override
    public String timeValue() {
        return super.getTravelItem(getArguments(), TRAVEL_DEPARTURE_KEY, 2, getResources().getString(R.string.departure_time_value_summary));
    }
}
