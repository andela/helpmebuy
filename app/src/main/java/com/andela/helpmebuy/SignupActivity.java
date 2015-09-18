package com.andela.helpmebuy;

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
import android.widget.RelativeLayout;

import com.andela.helpmebuy.models.User;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    public final String TAG = "SignupActivity";

    public final String FIREBASE_URL = "https://hmbuy.firebaseio.com";

    private Firebase firebase;

    private RelativeLayout parentLayout;

    private EditText fullNameEditText;

    private EditText emailEditText;

    private EditText passwordEditText;

    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        parentLayout = (RelativeLayout) findViewById(R.id.background);
        fullNameEditText = (EditText) findViewById(R.id.fullName_text);
        emailEditText = (EditText) findViewById(R.id.email_text);
        passwordEditText = (EditText) findViewById(R.id.password_text);
        signupButton = (Button) findViewById(R.id.signup_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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

    public void signUp(View view) {
        final String fullName = fullNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (fullName.equals("")) {
            fullNameEditText.setError(getResources().getString(R.string.fullname_missing));

        } else if (email.equals("")) {
            emailEditText.setError(getResources().getString(R.string.email_missing));

        } else if (password.equals("")) {
            passwordEditText.setError(getResources().getString(R.string.password_missing));

        } else {
            signupButton.setText(R.string.signing_up);
            signupButton.setEnabled(false);

            firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {

                @Override
                public void onSuccess(Map<String, Object> result) {
                    String id = result.get("uid").toString();

                    Log.i(TAG, "Created user ID = " + id);

                    User user = new User(id);
                    user.setFullName(fullName);
                    user.setEmail(email);

                    saveUser(user);

                    signupButton.setText(R.string.signup);
                    signupButton.setEnabled(true);

                    Snackbar.make(parentLayout, "Created user ID = " + id, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Log.d(TAG, firebaseError.toString());

                    signupButton.setText(R.string.signup);
                    signupButton.setEnabled(true);

                    String message = firebaseError.getMessage();
                    if (message.contains("email")) {
                        emailEditText.setError(message);
                    } else {
                        Snackbar.make(parentLayout, firebaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    public void signIn(View view) {

    }
    public void saveUser(User user) {
        firebase.child("users").child(user.getId()).setValue(user);
    }

}
