package com.andela.helpmebuy.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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

public class TravelArrivalFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private View location;

    private TextView locationValue;

    private View date;

    private TextView dateValue;

    private View time;

    private TextView timeValue;

    private TextView travelInfoTitle;

    private String arrivalDateTime;

    private Location arrivalLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_arrival_infos, container, false);

        travelInfoTitle = (TextView)view.findViewById(R.id.travel_info_title);
        travelInfoTitle.setText(R.string.arrival);

        location = view.findViewById(R.id.location);
        location.setOnClickListener(this);
        locationValue = (TextView)view.findViewById(R.id.location_value);
        locationValue.setText(R.string.arrival_location_value_summary);

        date = view.findViewById(R.id.date);
        date.setOnClickListener(this);
        dateValue = (TextView) view.findViewById(R.id.date_value);
        dateValue.setText(R.string.arrival_date_value_summary);

        time = view.findViewById(R.id.time);
        time.setOnClickListener(this);
        timeValue = (TextView) view.findViewById(R.id.time_value);
        timeValue.setText(R.string.arrival_time_value_summary);

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        dateValue.setText(String.format("%02d/%02d/%02d", day, month + 1, year));
        clearError(dateValue);
        arrivalDateTime = dateValue.getText().toString();
    }

    @Override
    public void onTimeSet(TimePicker view,int hourOfDay, int minute) {
        int hour = Utils.getHourIn12HoursFormat(hourOfDay);
        int second = 00;

        timeValue.setText(String.format("%02d:%02d %s", hour, minute, ((hourOfDay < 12) ? "AM" : "PM")));
        clearError(timeValue);
        String timeFormat = String.format("%02d:%02d:%02d", hourOfDay, minute, second);

        arrivalDateTime += " "+ timeFormat;
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
                        TravelArrivalFragment.this.setArrivalLocation(location);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;

            case R.id.date:
                DatePickerFragment dateDialog = new DatePickerFragment();

                dateDialog.setDateSetListener(this);
                dateDialog.show(getActivity().getFragmentManager(), DatePickerFragment.TAG);
                break;

            case R.id.time:
                TimePickerFragment timeDialog = new TimePickerFragment();

                timeDialog.setTimeSetListener(TravelArrivalFragment.this);
                timeDialog.show(getActivity().getFragmentManager(), TimePickerFragment.TAG);
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

    public Location getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(Location arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public DateTime getArrivalDateTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d/MM/yyyy HH:mm:ss");
        return formatter.parseDateTime(arrivalDateTime);
    }
}
