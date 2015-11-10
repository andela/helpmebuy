package com.andela.helpmebuy.locations;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.Firebase;

import java.util.List;


public class FirebaseCountries extends FirebaseCollection<Country> {

    private Firebase firebase;

    private String childName;

    private Class<Country> type;

    public FirebaseCountries() {
        super(Constants.COUNTRIES, Country.class);

    }


}
