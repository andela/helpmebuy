package com.andela.helpmebuy.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ConnectionRequestsAdapter;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.models.ConnectionStatus;
import com.andela.helpmebuy.models.Contact;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;
import com.andela.helpmebuy.utilities.ContactsHelper;
import com.andela.helpmebuy.utilities.ContactsListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment {

    private List<Connection> connections;
    private ConnectionRequestsAdapter requestsAdapter;
    private ProgressWheel progressWheel;
    private ImageView retry;
    private CountDownTimer countDownTimer;
    private User user;

    public RequestFragment() {
        connections = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connection_request, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);

        loadConnections();
    }

    private void initializeComponents(View view) {
        retry = (ImageView) view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadConnections();
            }
        });

        requestsAdapter = new ConnectionRequestsAdapter(connections, getContext());
        requestsAdapter.setConnectionRequestListener(new ConnectionRequestListener() {
            @Override
            public void onConnectionUpdate(Connection connection) {
                String currentUserId = CurrentUserManager.get(getContext()).getId();
                updateConnection(connection, connectionUrl(currentUserId));

                Connection connection1 = new Connection(connection.getConnectionStatus());
                connection1.setId(currentUserId);
                connection1.setMessage(connection.getMessage());
                connection1.setReceiver(currentUserId);
                connection1.setSender(connection.getSender());

                updateConnection(connection1, connectionUrl(connection.getSender()));

                if (isConnectionAccepted(connection)) {
                    createContact(connection);
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(requestsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        progressWheel = (ProgressWheel) view.findViewById(R.id.connection_progress_wheel);

        user = CurrentUserManager.get(getContext());
    }


    private boolean isConnectionAccepted(Connection connection) {
        return connection.getConnectionStatus() == ConnectionStatus.ACCEPTED.getStatus();
    }

    private void loadConnections() {
        progressWheel.spin();
        countDown();

        new FirebaseCollection<>(connectionUrl(), Connection.class)
                .query(getString(R.string.connectionStatus), 2, new DataCallback<List<Connection>>() {
                    @Override
                    public void onSuccess(List<Connection> data) {
                        stopTimer();

                        if (!data.isEmpty()) {
                            for (Connection connection : data) {
                                if (!connection.getSender().equals(user.getId()) &&
                                        connection.getConnectionStatus() == ConnectionStatus.PENDING.getStatus()) {
                                    addConnection(connection);
                                }
                            }
                            retry.setVisibility(View.GONE);
                        } else {
                            displayMessage(getString(R.string.no_request_found));
                            retry.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        displayMessage(errorMessage);
                        stopTimer();
                    }
                });
    }

    private void addConnection(Connection connection) {
        int index = findIndex(connection);
        if (index < 0) {
            connections.add(connection);
            requestsAdapter.notifyItemInserted(connections.size() - 1);
        } else {
            connections.set(index, connection);
            requestsAdapter.notifyItemChanged(index);
        }
    }

    private int findIndex(Connection connection) {
        for (int i = 0, size = connections.size(); i < size; ++i) {
            if (connection.getId().equals(connections.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    private void createContact(Connection connection1) {
        Contact contact1 = new Contact();
        contact1.setId(connection1.getSender());
        Contact contact2 = new Contact();
        contact2.setId(connection1.getReceiver());

        saveContact(contact1, connection1.getReceiver());
        saveContact(contact2, connection1.getSender());
    }

    private void saveContact(Contact contact, String targetId) {
        ContactsHelper helper = new ContactsHelper(getContactsListener());
        helper.createContact(contact, targetId);
    }

    private ContactsListener getContactsListener() {
        return new ContactsListener() {
            @Override
            public void onContactCreated(Contact contact) {
                Toast.makeText(getActivity(), "Contact created successfully", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

    private String connectionUrl() {
        return Constants.CONNECTIONS + "/" + CurrentUserManager.get(getContext()).getId();
    }

    private void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void countDown() {
        retry.setVisibility(View.INVISIBLE);
        progressWheel.spin();

        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    retry.setVisibility(View.VISIBLE);
                }
            }
        };
        countDownTimer.start();
    }

    public void stopTimer() {
        countDownTimer.cancel();
        progressWheel.stopSpinning();
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
