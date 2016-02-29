package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.FirebaseChangeEmail;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.User;

public class ChangeEmailActivity extends AppCompatActivity {
    private TextView oldEmailText;
    private TextView newEmailText;
    private TextView passwordText;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        initializeComponents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeComponents() {
        oldEmailText = (TextView) findViewById(R.id.old_email_text);
        newEmailText = (TextView) findViewById(R.id.new_email_text);
        passwordText = (TextView) findViewById(R.id.password_text);
        updateButton = (Button) findViewById(R.id.update_email_button);
    }


    public void updateEmail(final View view) {
        updateButton.setEnabled(false);
        changeEmail(oldEmailText.getText().toString(), passwordText.getText().toString(), newEmailText.getText().toString());
    }

    private void changeEmail(String oldEmail, String password, String newEmail) {
        FirebaseChangeEmail firebaseChangeEmail = new FirebaseChangeEmail(ChangeEmailActivity.this);
        firebaseChangeEmail.changeEmail(oldEmail, newEmail, password, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                FirebaseCollection<User> collection = new FirebaseCollection<>(Constants.USERS, User.class);
                collection.save(user, new DataCallback<User>() {
                    @Override
                    public void onSuccess(User data) {
                        updateButton.setEnabled(true);
                        ChangeEmailActivity.this.finish();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        updateButton.setEnabled(true);
                    }
                });
            }

            @Override
            public void onCancel() {
                updateButton.setEnabled(true);
            }

            @Override
            public void onError(String errorMessage) {
                updateButton.setEnabled(true);
            }

            @Override
            public void onFailure(Exception e) {
                updateButton.setEnabled(true);
            }
        });
    }
}
