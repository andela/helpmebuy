package com.andela.helpmebuy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andela.helpmebuy.dal.Users;
import com.andela.helpmebuy.dal.firebase.FirebaseUsers;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.FacebookAuth;
import com.firebase.client.Firebase;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.andela.helpmebuy.authentication.GoogleAuth;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.AlertDialogHelper;
import com.andela.helpmebuy.utilities.Constants;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.util.Arrays;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;

    private EditText passwordText;

    private Button signInButton;

    private Users users;

    private Firebase firebase;

    private LinearLayout parentLayout;

    private FacebookAuth facebookAuth;

    private GoogleApiClient googleApiClient;

    private GoogleAuth googleAuth;

    private SignInButton googleSignInButton;

    private Button googleSignOutButton;

    public static String UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        firebase = new Firebase(Constants.FIREBASE_URL);

        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);
        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signInButton = (Button) findViewById(R.id.signin_button);

        users = new FirebaseUsers();

        initializeFacebookAuth();

        initializeGoogleAuth();
    }

    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        googleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleAuth.RC_SIGN_IN) {

            if(resultCode != RESULT_OK) {
                googleAuth.setShouldResolve(false);
            }

            googleAuth.setResolving(false);
            googleApiClient.connect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signIn(View view){
        final String email = emailText.getText().toString().trim();


        String password = passwordText.getText().toString();

        if (email.equals(""))
            emailText.setError(getResources().getString(R.string.email_missing));

        else if (password.equals(""))
            passwordText.setError(getResources().getString(R.string.password_missing));
        else {

            signInButton.setText((R.string.signing_in));
            signInButton.setEnabled(false);

            final Activity that = this;

            firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    UserEmail = email;

                    if ((boolean) authData.getProviderData().get("isTemporaryPassword")) {
                        AlertDialogHelper.createDialog(that).show();

                        signInButton.setEnabled(true);

                    } else {
                        Snackbar.make(parentLayout, R.string.success_login, Snackbar.LENGTH_LONG).show();

                        signInButton.setText(R.string.signin);
                        signInButton.setEnabled(true);
                    }
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

    public void logInWithFacebook(View view) {
        facebookAuth.logIn();
    }

    public void signInWithGooglePlus() {
        googleAuth.signIn();
    }

    public void signOutWithGooglePlus() {
        googleAuth.signOut();

        googleSignOutButton.setVisibility(View.INVISIBLE);


        googleSignInButton.setEnabled(true);
        googleSignInButton.setVisibility((View.VISIBLE));

        Snackbar.make(parentLayout, R.string.google_sign_out_successful, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_signin_button:
                signInWithGooglePlus();
                break;

            case R.id.google_signout_button:
                signOutWithGooglePlus();
                break;
        }
    }

    @SuppressLint("NewApi")
    public void resetPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void initializeFacebookAuth() {
        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        facebookAuth = new FacebookAuth(this, facebookLoginButton, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                users.save(user, null);
            }

            @Override
            public void onCancel() {
                Snackbar.make(parentLayout, R.string.facebook_login_cancelled, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void initializeGoogleAuth() {
        googleSignInButton = (SignInButton) findViewById(R.id.google_signin_button);
        googleSignInButton.setOnClickListener(this);

        googleSignOutButton = (Button) findViewById(R.id.google_signout_button);
        googleSignOutButton.setOnClickListener(this);

        googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Plus.API)
            .addScope(new Scope(Scopes.PROFILE))
            .addScope(new Scope(Scopes.EMAIL))
            .build();

        googleAuth = new GoogleAuth(this, googleApiClient, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                users.save(user, null);

                googleSignOutButton.setVisibility(View.VISIBLE);
                googleSignInButton.setVisibility((View.GONE));

                googleSignInButton.setEnabled(false);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Snackbar.make(parentLayout, R.string.google_signin_cancel, Snackbar.LENGTH_LONG).show();
            }
        });

    }
}
