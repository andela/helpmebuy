package com.andela.helpmebuy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.AlertDialogHelper;
import com.andela.helpmebuy.utilities.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SigninActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private final String TAG = "SigninActivity";

    private EditText emailText;

    private EditText passwordText;

    private Button signInButton;

    private Firebase firebase;

    private LoginButton loginButton;

    private SignInButton googleSignInButton;

    private Button googlePlusSignOut;

    private CallbackManager callbackManager;

    private LinearLayout parentLayout;

    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIsResolving = false;

    private boolean mShouldResolve = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Firebase.setAndroidContext(this);
        firebase = new Firebase(Constants.FIREBASE_URL);

        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signInButton = (Button) findViewById(R.id.signin_button);
        loginButton = (LoginButton) findViewById(R.id.facebook_button);

        googleSignInButton = (SignInButton) findViewById(R.id.googleplus_button);
        googleSignInButton.setOnClickListener(this);

        googlePlusSignOut = (Button) findViewById(R.id.googlePlusSignOut);

        callbackManager = CallbackManager.Factory.create();

        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Snackbar.make(parentLayout, R.string.facebook_success, Snackbar.LENGTH_LONG).show();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{

                            User user = new User(object.getString("id"));
                            user.setFullName(object.getString("name"));
                            user.setEmail(object.getString("email"));

                            if (!object.isNull("picture")) {

                                JSONObject picture = (JSONObject) object.get("picture");

                                if (picture !=null) {

                                    JSONObject data = (JSONObject) picture.get("data");

                                    if (data.length() != 0) {

                                        user.setProfilePictureUrl(data.getString("url"));
                                    }

                                    firebase.child("users").child(user.getId()).setValue(user);
                                }
                            }

                        } catch(JSONException e){
                            Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
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
                Snackbar.make(parentLayout, R.string.facebook_cancel, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + resultCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {

            if(resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){

        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if(!mIsResolving && mShouldResolve){

            if(connectionResult.hasResolution()){
                try{
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);

                    mIsResolving = true;

                } catch (IntentSender.SendIntentException e){

                      Log.e(TAG,"Connection can not be established", e);

                      mIsResolving = false;

                      mGoogleApiClient.connect();
                }
            } else{

                Snackbar.make(parentLayout, R.string.googleplus_error, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle){
        Log.i(TAG, "onConnected" + bundle);
        mShouldResolve = false;

        googlePlusSignOut.setVisibility(View.VISIBLE);
        googleSignInButton.setVisibility((View.GONE));

        googleSignInButton.setEnabled(false);

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

            User user = new User();

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            user.setId(currentPerson.getId());
            user.setFullName(currentPerson.getDisplayName());
            user.setProfilePictureUrl(currentPerson.getImage().getUrl());
            user.setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));

            saveUser(user);
        }
    }

    @Override
    public void onConnectionSuspended(int status){

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
//        firebase = new Firebase(Constants.FIREBASE_URL);

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
                    if ((boolean)authData.getProviderData().get("isTemporaryPassword")) {
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
            onSignInClicked();
        }
    }

    public void onSignInClicked() {

        mShouldResolve = true;

        mGoogleApiClient.connect();

        Snackbar.make(parentLayout, R.string.signing_in, Snackbar.LENGTH_LONG).show();
    }

    public void onClickSignOut(View view) {

        if(view.getId() == R.id.googlePlusSignOut) {

            signOutGooglePlus();

            googlePlusSignOut.setVisibility(View.INVISIBLE);

            googleSignInButton.setVisibility((View.VISIBLE));

            googleSignInButton.setEnabled(true);
        }
    }

    public void signOutGooglePlus(){

        if(mGoogleApiClient.isConnected()) {

            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);

            mGoogleApiClient.disconnect();

            Snackbar.make(parentLayout, "Signout successful", Snackbar.LENGTH_LONG).show();
        }
    }

    public void saveUser(User user) {

        firebase.child("users").child(user.getId()).setValue(user);
    }
    @SuppressLint("NewApi")
    public void resetPassword(View view) {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }
}
