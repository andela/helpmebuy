package com.andela.helpmebuy.locations;

import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.utilities.Constants;

public class FirebaseCountries extends FirebaseCollection<Country> {
    public FirebaseCountries() {
        super(Constants.COUNTRIES, Country.class);
    }
}
