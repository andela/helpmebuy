package com.andela.helpmebuy.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.utilities.LocationPickerDialog;

public class TravelDepartureFragment extends Fragment implements View.OnClickListener {

    private View location;

    private TextView locationValue;

    private View date;

    private TextView dateValue;

    private View time;

    private TextView timeValue;

    private TextView travelInfoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departure_infos, container, false);

        travelInfoView = (TextView)view.findViewById(R.id.travel_info_title);
        travelInfoView.setText(R.string.departure);

        location = view.findViewById(R.id.location);
        location.setOnClickListener(this);
        locationValue = (TextView) view.findViewById(R.id.location_value);

        date = view.findViewById(R.id.date);
        dateValue = (TextView) view.findViewById(R.id.date_value);

        time = view.findViewById(R.id.time);
        timeValue = (TextView) view.findViewById(R.id.time_value);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.location) {
            final LocationPickerDialog dialog = new LocationPickerDialog(getActivity());

            dialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
                @Override
                public void onLocationSet(Location location) {
                    locationValue.setText(location.toString());

                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }
}
