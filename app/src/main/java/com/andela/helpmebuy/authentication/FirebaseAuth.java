package com.andela.helpmebuy.authentication;

import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class FirebaseAuth implements EmailPasswordAuth {
    public static final String TEMPORARY_PASSWORD = "Temporary Password";

    private Firebase firebase;

    public FirebaseAuth() {
        firebase = new Firebase(Constants.FIREBASE_URL);
    }

    @Override
    public void signIn(String email, String password, final AuthCallback callback) {
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                if ((boolean) authData.getProviderData().get("isTemporaryPassword")) {
                    callback.onError(TEMPORARY_PASSWORD);
                } else {
                    FirebaseCollection<User> users = new FirebaseCollection<>(Constants.USERS, User.class);

                    users.get(authData.getUid(), new DataCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            callback.onSuccess(user);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onError(errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                callback.onError(firebaseError.getMessage());
            }
        });

    }

    @Override
    public void signUp(final String email, String password, final AuthCallback callback) {
        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
             public void onSuccess(Map<String, Object> result) {
                String id = result.get("uid").toString();

                User user = new User(id);
                user.setEmail(email);

                callback.onSuccess(user);
             }

            @Override
            public void onError(FirebaseError firebaseError) {
                callback.onError(firebaseError.getMessage());
            }
        });

    }

    @Override
    public void signOut() {
    }
}