package com.andela.helpmebuy.locations;

import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.City;
import com.andela.helpmebuy.utilities.Constants;

public class FirebaseCities extends FirebaseCollection<City> {
    public FirebaseCities(String regionID) {
        super(Constants.CITIES + "/" + regionID, City.class);
    }
}
