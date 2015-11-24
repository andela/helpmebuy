package com.andela.helpmebuy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.andela.helpmebuy.R;

public class TravelArrivalFragment extends Fragment {

    private View location;

    private TextView locationValue;

    private View date;

    private TextView dateValue;

    private View time;

    private TextView timeValue;

    private TextView travelInfoTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_arrival_infos, container, false);

        travelInfoTitle = (TextView)view.findViewById(R.id.travel_info_title);
        travelInfoTitle.setText(R.string.arrival);

        location = view.findViewById(R.id.location);
        locationValue = (TextView)view.findViewById(R.id.location_value);

        date = view.findViewById(R.id.date);
        dateValue = (TextView) view.findViewById(R.id.date_value);

        time = view.findViewById(R.id.time);
        timeValue = (TextView) view.findViewById(R.id.time_value);

        return view;
    }
}
