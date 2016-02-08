package com.andela.helpmebuy.authentication;

import android.content.Context;

import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by GRACE on 1/19/2016.
 */
public class FirebaseChangeEmail implements ChangeEmailAuth {

    private Context context;
    private Firebase firebase;

    public FirebaseChangeEmail(Context context) {
        this.context = context;
        this.firebase = new Firebase(Constants.FIREBASE_URL);
    }

    @Override
    public void changeEmail(String oldEmail, final String newEmail, String password, final AuthCallback callback) {
        this.firebase.changeEmail(oldEmail, password, newEmail, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                User user = CurrentUserManager.get(context);
                user.setEmail(newEmail);
                CurrentUserManager.save(user, context);
                callback.onSuccess(user);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                callback.onError(firebaseError.getMessage());
            }
        });
    }
}
