package com.andela.helpmebuy.authentication;

import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleAuth implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onClick(View v) {
        
    }


}
