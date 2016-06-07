package com.andela.helpmebuy.utilities;

import android.support.v7.app.AppCompatActivity;

public class ActionBar {

    public static void enableHomeButton(AppCompatActivity activity) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
