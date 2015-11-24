package com.andela.helpmebuy.fragments;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TimePicker;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dialogs.HMBDatePickerDialog;
import com.andela.helpmebuy.dialogs.HMBTimePickerDialog;

public class TravelDepartureFragment extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private View location;

    private TextView locationValue;

    private View date;

    private TextView dateValue;

    private View time;

    private TextView timeValue;

    private TextView travelInfoView;

    private TimePickerDialog timePickerDialog;

    private LinearLayout timeLayout;

    private LinearLayout dateLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departure_infos, container, false);

        travelInfoView = (TextView)view.findViewById(R.id.travel_info_title);

        travelInfoView.setText(R.string.departure);
        location = view.findViewById(R.id.location);
        locationValue = (TextView) view.findViewById(R.id.location_value);

        date = view.findViewById(R.id.date);
        dateValue = (TextView) view.findViewById(R.id.date_value);

        dateLayout = (LinearLayout) view.findViewById(R.id.date);
        dateLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HMBDatePickerDialog dateDialog = new HMBDatePickerDialog();
                dateDialog.setDateSetListener(TravelDepartureFragment.this);
                dateDialog.show(getActivity().getFragmentManager(), "Date Picker");
            }
        });

        time = view.findViewById(R.id.time);
        timeValue = (TextView) view.findViewById(R.id.time_value);

        timeLayout = (LinearLayout) view.findViewById(R.id.time);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HMBTimePickerDialog timeDialog = new HMBTimePickerDialog();
                timeDialog.setTimeSetListener(TravelDepartureFragment.this);
                timeDialog.show(getActivity().getFragmentManager(), "Time Picker");

            }
        });

        return view;
    }

    public void onTimeSet(TimePicker view,int hourOfDay, int minute) {
        String am_pm = (hourOfDay < 12) ? "AM" : "PM";
        int hour = getSelectedHour(hourOfDay,am_pm);
        String selectedTime = String.format("%02d", hour) +":"+String.format("%02d", minute)+" "+am_pm;
        timeValue.setText(selectedTime);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int selectedMonth =  month+1;
        String selectedDate = String.format("%02d", year) + "/"+String.format("%02d",selectedMonth)+"/"+ String.format("%02d",day);
        dateValue.setText(selectedDate);
    }

    private int getSelectedHour(int hourOfDay, String am_pm) {
        int hour = 0;
        if(hourOfDay > 12 && am_pm == "PM") {
            hour = hourOfDay % 12;
        }
        return hour;
    }

}
