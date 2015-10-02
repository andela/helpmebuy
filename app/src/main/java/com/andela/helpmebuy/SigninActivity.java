package com.andela.helpmebuy;

import android.app.PendingIntent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.helpmebuy.models.User;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class SigninActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,ResultCallback<People.LoadPeopleResult> {

    private final String TAG = "SigninAcivity";

    private final String firebaseUrl = "http://hmbuy.firebaseio.com";

    private EditText emailText;

    private EditText passwordText;

    private Button signInButton;

    private Firebase firebase;

    private LoginButton loginButton;

    private SignInButton googleSignInButton;

    private CallbackManager callbackManager;

    private LinearLayout parentLayout;

    private static final int STATE_DEFAULT = 0;

    private static final int STATE_SIGN_IN = 1;

    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    private GoogleApiClient mGoogleApiClient;

    private int mSignInProgress;

    private PendingIntent mSignInIntent;

    private int mSignInError;

    private Button googlePlusLogout;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private ArrayList<String> mCirclesList;

    private ArrayAdapter<String> mCirclesAdapter;

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
        firebase = new Firebase(firebaseUrl);

        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signInButton = (Button) findViewById(R.id.signin_button);
        loginButton = (LoginButton) findViewById(R.id.facebook_button);
        googleSignInButton = (SignInButton) findViewById(R.id.googleplus_button);
        googleSignInButton.setOnClickListener(this);
        googlePlusLogout = (Button) findViewById(R.id.googlePlusSignOut);
        callbackManager = CallbackManager.Factory.create();
        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mCirclesList = new ArrayList<String>();
        mCirclesAdapter = new ArrayAdapter<String>(
                this, R.layout.circle_member, mCirclesList);

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

        //googleplus
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        mGoogleApiClient = buildGoogleApiClient();
    }


    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);

        return builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            mCirclesList.clear();
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    mCirclesList.add(personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }

            mCirclesAdapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }

                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        Log.d(TAG,"onConnectionFailed:"+result);

        //if(!mIsResolving && mShouldResolve){
        if(mSignInProgress == STATE_SIGN_IN){
            if(result.hasResolution()){
                try{
                    result.startResolutionForResult(this,RC_SIGN_IN);
                    //mIsResolving = true;
                    mSignInProgress = STATE_IN_PROGRESS;
                }
                catch (IntentSender.SendIntentException e){
                    Log.e(TAG,"Connection can not be established",e);
                   // mIsResolving = false;
                    mSignInProgress = STATE_DEFAULT;
                    mGoogleApiClient.connect();
                }
            }
            else{
                Snackbar.make(parentLayout, R.string.googleplus_error, Snackbar.LENGTH_LONG).show();
            }

        }

    }

    /* Starts an appropriate intent or dialog for user interaction to resolve
 * the current error preventing the user from being signed in.  This could
 * be a dialog allowing the user to select an account, an activity allowing
 * the user to consent to the permissions being requested by your app, a
 * setting to enable device networking, etc.
 */
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle){
        Log.i(TAG, "onConnected");

        // Update the user interface to reflect that the user is signed in.
        googleSignInButton.setEnabled(false);
        // Retrieve some profile information to personalize our app for the user.
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(this);

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;

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

    public void signInWithFacebook(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onClick(View v) {
        // We only process button clicks when GoogleApiClient is not transitioning
        // between connected and not connected.

        if (v.getId() == R.id.googleplus_button && !mGoogleApiClient.isConnecting()) {
            Snackbar.make(parentLayout, R.string.signing_in, Snackbar.LENGTH_LONG).show();
            mSignInProgress = STATE_SIGN_IN;

            mGoogleApiClient.connect();

            googlePlusLogout.setVisibility(View.VISIBLE);
        }

    }

    public void onClickSignOut(View view) {
        if(view.getId() == R.id.googlePlusSignOut) {
            googleSignInButton.setVisibility((View.INVISIBLE));
            signOutGooglePlus();
        }
    }

    public void signOutGooglePlus(){
        if(mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            Snackbar.make(parentLayout, "Signout successful", Snackbar.LENGTH_LONG).show();
        }
        //Snackbar.make(parentLayout, R.string.googleplus_signout, Snackbar.LENGTH_LONG).show();
    }
}
