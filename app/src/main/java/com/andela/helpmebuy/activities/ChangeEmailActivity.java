package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.ChangeEmailAuth;
import com.andela.helpmebuy.authentication.ChangeEmailCallback;
import com.andela.helpmebuy.authentication.FirebaseAuth;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.Firebase;

public class ChangeEmailActivity extends AppCompatActivity{
    private TextView oldEmailText;
    private TextView newEmailText;
    private TextView passwordText;
    private Button updateEmailButton;
    private ChangeEmailAuth changeEmailAuth;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parentLayout = (LinearLayout)findViewById(R.id.change_email_layout);

        oldEmailText = (TextView)findViewById(R.id.old_email_text);
        newEmailText = (TextView)findViewById(R.id.new_email_text);
        passwordText =  (TextView)findViewById(R.id.password_text);
        updateEmailButton = (Button)findViewById(R.id.update_email_button);
        changeEmailAuth = new FirebaseAuth(this);
    }

    public void updateEmail(View view) {
        String oldEmail = oldEmailText.getText().toString();
        String newEmail = newEmailText.getText().toString();
        String password = passwordText.getText().toString();

        changeEmailAuth.changeEmail(oldEmail, newEmail, password, new ChangeEmailCallback(){

            @Override
            public void onSuccess() {
                Snackbar.make(parentLayout,"Email successfully changed",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Snackbar.make(parentLayout,"Email not successfully changed",Snackbar.LENGTH_LONG).show();
            }
        });

    }


//        var ref = new Firebase("https://<YOUR-FIREBASE-APP>.firebaseio.com");
//        ref.changeEmail({
//                oldEmail : "bobtony@firebase.com",
//                newEmail : "bobtony@google.com",
//                password : "correcthorsebatterystaple"
//        }, function(error) {
//            if (error === null) {
//                console.log("Email changed successfully");
//            } else {
//                console.log("Error changing email:", error);
//            }
//        });



}
