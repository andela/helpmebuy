package com.andela.helpmebuy.utilities;


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.andela.helpmebuy.config.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class HomeCountryDetector implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {


   private GoogleApiClient googleApiClient;
   private LocationRequest locationRequest;
   private String countryName;
   Activity activity;
   private double longitude;
   private double latitude;
   private HomeCountryDetectorListener listener;


    public String getCountryName() {
        return countryName;
    }

    public void setListener(HomeCountryDetectorListener listener) {
        this.listener = listener;
    }

    public HomeCountryDetector(Activity activity) {
        this.activity = activity;
        longitude = 0;
        latitude = 0;


        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000 );
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void onLocationChanged(Location location) {
       longitude = location.getLongitude();
       latitude = location.getLatitude();
       detectCountry();
    }

    public void detectCountry() {
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses != null && addresses.size() > 0 ){

                Address address = addresses.get(0);
                countryName = address.getCountryName();
                listener.onCountryDetected(countryName);
            }
        }
        catch (Exception exception) {
            loadFromHttp();
        }


    }

    private void loadFromHttp() {

        String latLng = Double.toString(latitude) + "," + Double.toString(longitude);

        String url = Constants.GOOGLE_MAP_CONNECTION_URL + latLng;

        HttpHelper httpHelper = new HttpHelper(activity);

        httpHelper.getJson(url, new HttpCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("HMB", result);
                countryName = getCountry(result);
                Log.d("HMB", countryName);
                listener.onCountryDetected(countryName);
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    public String getCountry(String result) {
        String[] placeNames = result.split(",");
        countryName = placeNames[placeNames.length - 1].trim();
        return countryName;
    }


}
