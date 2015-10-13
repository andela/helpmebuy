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
import com.facebook.login.LoginManager;
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

    private SignInButton googleSignInButton;

    private Button googlePlusSignOut;

    private LinearLayout parentLayout;

    private GoogleApiClient mGoogleApiClient;

    private GoogleAuth googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        firebase = new Firebase(Constants.FIREBASE_URL);

        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signInButton = (Button) findViewById(R.id.signin_button);

        googleSignInButton = (SignInButton) findViewById(R.id.googleplus_button);
        googleSignInButton.setOnClickListener(this);

        googlePlusSignOut = (Button) findViewById(R.id.googlePlusSignOut);

        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);

        users = new FirebaseUsers();

        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        FacebookAuth.onLoginButtonClicked(loginButton, new AuthCallback() {
           @Override
            public void onSuccess(User user) {
                users.save(user, null);
            }

            @Override
            public void onCancel() {
                Snackbar.make(parentLayout, R.string.facebook_cancel, Snackbar.LENGTH_LONG).show();
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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        googleClient = new GoogleAuth(this, mGoogleApiClient, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                users.save(user, null);

                googlePlusSignOut.setVisibility(View.VISIBLE);
                googleSignInButton.setVisibility((View.GONE));

                googleSignInButton.setEnabled(false);
            }

            @Override
            public void onError(String string) {}

            @Override
            public void onFailure(Exception e) {}

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleAuth.RC_SIGN_IN) {

            if(resultCode != RESULT_OK) {
                googleClient.setShouldResolve(false);
            }

            googleClient.setResolving(false);
            mGoogleApiClient.connect();
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

    public void signInWithFacebook(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.googleplus_button) {
            googleClient.signIn();

            Snackbar.make(parentLayout, R.string.signing_in, Snackbar.LENGTH_LONG).show();
        }

        if(v.getId() == R.id.googlePlusSignOut) {
            googleClient.signOut();

            googlePlusSignOut.setVisibility(View.INVISIBLE);
            googleSignInButton.setVisibility((View.VISIBLE));

            googleSignInButton.setEnabled(true);

            Snackbar.make(parentLayout, "Signout successful", Snackbar.LENGTH_LONG).show();

        }
    }

    @SuppressLint("NewApi")
    public void resetPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
