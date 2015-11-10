package com.andela.helpmebuy.locations;


import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.locations.Country;
import com.andela.helpmebuy.utilities.Constants;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Places {
    private List<String> countrys;

    private FirebaseCollection<Country> countriesCollection;

    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> countries;

    private LinkedHashMap<String, List<String>> regions;

    public Places() {
        countrys = new ArrayList<>();
        countriesCollection = new FirebaseCollection<>(Constants.COUNTRIES, Country.class);
    }

    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getCountriesmap() {
        countriesCollection.getMap(new DataCallback<LinkedHashMap<String, LinkedHashMap<String, List<String>>>>() {
            @Override
            public void onSuccess(LinkedHashMap<String, LinkedHashMap<String, List<String>>> data) {
                countries = data;
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        return countries;
    }
    
    public List<String> listOfCountries() {

        for (String key: countries.keySet()) {
            countrys.add(countries.get(key).toString());
        }
        return countrys;
    }

}
