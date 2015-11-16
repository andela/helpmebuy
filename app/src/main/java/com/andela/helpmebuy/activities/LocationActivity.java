package com.andela.helpmebuy.activities;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.LocationAdapter;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dialogs.CountryPickerDialog;
import com.andela.helpmebuy.locations.FirebaseCountries;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.views.LocationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    private RecyclerView locationRecyclerView;
    private RecyclerView.Adapter locationAdapter;
    private RecyclerView.LayoutManager locationLayoutManager;

    private LocationView<Country> countriesView;
    private EditText searchViewText;

    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        testButton = (Button) findViewById(R.id.show_location_button);
        testButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                CountryPickerDialog countryPickerDialog = new CountryPickerDialog();
                countryPickerDialog.show(LocationActivity.this.getSupportFragmentManager(),"countries_picker");
            }
        });

//        countriesView = (LocationView<Country>) findViewById(R.id.location_view);
//
//        countriesView.setOnLocationClickedListener(new LocationView.OnLocationClickedListener<Country>() {
//            @Override
//            public void onLocationClicked(Country country) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
//                builder.setMessage("You cliked on " + country.getName())
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                builder.create().show();
//            }
//        });


//        FirebaseCountries countries = new FirebaseCountries();
//        countries.getAll(new DataCallback<List<Country>>() {
//            @Override
//            public void onSuccess(List<Country> data) {
//                countriesView.setLocations(data);
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//            }
//        });
//
//        searchViewText = (EditText) findViewById(R.id.search_view);
//        searchViewText.addTextChangedListener(countriesView.locationFilter());
    }

}
