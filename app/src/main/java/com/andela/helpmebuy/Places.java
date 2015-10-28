package com.andela.helpmebuy;


import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.locations.Country;
import com.andela.helpmebuy.utilities.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Places {
    //private List<Country> countries;

    private FirebaseCollection<Country> countriesCollection;

    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> countries;

    public Places() {
        //countries = new ArrayList<>();
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

//    public List<Country> getCountries() {
//
//        countriesCollection.getAll(new DataCallback<List<Country>>() {
//
//            @Override
//            public void onSuccess(List<Country> data) {
//
//                countries = data;
////                for (Country el: data
////                     ) {
////                    countries.add(el);
////                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });
//        return countries;
//    }

}
