package com.andela.helpmebuy.authentication;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;

import com.andela.helpmebuy.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GoogleAuth implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int RC_SIGN_IN = 0;

    private boolean resolving;

    private boolean shouldResolve;

    private GoogleApiClient googleApiClient;

    private AuthCallback callback;

    private Activity activity;

    public GoogleAuth(Activity activity, AuthCallback callback) {
        this.activity = activity;

        this.googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.PLUS_ME))
                .build();

        this.googleApiClient.registerConnectionCallbacks(this);
        this.googleApiClient.registerConnectionFailedListener(this);

        this.callback = callback;

        this.resolving = false;
        this.shouldResolve = false;
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    public void signIn() {
        shouldResolve = true;

        googleApiClient.connect();
    }

    public void signOut() {
        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);

            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!resolving && shouldResolve) {

            if (connectionResult.hasResolution()) {

                try {
                    connectionResult.startResolutionForResult(activity, RC_SIGN_IN);
                    resolving = true;

                } catch (IntentSender.SendIntentException e) {
                    resolving = false;
                    googleApiClient.connect();
                }

            } else {
                callback.onError(connectionResult.toString());
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        shouldResolve = false;

        if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
            User user = new User();

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);

            user.setId(currentPerson.getId());
            user.setFullName(currentPerson.getDisplayName());
            user.setProfilePictureUrl(currentPerson.getImage().getUrl());
            user.setEmail(Plus.AccountApi.getAccountName(googleApiClient));

            callback.onSuccess(user);
        }
    }

    @Override
    public void onConnectionSuspended(int status) {
    }

    public void setResolving(boolean resolving) {
        this.resolving = resolving;
    }

    public void setShouldResolve(boolean shouldResolve) {
        this.shouldResolve = shouldResolve;
    }
}
