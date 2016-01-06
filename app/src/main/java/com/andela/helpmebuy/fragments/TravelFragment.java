package com.andela.helpmebuy.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dialogs.DatePickerFragment;
import com.andela.helpmebuy.dialogs.TimePickerFragment;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.utilities.LocationPickerDialog;
import com.andela.helpmebuy.utilities.HourFormatter;

import java.util.ArrayList;


public abstract class TravelFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private View locationView, date, time;

    private TextView locationValue, dateValue, timeValue, travelInfoTitle;

    ImageView nextButton, previousButton, saveButton;

    ArrayList<String> travelDetails;
    Location travelLocation;

    OnTravelActivityListener mActivityListener;

    public TravelFragment(){
        travelDetails = new ArrayList<>();
        travelDetails.add(0, "");
        travelDetails.add(1, "");
        travelDetails.add(2, "");
    }

    public void setActivityListener(OnTravelActivityListener onTravelActivityListener){
        this.mActivityListener = onTravelActivityListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(viewLayout(), container, false);

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
        travelInfoTitle = (TextView) v.findViewById(titleId());

        if (v.findViewById(R.id.next) != null) {
            nextButton = (ImageView) v.findViewById(R.id.next);

            nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (verifyDetails(travelDetails)) {
                        mActivityListener.onNextButtonClicked(v, travelDetails, travelLocation);
                    }
                }
            });
        }

        if (v.findViewById(R.id.previous) != null) {
            previousButton = (ImageView) v.findViewById(R.id.previous);

            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivityListener.onPreviousButtonClicked(travelDetails, travelLocation);
                }
            });
        }

        if (v.findViewById(R.id.save) != null) {
            saveButton = (ImageView) v.findViewById(R.id.save);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (verifyDetails(travelDetails)) {
                        mActivityListener.onSaveButtonClicked(v, travelDetails, travelLocation);
                    }
                }
            });
        }
    }

    public void setOnClickListeners() {
        locationView.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
    }

    public void setViewText() {
        travelInfoTitle.setText(titleValue() );
        locationValue.setText(locationValue());
        dateValue.setText(dateValue());
        timeValue.setText(timeValue());
    }

    public boolean verifyDetails(ArrayList<String> details){
        boolean proceed = true;

        if (details.get(0).isEmpty()){
            setLocationError(getView());
            proceed = false;
        }

        if (details.get(1).isEmpty()){
            setDateError(getView());
            proceed = false;
        }

        if (details.get(2).isEmpty()){
            setTimeError(getView());
            proceed = false;
        }

        return proceed;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String dateVal = String.format("%02d/%02d/%02d", day, month + 1, year);

        dateValue.setText(dateVal);
        clearError(dateValue);
        travelDetails.set(1, dateVal);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hour = HourFormatter.getHourIn12HoursFormat(hourOfDay);
        String timeVal = String.format("%02d:%02d %s", hour, minute, ((hourOfDay < 12) ? "AM" : "PM"));

        timeValue.setText(timeVal);
        travelDetails.set(2, timeVal);
        clearError(timeValue);
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
                travelLocation = location;
                String locationVal = location.toString();

                locationValue.setText(locationVal);
                clearError(locationValue);
                travelDetails.set(0, location.toString());

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

    private ArrayList<String> getTravelBundle(Bundle bundle, String key){
        ArrayList<String> details = new ArrayList<>();

        if (bundle.getStringArrayList(key) != null){
            details = bundle.getStringArrayList(key);
            assert details != null;
            Log.d("HMB", " THE BUNDLE: " + details.get(0));
        }

        return details;
    }

    public String getTravelItem(Bundle bundle, String key, int position, String hint){
        ArrayList<String> details = getTravelBundle(bundle, key);
        if (details.size() > 0 && !(details.get(position).isEmpty()))
            return details.get(position);
        else
            return hint;
    }

    public abstract int viewLayout();

    public abstract int titleId();

    public abstract String titleValue();

    public abstract String locationValue();

    public abstract String dateValue();

    public abstract String timeValue();
}
