package com.andela.helpmebuy;

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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.facebook.FacebookSdk;

public class SigninActivity extends AppCompatActivity {

    private final String firebaseUrl = "http://hmbuy.firebaseio.com";

    private EditText emailText;

    private EditText passwordText;

    private Button signInButton;

    private Firebase firebase;

    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(firebaseUrl);

        FacebookSdk.sdkInitialize(getApplicationContext());

        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signInButton = (Button) findViewById(R.id.signin_button);
        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);
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
}
