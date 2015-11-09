package com.andela.helpmebuy.locations;


import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.utilities.Constants;

import java.util.LinkedHashMap;
import java.util.List;

public class Places {
    private FirebaseCollection<Country> countriesCollection;

    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> countries;

    public Places() {
        countriesCollection = new FirebaseCollection<>(Constants.COUNTRIES, Country.class);
    }

    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getCountriesMap() {
        countriesCollection.getMap(new DataCallback<LinkedHashMap<String, LinkedHashMap<String, List<String>>>>() {
            @Override
            public void onSuccess(LinkedHashMap<String, LinkedHashMap<String, List<String>>> data) {
                countries = data;
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
        return countries;
    }


}
