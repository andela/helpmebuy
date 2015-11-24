package com.andela.helpmebuy.dialogs;



import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.DataCollection;
import com.andela.helpmebuy.locations.FirebaseCountries;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.views.LocationView;

import java.util.List;

public class CountryPickerDialog extends DialogFragment {
    public static final String TAG = "CountryPickerDialog";

    public static final String COUNTRY = "Country";

    private DataCollection<Country> countries;

    private LocationView<Country> countriesView;

    private OnCountrySetListener listener;

    public CountryPickerDialog() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        initializeCountriesView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(countriesView);

        return builder.create();
    }

    public void setOnCountrySetListener(OnCountrySetListener listener) {
        this.listener = listener;
    }

    private void initializeCountriesView() {
        countriesView = new LocationView<>(getActivity());
        countriesView.setOnLocationClickedListener(new LocationView.OnLocationClickedListener<Country>() {
            @Override
            public void onLocationClicked(Country country) {
                if (listener != null) {
                    listener.onCountrySet(country);

                    return;
                }

                RegionPickerDialog dialog = new RegionPickerDialog();

                Bundle arguments = new Bundle();

                arguments.putParcelable(COUNTRY, country);

                dialog.setArguments(arguments);

                dialog.show(getActivity().getSupportFragmentManager(), "regionpickerdialog");
            }
        });

        countries = new FirebaseCountries();
        countries.getAll(new DataCallback<List<Country>>() {
            @Override
            public void onSuccess(List<Country> data) {
                countriesView.setLocations(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public interface OnCountrySetListener {
        void onCountrySet(Country country);
    }

}
