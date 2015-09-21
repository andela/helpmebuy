package com.andela.helpmebuy;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.facebook.FacebookSdk;

import java.util.Arrays;

public class SigninActivity extends AppCompatActivity {

    private final String firebaseUrl = "http://hmbuy.firebaseio.com";

    private EditText emailText;

    private EditText passwordText;

    private Button signInButton;

    private Firebase firebase;

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;

    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_signin);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(firebaseUrl);

        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signInButton = (Button) findViewById(R.id.signin_button);
        loginButton = (LoginButton) findViewById(R.id.facebook_button);
        callbackManager = CallbackManager.Factory.create();
        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
/**
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Snackbar.make(parentLayout, R.string.facebook_success, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Snackbar.make(parentLayout, R.string.facebook_cancel, Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException e) {
                Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
*/
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                onFacebookAccessTokenChange(currentAccessToken);
            }
        };
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signIn(View view){
        firebase = new Firebase(firebaseUrl);

        final String email = emailText.getText().toString().trim();

        String password = passwordText.getText().toString();

        if (email.equals(""))
            emailText.setError(getResources().getString(R.string.email_missing));

        else if (password.equals(""))
            passwordText.setError(getResources().getString(R.string.password_missing));

        else {

            signInButton.setText((R.string.signing_in));
            signInButton.setEnabled(false);

            firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {

                @Override
                public void onAuthenticated(AuthData authData) {

                    Snackbar.make(parentLayout, R.string.success_login, Snackbar.LENGTH_LONG).show();
                    signInButton.setText(R.string.signin);
                    signInButton.setEnabled(true);

                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                    Snackbar.make(parentLayout, firebaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                    signInButton.setText(R.string.signin);
                    signInButton.setEnabled(true);

                }
            });
        }
    }

    public void facebookSignIn(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            firebase.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // The Facebook user is now authenticated with your Firebase app
                    Snackbar.make(parentLayout, R.string.facebook_success, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                    Snackbar.make(parentLayout,firebaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
        /* Logged out of Facebook so do a logout from the Firebase app */
            firebase.unauth();
        }
    }
}
