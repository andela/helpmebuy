package com.andela.helpmebuy.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.DataCollection;
import com.andela.helpmebuy.locations.FirebaseRegions;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.models.Region;
import com.andela.helpmebuy.views.LocationView;

import java.util.List;

public class RegionPickerDialog extends DialogFragment {
    public static final String TAG = "RegionPickerDialog";

    public static final String REGION = "region";

    private LocationView<Region> regionsView;

    private OnRegionSetListener listener;

    public RegionPickerDialog() {}

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

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

    public void setOnRegionSetListener(OnRegionSetListener listener) {
        this.listener = listener;
    }

    private void initializeRegionsView() {
        final Country country = getArguments().getParcelable(CountryPickerDialog.COUNTRY);

        regionsView = new LocationView<>(getActivity());
        regionsView.setOnLocationClickedListener(new LocationView.OnLocationClickedListener<Region>() {
            @Override
            public void onLocationClicked(Region region) {
                if (listener != null) {
                    listener.onRegionSet(region);

                    return;
                }

                CityPickerDialog dialog = new CityPickerDialog();

                Bundle arguments = new Bundle();

                arguments.putParcelable(CountryPickerDialog.COUNTRY, country);

                arguments.putParcelable(REGION, region);

                dialog.setArguments(arguments);

                dialog.show(getActivity().getSupportFragmentManager(), CityPickerDialog.TAG);

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

    public interface OnRegionSetListener {
        void onRegionSet(Region region);
    }
}
