package com.andela.helpmebuy.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andela.helpmebuy.R;

public class TravelArrivalFragment extends TravelFragment implements OnTravelFragmentListener {

    public static final String TRAVEL_ARRIVAL_KEY = "TRAVEL_ARRIVAL_KEY";

    public TravelArrivalFragment(){
        mFragmentListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int viewLayout() {
        return R.layout.fragment_arrival_infos;
    }

    @Override
    public int titleId() {
        return R.id.travel_info_title;
    }

    @Override
    public String titleValue() {
        return getResources().getString(R.string.arrival);
    }

    @Override
    public String locationValue() {
        return super.getTravelItem(getArguments(), TRAVEL_ARRIVAL_KEY, 0, getResources().getString(R.string.arrival_location_value_summary));
    }

    @Override
    public String dateValue() {
        return super.getTravelItem(getArguments(), TRAVEL_ARRIVAL_KEY, 1, getResources().getString(R.string.arrival_date_value_summary));
    }

    @Override
    public String timeValue() {
        return super.getTravelItem(getArguments(), TRAVEL_ARRIVAL_KEY, 2, getResources().getString(R.string.arrival_time_value_summary));
    }
}
