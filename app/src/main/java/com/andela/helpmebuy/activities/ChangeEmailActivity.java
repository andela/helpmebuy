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
import com.andela.helpmebuy.authentication.FirebaseChangeEmail;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.firebase.client.Firebase;

public class ChangeEmailActivity extends AppCompatActivity {
    private TextView oldEmailText;
    private TextView newEmailText;
    private TextView passwordText;
    private Button updateEmailButton;
    private FirebaseChangeEmail firebaseChangeEmail;
    private LinearLayout parentLayout;
    private FirebaseCollection<User> collection;

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
    }

    public void updateEmail(View view) {
        String oldEmail = oldEmailText.getText().toString();
        final String newEmail = newEmailText.getText().toString();
        String password = passwordText.getText().toString();
        firebaseChangeEmail = new FirebaseChangeEmail(this);
        firebaseChangeEmail.changeEmail(oldEmail, newEmail, password, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                user.setEmail(newEmail);
                collection = new FirebaseCollection<User>(Constants.USERS, User.class);
                //collection.save();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onFailure(Exception e) {

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
