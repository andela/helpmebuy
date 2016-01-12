package com.andela.helpmebuy.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.fragments.UserSettingsFragment;
import java.util.List;

public class UserSettingsActivity extends AppCompatActivity {

    private FrameLayout parentLayout;
    private UserSettingsFragment userSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentLayout = (FrameLayout)findViewById(R.id.user_settings_layout);
        userSettingsFragment = new UserSettingsFragment();

       //addUserSettingsFragment(parentLayout.getId(),userSettingsFragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void addUserSettingsFragment(int layout, Fragment fragment) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(layout, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
