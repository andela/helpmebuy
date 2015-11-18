package com.andela.helpmebuy.dialogs;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.andela.helpmebuy.activities.HomeActivity;
import com.andela.helpmebuy.activities.SigninActivity;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.DataCollection;
import com.andela.helpmebuy.locations.FirebaseCities;
import com.andela.helpmebuy.models.City;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.models.Region;
import com.andela.helpmebuy.utilities.Launcher;
import com.andela.helpmebuy.views.LocationView;

import java.util.List;

public class CityPickerDialog extends DialogFragment {

    private DataCollection<City> cities;
    private LocationView<City> citiesView;
    private static final String LOCATION = "location";
    public static String userLocation = "";


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
        final Region region = getArguments().getParcelable(RegionPickerDialog.REGION);
        final Country country = getArguments().getParcelable(RegionPickerDialog.COUNTRY);

        if (region != null) {
            builder.setTitle(region.getName());
        }

        citiesView.setOnLocationClickedListener(new LocationView.OnLocationClickedListener<City>() {
            public void onLocationClicked(City city) {
                userLocation = region.getName() + ","+country.getName();

                Bundle bundle = new Bundle();
                bundle.putParcelable(LOCATION, city);

                Launcher.launchActivity(getContext(), HomeActivity.class);
                dismiss();
            }

        });

        builder.setView(citiesView);
        return builder.create();
    }


    private void initializeCitiesView() {
        Region region = getArguments().getParcelable(RegionPickerDialog.REGION);

        if (region != null) {
            citiesView = new LocationView<>(getActivity());

            cities = new FirebaseCities(region.getId());

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
}
