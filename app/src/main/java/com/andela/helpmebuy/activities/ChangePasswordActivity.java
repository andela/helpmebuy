package com.andela.helpmebuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.FirebasePasswordReset;
import com.andela.helpmebuy.authentication.PasswordReset;
import com.andela.helpmebuy.models.User;


import com.andela.helpmebuy.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button changePasswordButton;

    private EditText emailText;

    private EditText oldPassword;

    private EditText newPassword;

    private LinearLayout parentLayout;

    private PasswordReset firebasePasswordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebasePasswordReset = new FirebasePasswordReset();
        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        emailText = (EditText) findViewById(R.id.emailtext);
        changePasswordButton = (Button) findViewById(R.id.change_password);
    }

    public void changePassword(View view) {
        String email = emailText.getText().toString().trim();
        String oldPassword = this.oldPassword.getText().toString();
        String newPassword = this.newPassword.getText().toString();

        if (email.isEmpty())
            emailText.setError(getResources().getString(R.string.email_missing));
        else if (oldPassword.isEmpty()) {
            this.oldPassword.setError(getResources().getString(R.string.password_missing));
        }
        else if (newPassword.isEmpty()){
            this.newPassword.setError(getResources().getString(R.string.password_missing));
        }
        else {
            changePasswordButton.setEnabled(false);

            final Intent intent = new Intent(this, HomeActivity.class);

            firebasePasswordReset.changePassword(email, oldPassword, newPassword, new AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    Snackbar.make(parentLayout, "Password changed successfully", Snackbar.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(String errorMessage) {
                   Snackbar.make(parentLayout, errorMessage.toString(), Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
