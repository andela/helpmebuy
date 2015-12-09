package com.andela.helpmebuy.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.helpmebuy.R;

public class TravelArrivalFragment extends TravelFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setViewId(R.layout.fragment_arrival_infos);
        super.setTitleId(R.id.travel_info_title);
        super.setTitle(R.string.arrival);
        super.setLocationValueHint(R.string.arrival_location_value_summary);
        super.setDateValueHint(R.string.arrival_date_value_summary);
        super.setTimeValueHint(R.string.arrival_time_value_summary);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public String setLocationValue() {
        return "";
    }


}
