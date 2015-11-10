package com.andela.helpmebuy.locations;

import android.app.DownloadManager;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Region;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.Query;

import java.util.List;

/**
 * Created by Oluwatosin on 11/10/2015.
 */
public class FirebaseRegions extends FirebaseCollection<Region> {

    public FirebaseRegions(String countryID) {
        super(Constants.REGIONS + "/" + countryID, Region.class);
    }

    public void getByCountry (String countryId) {


    }
}
