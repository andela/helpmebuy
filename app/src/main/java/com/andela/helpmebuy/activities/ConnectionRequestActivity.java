package com.andela.helpmebuy.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;

public class ConnectionRequestActivity extends AppCompatActivity
implements ConnectionRequestListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = ConnectionRequestActivity.this;
        setSupportActionBar(toolbar);
    }
    @Override
    public void onConnectionUpdate(Connection connection) {
        //TODO: Refactor duplicate lines of code.
        User user = connection.getUser();
        FirebaseCollection<Connection> firebaseCollection =
                new FirebaseCollection<>(Constants.CONNECTIONS
                        + "/" + user.getId(), Connection.class);
        firebaseCollection.save(connection, new DataCallback<Connection>() {
            @Override
            public void onSuccess(Connection data) {
                Toast.makeText(context, "Operation Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
