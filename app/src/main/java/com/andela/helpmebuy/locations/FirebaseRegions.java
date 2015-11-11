package com.andela.helpmebuy.locations;

import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Region;
import com.andela.helpmebuy.utilities.Constants;

public class FirebaseRegions extends FirebaseCollection<Region> {

    public FirebaseRegions(String countryID) {
        super(Constants.REGIONS + "/" + countryID, Region.class);
    }
}
