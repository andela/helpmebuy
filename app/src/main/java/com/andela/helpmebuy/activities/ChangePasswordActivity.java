package com.andela.helpmebuy.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.FirebasePasswordReset;
import com.andela.helpmebuy.authentication.PasswordReset;
import com.andela.helpmebuy.fragments.ConfirmPasswordFragment;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.ActionBar;
import com.andela.helpmebuy.utilities.Launcher;
import com.andela.helpmebuy.utilities.SoftKeyboard;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button changePasswordButton;

    private EditText emailText;

    private EditText oldPassword;

    private EditText newPassword;

    private LinearLayout parentLayout;

    private PasswordReset firebasePasswordReset;

    private Context context = ChangePasswordActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar.enableHomeButton(this);
        firebasePasswordReset = new FirebasePasswordReset(this);
        parentLayout = (LinearLayout) findViewById(R.id.linear_layout);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        emailText = (EditText) findViewById(R.id.emailtext);
        changePasswordButton = (Button) findViewById(R.id.change_password);

    }

    public void changePassword(View view) {
        SoftKeyboard.hide(this);
        String email = emailText.getText().toString().trim();
        String oldPassword = this.oldPassword.getText().toString();
        String newPassword = this.newPassword.getText().toString();
        if (email.isEmpty()) {
            emailText.setError(getResources().getString(R.string.email_missing));
        } else if (oldPassword.isEmpty()) {
            this.oldPassword.setError(getResources().getString(R.string.password_missing));
        } else if (newPassword.isEmpty()) {
            this.newPassword.setError(getResources().getString(R.string.password_missing));
        } else {
            changePasswordButton.setEnabled(false);
            resetPassword(email, oldPassword, newPassword);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Launcher.launchActivity(context, UserSettingsActivity.class);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetPassword(String email, String oldPassword, String newPassword) {
        firebasePasswordReset.changePassword(email, oldPassword, newPassword, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Snackbar.make(parentLayout, "Password changed successfully", Snackbar.LENGTH_LONG).show();

                Bundle bundle = getIntent().getBundleExtra(Launcher.TRANSPORT);
                if (bundle != null) {
                    if (bundle.getBoolean(ConfirmPasswordFragment.CONFIRM_PASSWORD)) {
                        Launcher.launchActivity(context, UserSettingsActivity.class);
                    }
                } else {
                    Launcher.launchActivity(context, MainActivity.class);
                }

                finish();

            }

            @Override
            public void onCancel() {
                changePasswordButton.setEnabled(true);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();
                changePasswordButton.setEnabled(true);
            }

            @Override
            public void onFailure(Exception e) {
                changePasswordButton.setEnabled(true);
                Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
