package com.andela.helpmebuy.utilities;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.andela.helpmebuy.dialogs.CityPickerDialog;
import com.andela.helpmebuy.dialogs.CountryPickerDialog;
import com.andela.helpmebuy.dialogs.RegionPickerDialog;
import com.andela.helpmebuy.models.City;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Region;

public class LocationPickerDialog {

    private Context context;

    private CountryPickerDialog countryPickerDialog;

    private RegionPickerDialog regionPickerDialog;

    private CityPickerDialog cityPickerDialog;

    private OnLocationSetListener listener;

    public LocationPickerDialog(Context context) {
        this.context = context;

        initialize();
    }

    public void setOnLocationSetListener(OnLocationSetListener listener) {
        this.listener = listener;
    }

    public void show() {
        if (!countryPickerDialog.isAdded()) {
            countryPickerDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), CountryPickerDialog.TAG);
        }
    }

    public void dismiss() {
        cityPickerDialog.dismiss();
        regionPickerDialog.dismiss();
        countryPickerDialog.dismiss();
    }

    private void initialize() {
        countryPickerDialog = new CountryPickerDialog();
        regionPickerDialog = new RegionPickerDialog();
        cityPickerDialog = new CityPickerDialog();

        countryPickerDialog.setOnCountrySetListener(new CountryPickerDialog.OnCountrySetListener() {
            @Override
            public void onCountrySet(Country country) {
                Bundle args = new Bundle();
                args.putParcelable(CountryPickerDialog.COUNTRY, country);

                if (regionPickerDialog.getArguments() == null) {
                    regionPickerDialog.setArguments(args);
                }

                if (!regionPickerDialog.isAdded()) {
                    regionPickerDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), RegionPickerDialog.TAG);
                }
            }
        });

        regionPickerDialog.setOnRegionSetListener(new RegionPickerDialog.OnRegionSetListener() {
            @Override
            public void onRegionSet(Region region) {
                Bundle args = new Bundle();
                args.putParcelable(CountryPickerDialog.COUNTRY, regionPickerDialog.getArguments().getParcelable(CountryPickerDialog.COUNTRY));
                args.putParcelable(RegionPickerDialog.REGION, region);

                if (cityPickerDialog.getArguments() == null) {
                    cityPickerDialog.setArguments(args);
                }

                if (!cityPickerDialog.isAdded()) {
                    cityPickerDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), CityPickerDialog.TAG);
                }
            }
        });

        cityPickerDialog.setOnCitySetListener(new CityPickerDialog.OnCitySetListener() {
            @Override
            public void onCitySet(City city) {
                Country country = cityPickerDialog.getArguments().getParcelable(CountryPickerDialog.COUNTRY);
                Region region = cityPickerDialog.getArguments().getParcelable(RegionPickerDialog.REGION);

                Location location = new Location(country, region, city);

                if (listener != null) {
                    listener.onLocationSet(location);
                }
            }
        });
    }

    public interface OnLocationSetListener {
        void onLocationSet(Location location);
    }
}
