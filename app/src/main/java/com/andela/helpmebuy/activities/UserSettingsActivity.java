package com.andela.helpmebuy.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.fragments.UserSettingsFragment;
import com.andela.helpmebuy.utilities.ActionBar;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar.enableHomeButton(this);
        addUserSettingsFragment(savedInstanceState);
    }

    public void addUserSettingsFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }
        UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
        userSettingsFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, userSettingsFragment)
                .commit();
    }

}
