package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andela.helpmebuy.R;

public class ChangeEmailActivity extends AppCompatActivity {
    private TextView oldEmailText;
    private TextView newEmailText;
    private Button updateEmailButton;

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

        oldEmailText = (TextView)findViewById(R.id.old_email_text);
        newEmailText = (TextView)findViewById(R.id.new_email_text);
        updateEmailButton = (Button)findViewById(R.id.update_email_button);
    }

    public void updateEmail(View view) {
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

}
