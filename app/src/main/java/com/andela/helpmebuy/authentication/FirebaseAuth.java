package com.andela.helpmebuy.authentication;

import android.content.Context;

import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.FireBaseErrorHandler;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class FirebaseAuth implements EmailPasswordAuth {
    public static final String TEMPORARY_PASSWORD = "Temporary Password";

    private Context context;
    private Firebase firebase;

    public FirebaseAuth(Context context) {
        this.context = context;
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
                callback.onError(FireBaseErrorHandler.getErrorMessage(firebaseError, context));
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
                callback.onError(FireBaseErrorHandler.getErrorMessage(firebaseError, context));
            }
        });

    }

    @Override
    public void signOut() {
    }
}
