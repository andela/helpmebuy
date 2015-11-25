package com.andela.helpmebuy.locations;



import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.utilities.Constants;

import java.util.LinkedHashMap;
import java.util.List;

public class Places {
    private FirebaseCollection<Country> countriesCollection;

    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> countries;

    private LinkedHashMap<String, List<String>> regions;

    public Places() {

        countriesCollection = new FirebaseCollection<>(Constants.COUNTRIES, Country.class);
    }

}
