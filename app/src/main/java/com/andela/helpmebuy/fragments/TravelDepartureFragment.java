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

    private DateTime departureDate;

    private DateTime departureTime;

    private String departureDateTime;

    private Location departureLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departure_infos, container, false);

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

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d/MM/yyyy");
        dateValue.setText(String.format("%02d/%02d/%02d", day, month + 1, year));

        departureDateTime = dateValue.getText().toString();
        TravelDepartureFragment.this.setDepartureDate(formatter.parseDateTime(departureDateTime));
        dateValue.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        int hour = Utils.getHourIn12HoursFormat(hourOfDay);
        int second = 00;

        timeValue.setText(String.format("%02d:%02d %s", hour, minute, (hour < 12) ? "AM" : "PM"));
        String timeFormat = String.format("%02d:%02d:%02d", hourOfDay, minute, second);

        departureDateTime += " "+ timeFormat;
        TravelDepartureFragment.this.setDepartureTime(formatter.parseDateTime(timeFormat));
        timeValue.setVisibility(View.VISIBLE);
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
                        TravelDepartureFragment.this.setDepartureLocation(location);
                        locationValue.setVisibility(View.VISIBLE);

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

    public DateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(DateTime departureDate) {
        this.departureDate = departureDate;
    }

    public DateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(DateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Location getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(Location departureLocation) {
        this.departureLocation = departureLocation;
    }

    public DateTime getDepartureDateTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d/MM/yyyy HH:mm:ss");
        return formatter.parseDateTime(departureDateTime);
    }
}
