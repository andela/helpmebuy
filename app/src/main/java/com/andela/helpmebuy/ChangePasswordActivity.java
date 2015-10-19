package com.andela.helpmebuy;

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


public class ChangePasswordActivity extends AppCompatActivity {

    private Button change_password_buton;
    private EditText emailText;
    private EditText old_password;
    private EditText new_password;
    private LinearLayout parent_layout;
    private PasswordReset firebasePasswordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebasePasswordReset = new FirebasePasswordReset();
        parent_layout = (LinearLayout) findViewById(R.id.linear_layout);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        emailText = (EditText) findViewById(R.id.emailtext);
        change_password_buton = (Button) findViewById(R.id.change_password);

    }

    public void changePassword(View view) {
        String email = emailText.getText().toString().trim();
        String oldPassword = old_password.getText().toString();
        String newPassword = new_password.getText().toString();

        if (email.isEmpty())
            emailText.setError(getResources().getString(R.string.email_missing));
        else if(oldPassword.equals("")) {
            old_password.setError(getResources().getString(R.string.password_missing));
        }
        else if (newPassword.equals("")){
            new_password.setError(getResources().getString(R.string.password_missing));
        }
        else {
            change_password_buton.setEnabled(false);
            final Intent intent =new Intent(this, HomeActivity.class);
            firebasePasswordReset.changePassword(email, oldPassword, newPassword, new AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    Snackbar.make(parent_layout, "Password changed successfully", Snackbar.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(String errorMessage) {
                   Snackbar.make(parent_layout, errorMessage.toString(), Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Snackbar.make(parent_layout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });


        }
    }

}
