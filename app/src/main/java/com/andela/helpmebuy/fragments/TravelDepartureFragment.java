package com.andela.helpmebuy.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.helpmebuy.R;

public class TravelDepartureFragment extends TravelFragment {

    public static final String KEY_LOCATION = "location";

    public static final String KEY_DATE = "date";

    public static final String KEY_TIME = "time";

    private TextView location;

    private TextView date;

    private TextView time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        location = (TextView)container.findViewById(R.id.location_value);
        date = (TextView)container.findViewById(R.id.date_value);
        time = (TextView)container.findViewById(R.id.time_value);

        super.setViewId(R.layout.fragment_departure_infos);
        super.setTitleId(R.id.travel_info_title);
        super.setTitle(R.string.departure);
        super.setLocationValueHint(R.string.departure_location_value_summary);
        super.setDateValueHint(R.string.departure_date_value_summary);
        super.setTimeValueHint(R.string.departure_time_value_summary);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            if (savedInstanceState != null) {
                location.setText(savedInstanceState.getString(KEY_LOCATION, "location"));
                date.setText(savedInstanceState.getString(KEY_DATE, "date"));
                time.setText(savedInstanceState.getString(KEY_TIME, "time"));
            }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_LOCATION, location.toString());
        savedInstanceState.putString(KEY_DATE, date.toString());
        savedInstanceState.putString(KEY_TIME, time.toString());
    }
}
