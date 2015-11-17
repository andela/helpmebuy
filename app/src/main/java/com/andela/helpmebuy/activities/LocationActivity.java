package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dialogs.CityPickerDialog;
import com.andela.helpmebuy.dialogs.CountryPickerDialog;

public class LocationActivity extends AppCompatActivity {

    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        testButton = (Button) findViewById(R.id.show_location_button);
        testButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //CountryPickerDialog countryPickerDialog = new CountryPickerDialog();
                //countryPickerDialog.show(LocationActivity.this.getSupportFragmentManager(),"countries_picker");
                CityPickerDialog cityPickerDialog = new CityPickerDialog();
                cityPickerDialog.show(LocationActivity.this.getSupportFragmentManager(),"cities_picker");
            }
        });
    }

}
