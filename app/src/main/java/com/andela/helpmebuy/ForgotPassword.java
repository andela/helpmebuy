package com.andela.helpmebuy;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.client.Firebase;

public class ForgotPassword extends AppCompatActivity {

    public final String FIREBASE_URL = "https://hmbuy.firebaseio.com";

    private Firebase firebase;

    private RelativeLayout parentLayout;

    private EditText emailEditText;

    private Button sendResetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);
        setContentView(R.layout.activity_forgot_password);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendResetEmail = (Button) findViewById(R.id.send_reset_email_button);
        emailEditText = (EditText) findViewById(R.id.send_reset_email_text);
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

}
