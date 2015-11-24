package com.andela.helpmebuy.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment {
    public static final String TAG = "TimePickerFragment";

    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new android.app.TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, false);
    }

    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener =  onTimeSetListener;
    }
}
