package com.andela.helpmebuy.utilities;


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.andela.helpmebuy.activities.HomeActivity;

import java.util.List;

public class HomeCountryDetector {

    public HomeCountryDetector() {

    }

    private String countryName = "";

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getUserCountry() {
        return "";
    }

    public static String getCountryName(double longitude, double latitude, Activity activity) {
        String street = "";
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        }
        catch (Exception exception) {
        }
        if(addresses != null && addresses.size() > 0 ){

            Address address = addresses.get(0);
            street = address.getCountryName();
        }
        return street;
    }
}
