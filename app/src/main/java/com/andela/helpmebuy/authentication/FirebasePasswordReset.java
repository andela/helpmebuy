package com.andela.helpmebuy.authentication;

import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class FirebasePasswordReset implements PasswordReset {
    private Firebase firebase;

    public FirebasePasswordReset() {
        firebase = new Firebase(Constants.FIREBASE_URL);
    }
    @Override
    public void changePassword(String email, String oldPassword, String newPassword, final AuthCallback authCallback) {
        firebase.changePassword(email, oldPassword, newPassword, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                User user = new User();
                authCallback.onSuccess(user);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                authCallback.onError(firebaseError.getMessage());
            }

            });

    }

    @Override
    public void sendTemporaryPassword(String email, final AuthCallback authCallback) {
        firebase.resetPassword(email, new Firebase.ResultHandler() {
        User user = new User();
            @Override
            public void onSuccess() {
                authCallback.onSuccess(user);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                authCallback.onError(firebaseError.getMessage());

            }
        });

    }
}
