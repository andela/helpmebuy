package com.andela.helpmebuy;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;


public class HelpMeBuyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }
}
