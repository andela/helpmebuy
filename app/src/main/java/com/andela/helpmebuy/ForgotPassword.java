package com.andela.helpmebuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ForgotPassword extends AppCompatActivity {

    public final String FIREBASE_URL = "https://hmbuy.firebaseio.com";

    private Firebase firebase;

    private LinearLayout parentLayout;

    private EditText emailEditText;

    private Button sendResetEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        setContentView(R.layout.activity_forgot_password);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendResetEmailButton = (Button) findViewById(R.id.send_reset_email_button);
        emailEditText = (EditText) findViewById(R.id.send_reset_email_text);
        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendTemporaryPassword(View view) {
        final String email = emailEditText.getText().toString().trim();

        if (email.equals("")) {
            emailEditText.setError(getResources().getString(R.string.email_missing));
        } else {
            sendResetEmailButton.setEnabled(false);
            final Intent intent = new Intent(this, SigninActivity.class);

            firebase.resetPassword(emailEditText.getText().toString(), new Firebase.ResultHandler() {

                @Override
                public void onSuccess() {
                    Snackbar.make(parentLayout, "Success", Snackbar.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Snackbar.make(parentLayout, "Failed", Snackbar.LENGTH_LONG).show();
                    sendResetEmailButton.setEnabled(true);
                }
            });
        }
    }

}
