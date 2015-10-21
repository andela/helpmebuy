package com.andela.helpmebuy.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.EmailPasswordAuth;
import com.andela.helpmebuy.authentication.FirebaseAuth;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.andela.helpmebuy.utilities.CurrentUser;
import com.andela.helpmebuy.utilities.Launcher;
import com.andela.helpmebuy.utilities.SoftKeyboard;

public class SignupActivity extends AppCompatActivity {

    private RelativeLayout parentLayout;

    private EditText fullNameEditText;

    private EditText emailEditText;

    private EditText passwordEditText;

    private Button signupButton;

    private EmailPasswordAuth emailPasswordAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (CurrentUser.get(this) != null) {
            Launcher.launchActivity(this, HomeActivity.class);
            finish();
        }

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

        initializeEmailPasswordAuth();
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
        SoftKeyboard.hide(SignupActivity.this);

        final String fullName = fullNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (fullName.isEmpty()) {
            fullNameEditText.setError(getResources().getString(R.string.fullname_missing));

        } else if (email.isEmpty()) {
            emailEditText.setError(getResources().getString(R.string.email_missing));

        } else if (password.isEmpty()) {
            passwordEditText.setError(getResources().getString(R.string.password_missing));

        } else {
            signupButton.setText(R.string.signing_up);
            signupButton.setEnabled(false);

            emailPasswordAuth.signUp(email, password, new AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    signupButton.setText(R.string.signup);

                    user.setFullName(fullName);

                    saveUser(user);

                    CurrentUser.save(user, SignupActivity.this);

                    Launcher.launchActivity(SignupActivity.this, HomeActivity.class);
                    finish();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(String errorMessage) {
                    signupButton.setText(R.string.signup);
                    signupButton.setEnabled(true);

                    if (errorMessage.contains("email")) {
                        emailEditText.setError(errorMessage);
                    } else {
                        Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    public void signIn(View view) {
        Launcher.launchActivity(this, SigninActivity.class);
        finish();
    }

    public void saveUser(User user) {
        FirebaseCollection<User> users = new FirebaseCollection<>(Constants.USERS, User.class);

        users.save(user, null);
    }

    public void initializeEmailPasswordAuth() {
        emailPasswordAuth = new FirebaseAuth();
    }
}
