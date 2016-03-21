package com.andela.helpmebuy.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;

public class ConnectionRequestActivity extends AppCompatActivity
        implements ConnectionRequestListener {

    private Context context = ConnectionRequestActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_request);

        manageToolbar();
    }

    private void manageToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onConnectionUpdate(Connection connection) {
        updateConnection(connection, connectionUrl(CurrentUserManager.get(context).getId()));
        updateConnection(connection, connectionUrl(connection.getId()));
    }

    private void updateConnection(Connection connection, String connectionUrl) {
        new FirebaseCollection<>(connectionUrl, Connection.class)
                .save(connection, new DataCallback<Connection>() {
                    @Override
                    public void onSuccess(Connection data) {
                        displayMessage(R.string.operation_successful);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        displayMessage(R.string.operation_failed);
                    }
                });
    }

    private String connectionUrl(String userId) {
        return Constants.CONNECTIONS + "/" + userId;
    }

    private void displayMessage(int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
