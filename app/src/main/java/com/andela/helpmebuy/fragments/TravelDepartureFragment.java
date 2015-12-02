package com.andela.helpmebuy.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.TimePicker;

import com.andela.helpmebuy.R;

import com.andela.helpmebuy.dialogs.DatePickerFragment;
import com.andela.helpmebuy.dialogs.TimePickerFragment;

import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.utilities.LocationPickerDialog;
import com.andela.helpmebuy.utilities.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TravelDepartureFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private View location;

    private TextView locationValue;

    private View date;

    private TextView dateValue;

    private View time;

    private TextView timeValue;

    private TextView travelInfoView;

    private String departureDateTime;

    private Location departureLocation;

    private View parentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departure_infos, container, false);

        parentLayout = (LinearLayout) view.findViewById(R.id.departure_layout);

        travelInfoView = (TextView)view.findViewById(R.id.travel_info_title);
        travelInfoView.setText(R.string.departure);

        location = view.findViewById(R.id.location);
        location.setOnClickListener(this);
        locationValue = (TextView) view.findViewById(R.id.location_value);
        locationValue.setText(R.string.departure_location_value_summary);

        date = view.findViewById(R.id.date);
        date.setOnClickListener(this);
        dateValue = (TextView) view.findViewById(R.id.date_value);
        dateValue.setText(R.string.departure_date_value_summary);

        time = view.findViewById(R.id.time);
        time.setOnClickListener(this);
        timeValue = (TextView) view.findViewById(R.id.time_value);
        timeValue.setText(R.string.departure_time_value_summary);

        departureDateTime = null;

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        dateValue.setText(String.format("%02d/%02d/%02d", day, month + 1, year));
        clearError(dateValue);
        departureDateTime = dateValue.getText().toString();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hour = Utils.getHourIn12HoursFormat(hourOfDay);
        int second = 00;

        timeValue.setText(String.format("%02d:%02d %s", hour, minute, (hourOfDay < 12) ? "AM" : "PM"));
        clearError(timeValue);
        String timeFormat = String.format("%02d:%02d:%02d", hourOfDay, minute, second);

        departureDateTime += " "+ timeFormat;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location:
                final LocationPickerDialog dialog = new LocationPickerDialog(getActivity());

                dialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
                    @Override
                    public void onLocationSet(Location location) {
                        locationValue.setText(location.toString());
                        clearError(locationValue);
                        TravelDepartureFragment.this.setDepartureLocation(location);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;

            case R.id.date:
                DatePickerFragment dateDialog = new DatePickerFragment();

                dateDialog.setDateSetListener(TravelDepartureFragment.this);
                dateDialog.show(getActivity().getFragmentManager(), DatePickerFragment.TAG);
                break;

            case R.id.time:
                TimePickerFragment timeDialog = new TimePickerFragment();

                timeDialog.setTimeSetListener(TravelDepartureFragment.this);
                timeDialog.show(getActivity().getFragmentManager(), TimePickerFragment.TAG);
                break;

            default:
                break;
        }
    }

    public void setLocationError() {
        locationValue.setError("Please select a Location");
    }

    public void setDepartureDateError() {
        dateValue.setError("Please select a valid departure date");
    }

    public void setDepartureTimeError() {
        timeValue.setError("Please select a valid departure time");
    }

    public void clearError(TextView v) {
        v.setError(null);
    }

    public Location getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(Location departureLocation) {
        this.departureLocation = departureLocation;
    }

    public DateTime getDepartureDateTime() {
        if (departureDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("d/MM/yyyy HH:mm:ss");
            return formatter.parseDateTime(departureDateTime);
        }
        return null;
    }
}
