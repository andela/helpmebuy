package com.andela.helpmebuy.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andela.helpmebuy.R;

import java.text.DateFormat;
import java.util.Calendar;


public class HMBTimePickerDialog extends DialogFragment {

    public static String time;
    public int am_pm;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        am_pm = calendar.get(Calendar.AM_PM);

        return new android.app.TimePickerDialog(getActivity(),onTimeSetListener,hour,minute,false);
    }

    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener =  onTimeSetListener;
    }


}
