package com.andela.helpmebuy.dialogs;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.DataCollection;
import com.andela.helpmebuy.locations.FirebaseCities;
import com.andela.helpmebuy.models.City;
import com.andela.helpmebuy.views.LocationView;

import java.util.List;

public class CityPickerDialog extends DialogFragment {

    private DataCollection<City> cities;
    private LocationView<City> citiesView;

    public CityPickerDialog() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initializeCitiesView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        builder.setView(citiesView);
        return builder.create();
    }


    private void initializeCitiesView() {
        String regionId = getArguments().getString(RegionPickerDialog.REGION_ID);

        citiesView = new LocationView<City>(getActivity());
        cities = new FirebaseCities(regionId);

        cities.getAll(new DataCallback<List<City>>() {
            @Override
            public void onSuccess(List<City> data) {
                citiesView.setLocations(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
