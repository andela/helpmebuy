package com.andela.helpmebuy.authentication;


import android.app.Activity;
import android.os.Bundle;

import com.andela.helpmebuy.models.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookAuth {

    private Activity activity;

    private LoginButton loginButton;

    private AuthCallback callback;

    public FacebookAuth(Activity activity, LoginButton button, AuthCallback callback) {
        this.activity = activity;
        this.loginButton = button;
        this.callback = callback;

        initialize();
    }

    public void logIn() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    private void initialize() {
        onLoginButtonClicked(callback);
    }


    private void onLoginButtonClicked(final AuthCallback callback) {
        CallbackManager manager = CallbackManager.Factory.create();

        loginButton.registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            User user = new User(object.getString("id"));
                            user.setFullName(object.getString("name"));
                            user.setEmail(object.getString("email"));

                            if (!object.isNull("picture")) {
                                JSONObject picture = (JSONObject) object.get("picture");

                                if (picture != null) {
                                    JSONObject data = (JSONObject) picture.get("data");

                                    if (data.length() != 0) {
                                        user.setProfilePictureUrl(data.getString("url"));
                                    }
                                }
                            }

                            callback.onSuccess(user);

                        } catch (JSONException e) {
                            callback.onFailure(e);
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, picture");

                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }

            @Override
            public void onError(FacebookException e) {
                callback.onError(e.getMessage());
            }
        });
    }
}
