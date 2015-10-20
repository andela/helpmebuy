package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.FirebasePasswordReset;
import com.andela.helpmebuy.authentication.PasswordReset;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Launcher;

public class ForgotPasswordActivity extends AppCompatActivity  {

    private LinearLayout parentLayout;

    private EditText emailEditText;

    private Button sendResetEmailButton;

    private PasswordReset passwordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        passwordReset = new FirebasePasswordReset();
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

        if (email.isEmpty()) {
            emailEditText.setError(getResources().getString(R.string.email_missing));
        } else {
            sendResetEmailButton.setEnabled(false);

            passwordReset.sendTemporaryPassword(email, new AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    Launcher.launchActivity(ForgotPasswordActivity.this, SigninActivity.class);
                    finish();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(String errorMessage) {
                    Snackbar.make(parentLayout, "Failed: " + errorMessage, Snackbar.LENGTH_LONG).show();

                    sendResetEmailButton.setEnabled(true);
                }

                @Override
                public void onFailure(Exception e) {
                    Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
