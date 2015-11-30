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

    private Date departureDate;

    private Date departureTime;

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

        DateFormat dateFormat = new SimpleDateFormat("MM/d/yyyy");
        dateValue.setText(String.format("%02d/%02d/%02d", day, month + 1, year));
        try {
            TravelDepartureFragment.this.setDepartureDate(dateFormat.parse(dateValue.getText().toString()));
        }
        catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        dateValue.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTimeSet(TimePicker view,int hourOfDay, int minute) {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        int hour = Utils.getHourIn12HoursFormat(hourOfDay);

        timeValue.setText(String.format("%02d:%02d %s", hour, minute, (hour < 12) ? "AM" : "PM"));

        try {
            TravelDepartureFragment.this.setDepartureTime(timeFormat.parse(dateValue.getText().toString()));
        }
        catch (ParseException parseException) {
            parseException.printStackTrace();
        }

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

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Location getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(Location departureLocation) {
        this.departureLocation = departureLocation;
    }

    public DateTime getDepartureDateTime() {
        System.out.println(getDepartureDate());
        System.out.println(getDepartureTime());
        return new DateTime();
    }
}
