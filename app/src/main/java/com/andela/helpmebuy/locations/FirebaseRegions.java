package com.andela.helpmebuy.locations;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.utilities.Constants;

import java.util.List;

/**
 * Created by Oluwatosin on 11/10/2015.
 */
public class FirebaseRegions extends FirebaseCollection<Region> {

    public FirebaseRegions() {
        super(Constants.REGIONS, Region.class);
    }

    public void getByCountry (String countryId) {
        getAll(new DataCallback<List<Region>>() {
            @Override
            public void onSuccess(List<Region> data) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }
}
