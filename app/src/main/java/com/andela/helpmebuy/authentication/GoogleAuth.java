package com.andela.helpmebuy.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.andela.helpmebuy.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GoogleAuth implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    public static final int RC_SIGN_IN = 0;

    private boolean mIsResolving = false;

    private boolean mShouldResolve = false;

    public static final String TAG = "SigninActivity";

    private GoogleApiClient mGoogleApiClient;

    private AuthCallback callback;

    private Activity activity;

    public GoogleAuth(Activity activity, GoogleApiClient mGoogleApiClient, AuthCallback callback) {
        this.activity = activity;

        this.mGoogleApiClient = mGoogleApiClient;

        this.callback = callback;

        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.registerConnectionFailedListener(this);
    }

    public  void signIn() {
        mShouldResolve = true;

        mGoogleApiClient.connect();
    }

    public void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);

            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(activity, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                callback.onError(connectionResult.toString());
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            User user = new User();

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            user.setId(currentPerson.getId());
            user.setFullName(currentPerson.getDisplayName());
            user.setProfilePictureUrl(currentPerson.getImage().getUrl());
            user.setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));

            callback.onSuccess(user);
        }
    }

    @Override
    public void onConnectionSuspended(int status){

    }

    public void setMIsResolving(boolean mIsResolving) {
        this.mIsResolving = mIsResolving;
    }

    public void setMShouldResolve(boolean mShouldResolve) {
        this.mShouldResolve = mShouldResolve;
    }
}
