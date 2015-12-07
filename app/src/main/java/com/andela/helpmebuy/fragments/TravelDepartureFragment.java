package com.andela.helpmebuy.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.helpmebuy.R;

public class TravelDepartureFragment extends TravelFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setViewId(R.layout.fragment_departure_infos);
        super.setTitleId(R.id.travel_info_title);
        super.setTitle(R.string.departure);
        super.setLocationValueHint(R.string.departure_location_value_summary);
        super.setDateValueHint(R.string.departure_date_value_summary);
        super.setTimeValueHint(R.string.departure_time_value_summary);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
