package com.andela.helpmebuy.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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



public class TravelFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private View locationView;

    private TextView locationValue;

    private View date;

    private TextView dateValue;

    private View time;

    private TextView timeValue;

    private TextView travelInfoTitle;

    private Location location;

    private String dateTime;

    private int locationValueHint;

    private int dateValueHint;

    private int timeValueHint;

    private int title;

    private int layout;

    private int titleId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);

        initializeComponents(view);

        setViewText();

        setOnClickListeners();


        return view;
    }

    public void initializeComponents(View v) {
        locationView = v.findViewById(R.id.location);
        date = v.findViewById(R.id.date);
        time = v.findViewById(R.id.time);
        locationValue = (TextView) v.findViewById(R.id.location_value);
        dateValue = (TextView) v.findViewById(R.id.date_value);
        timeValue = (TextView) v.findViewById(R.id.time_value);
        travelInfoTitle = (TextView) v.findViewById(titleId);
    }

    public void setOnClickListeners() {
        locationView.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
    }

    public void setViewText() {
        travelInfoTitle.setText(title);
        locationValue.setText(locationValueHint);
        dateValue.setText(dateValueHint);
        timeValue.setText(timeValueHint);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        dateValue.setText(String.format("%02d/%02d/%02d", day, month + 1, year));
        clearError(dateValue);
        dateTime = dateValue.getText().toString();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hour = Utils.getHourIn12HoursFormat(hourOfDay);
        int second = 0;

        timeValue.setText(String.format("%02d:%02d %s", hour, minute, ((hourOfDay < 12) ? "AM" : "PM")));
        String timeFormat = String.format("%02d:%02d:%02d", hourOfDay, minute, second);
        clearError(timeValue);

        dateTime += " " + timeFormat;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location :
                displayLocationDialog();
                break;

            case R.id.date :
                displayDatePicker();
                break;

            case R.id.time :
                displayTimePicker();
                break;
        }
    }

    protected void displayLocationDialog() {
        final LocationPickerDialog dialog = new LocationPickerDialog(getActivity());

        dialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
            @Override
            public void onLocationSet(Location location) {
                locationValue.setText(location.toString());
                clearError(locationValue);
                setLocation(location);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void displayDatePicker() {
        DatePickerFragment dateDialog = new DatePickerFragment();

        dateDialog.setDateSetListener(this);
        dateDialog.show(getActivity().getFragmentManager(), DatePickerFragment.TAG);
    }

    protected void displayTimePicker() {
        TimePickerFragment timeDialog = new TimePickerFragment();

        timeDialog.setTimeSetListener(this);
        timeDialog.show(getActivity().getFragmentManager(), TimePickerFragment.TAG);
    }

    protected void setViewId(int layout) {
        this.layout = layout;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public DateTime getDateTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d/MM/yyyy HH:mm:ss");

        if (dateTime != null) {
            return formatter.parseDateTime(dateTime);
        }

        return null;
    }

    public String getDate() {
        return dateValue.getText().toString();
    }

    public String getTime() {
        return timeValue.getText().toString();
    }

    public void setTitleId(int id) {
        titleId = id;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public void setLocationValueHint(int hint) {
        locationValueHint = hint;
    }

    public void setDateValueHint(int hint) {
        dateValueHint = hint;
    }

    public void setTimeValueHint(int hint) {
        timeValueHint = hint;
    }

    public void setLocationError( View view) {
        locationValue.setError("Please select a location");
        Snackbar.make(view.getRootView(), "Please select a valid location", Snackbar.LENGTH_SHORT).show();
    }

    public void setDateError(View view) {
        dateValue.setError("Please select a valid Date");
        Snackbar.make(view.getRootView(), "Please select a valid Date", Snackbar.LENGTH_SHORT).show();
    }
    public void setTimeError(View view) {
        timeValue.setError("Please select a valid Time");
        Snackbar.make(view.getRootView(), "Please select a valid Time", Snackbar.LENGTH_SHORT).show();
    }

    public void clearError(TextView v) {
        v.setError(null);
    }



}
