package com.andela.helpmebuy.locations;

import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.City;
import com.andela.helpmebuy.utilities.Constants;

/**
 * Created by Oluwatosin on 11/10/2015.
 */
public class FirebaceCities extends FirebaseCollection<City> {
    public FirebaceCities(String regionID) {
        super(Constants.CITIES + "/" + regionID, City.class);
    }
}
