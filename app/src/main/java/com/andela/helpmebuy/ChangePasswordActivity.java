package com.andela.helpmebuy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class ChangePasswordActivity extends AppCompatActivity {

    private Button change_password_buton;
    private EditText old_password;
    private EditText new_password;
    private Firebase firebase;
    private LinearLayout parent_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebase = new Firebase(Constants.FIREBASE_URL);
        parent_layout = (LinearLayout) findViewById(R.id.linear_layout);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
    }

    public void changePassword(View view) {
        String oldPassword = old_password.getText().toString();
        String newPassword = new_password.getText().toString();
        String email = SigninActivity.UserEmail;

        if(oldPassword.equals("")) {
            old_password.setError(getResources().getString(R.string.password_missing));
        }
        else if (newPassword.equals("")){
            new_password.setError(getResources().getString(R.string.password_missing));
        }
        else {
            change_password_buton.setEnabled(false);
            Snackbar.make(parent_layout,email,Snackbar.LENGTH_LONG).show();

            /*firebase.changePassword(email, oldPassword, newPassword, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    change_password_buton.setEnabled(true);
                    Snackbar.make(parent_layout,"Password successfully changed",Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    change_password_buton.setEnabled(true);
                    Snackbar.make(parent_layout,firebaseError.getMessage(),Snackbar.LENGTH_LONG).show();
                }
            });*/
        }
    }

}
