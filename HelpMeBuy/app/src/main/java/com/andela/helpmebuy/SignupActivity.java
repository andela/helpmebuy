package com.andela.helpmebuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.andela.helpmebuy.models.User;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    public final String FIREBASE_URL = "https://hmbuy.firebaseio.com";

    private Firebase firebase;

    private EditText fullName;

    private EditText email;

    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        setContentView(R.layout.activity_signup);

        fullName = (EditText) findViewById(R.id.fullName_text);
        email = (EditText) findViewById(R.id.email_text);
        password = (EditText) findViewById(R.id.password_text);
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
        firebase.createUser(email.getText().toString(), password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {

            @Override
            public void onSuccess(Map<String, Object> result) {
                String id = result.get("uid").toString();

                User user = new User(id);
                user.setFullName(fullName.getText().toString());

                Log.i("SignupActivity", "Created user ID = " + id);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.d("SignupActivity", firebaseError.toString());
            }
        });
    }

}
