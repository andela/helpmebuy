package com.andela.helpmebuy.activities;

import android.annotation.SuppressLint;
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

import com.andela.helpmebuy.locations.Places;
import com.andela.helpmebuy.R;
import com.andela.helpmebuy.authentication.EmailPasswordAuth;
import com.andela.helpmebuy.authentication.FirebaseAuth;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.FacebookAuth;
import com.andela.helpmebuy.utilities.CurrentUser;
import com.andela.helpmebuy.utilities.Launcher;
import com.andela.helpmebuy.utilities.SoftKeyboard;
import com.google.android.gms.common.SignInButton;
import com.andela.helpmebuy.authentication.GoogleAuth;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.AlertDialogHelper;
import com.andela.helpmebuy.utilities.Constants;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;

    private EditText passwordText;

    private Button signInButton;

    private FirebaseCollection<User> users;

    private LinearLayout parentLayout;

    private FacebookAuth facebookAuth;

    private GoogleAuth googleAuth;

    private SignInButton googleSignInButton;

    private Button googleSignOutButton;

    private EmailPasswordAuth emailPasswordAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (CurrentUser.get(this) != null) {
            Launcher.launchActivity(this, HomeActivity.class);
            finish();
        }

        setContentView(R.layout.activity_signin);

        hideActionBar();

        loadComponents();

        initializeUserscollection();

        initializeFacebookAuth();

        initializeGoogleAuth();

        initializeEmailPasswordAuth();
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void loadComponents() {
        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);
        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signInButton = (Button) findViewById(R.id.signin_button);
    }

    private void initializeUserscollection()  {
        users = new FirebaseCollection<>(Constants.USERS, User.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleAuth.RC_SIGN_IN) {

            if(resultCode != RESULT_OK) {
                googleAuth.setShouldResolve(false);
            }

            googleAuth.setResolving(false);
            googleAuth.connect();
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

    public void signIn(View view) {
        SoftKeyboard.hide(SigninActivity.this);

        final String email = emailText.getText().toString().trim();

        String password = passwordText.getText().toString();

        if (email.isEmpty())
            emailText.setError(getResources().getString(R.string.email_missing));

        else if (password.isEmpty())
            passwordText.setError(getResources().getString(R.string.password_missing));
        else {
            signInButton.setText((R.string.signing_in));
            signInButton.setEnabled(false);

            signIn(email, password);
        }
    }

    public void logInWithFacebook(View view) {
        facebookAuth.logIn();
    }

    private void signInWithGooglePlus() {
        googleAuth.signIn();
        AlertDialogHelper.processDialog(SigninActivity.this).show();
    }

    private void signOutWithGooglePlus() {
        googleAuth.signOut();

        googleSignOutButton.setVisibility(View.INVISIBLE);

        googleSignInButton.setEnabled(true);
        googleSignInButton.setVisibility((View.VISIBLE));

        Snackbar.make(parentLayout, R.string.google_sign_out_successful, Snackbar.LENGTH_LONG).show();
    }

    private void signIn(String email, String password) {
        emailPasswordAuth.signIn(email, password, new AuthCallback() {

            public void onSuccess(User user) {
                CurrentUser.save(user, SigninActivity.this);

                Launcher.launchActivity(SigninActivity.this, HomeActivity.class);
                finish();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(String errorMessage) {
                if (errorMessage.equals(FirebaseAuth.TEMPORARY_PASSWORD)) {
                    AlertDialogHelper.displayWarning(SigninActivity.this).show();

                    signInButton.setEnabled(true);
                    signInButton.setText((R.string.signin));

                } else {
                    Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();

                    signInButton.setEnabled(true);
                    signInButton.setText((R.string.signin));
                }
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();

                signInButton.setEnabled(true);
                signInButton.setText((R.string.signin));
            }
        });
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
        Places places= new Places();
        System.out.println(places.getCountriesMap());
//        Launcher.launchActivity(this, ForgotPasswordActivity.class);
//        finish();
    }

    private void initializeFacebookAuth() {
        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        facebookAuth = new FacebookAuth(this, facebookLoginButton, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                users.save(user, null);

                CurrentUser.save(user, SigninActivity.this);

                Launcher.launchActivity(SigninActivity.this, HomeActivity.class);
                finish();
            }

            @Override
            public void onCancel() {
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

        googleAuth = new GoogleAuth(this, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                users.save(user, null);

                CurrentUser.save(user, SigninActivity.this);

                Launcher.launchActivity(SigninActivity.this, HomeActivity.class);
                finish();
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
            }
        });
    }

    private void initializeEmailPasswordAuth() {
        emailPasswordAuth = new FirebaseAuth(this);
    }
}
