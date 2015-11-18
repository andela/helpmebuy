package com.andela.helpmebuy.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.DataCollection;
import com.andela.helpmebuy.locations.FirebaseRegions;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.models.Region;
import com.andela.helpmebuy.views.LocationView;

import java.util.List;

public class RegionPickerDialog extends DialogFragment {
    public static final String REGION = "region";

    private LocationView<Region> regionsView;

    public RegionPickerDialog() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        initializeRegionsView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(regionsView);

        Country country = getArguments().getParcelable(CountryPickerDialog.COUNTRY);
        if (country != null) {
            builder.setTitle(country.getName());
        }

        return builder.create();
    }

    private void initializeRegionsView() {
        Country country = getArguments().getParcelable(CountryPickerDialog.COUNTRY);

        regionsView = new LocationView<>(getActivity());
        regionsView.setOnLocationClickedListener(new LocationView.OnLocationClickedListener<Region>() {
            @Override
            public void onLocationClicked(Region region) {
                CityPickerDialog dialog = new CityPickerDialog();

                Bundle arguments = new Bundle();
                arguments.putParcelable(REGION, region);

                dialog.setArguments(arguments);
                dialog.show(getActivity().getSupportFragmentManager(), "citypickerdialog");
            }
        });

        if (country != null) {
            DataCollection<Region> regions = new FirebaseRegions(country.getId());
            regions.getAll(new DataCallback<List<Region>>() {
                @Override
                public void onSuccess(List<Region> data) {
                    regionsView.setLocations(data);
                }

                @Override
                public void onError(String errorMessage) {
                }
            });
        }
    }
}
