package com.andela.helpmebuy.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ConnectionRequestsAdapter;
import com.andela.helpmebuy.adapters.ContactsAdapter;
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
import com.andela.helpmebuy.utilities.ListCallback;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private View rootView;

    private Context context;

    private User currentUser;

    private List<Contact> contacts;

    private ContactsAdapter adapter;

    private TextView noContacts;

    RecyclerView recyclerView;

    private List<Connection> connections;
    private ConnectionRequestsAdapter requestsAdapter;
    private User user;
    private TextView noContactRequest;
    RecyclerView requestRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.rootView = view;
        context = getActivity();
        contacts = new ArrayList<>();
        initializeComponents();
        initializeRecyclerView();
        getCurrentUser();
        loadConnections();
    }

    private void initializeComponents() {
        noContacts = (TextView)rootView.findViewById(R.id.no_contact);
        noContactRequest = (TextView)rootView.findViewById(R.id.no_contact_request);

        connections = new ArrayList<>();
        user = CurrentUserManager.get(getContext());
    }

    private void initializeRecyclerView() {
        adapter = new ContactsAdapter(context, contacts);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.contact_list);

        requestsAdapter = new ConnectionRequestsAdapter(connections, context, rootView);
        requestsAdapter.setConnectionRequestListener(new ConnectionRequestListener() {
            @Override
            public void onConnectionUpdate(Connection connection) {
                String currentUserId = CurrentUserManager.get(context).getId();
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


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        requestRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_conn_req);

        requestRecyclerView.setAdapter(requestsAdapter);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        requestRecyclerView.setHasFixedSize(false);
    }

    private void getCurrentUser() {
        currentUser = CurrentUserManager.get(context);
        loadContacts();
    }

    private void loadContacts() {
        ContactsHelper helper = new ContactsHelper(null);
        helper.getAllContacts(currentUser.getId(), getListCallback());
    }

    private ListCallback<Contact> getListCallback() {
        return new ListCallback<Contact>() {
            @Override
            public void onGetList(List<Contact> items) {
                if (items.size() == 0) {
                    noContacts.setVisibility(View.VISIBLE);
                    noContactRequest.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    adapter.setContacts(items);
                    adapter.notifyDataSetChanged();
                    noContacts.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private boolean isConnectionAccepted(Connection connection) {
        return connection.getConnectionStatus() == ConnectionStatus.ACCEPTED.getStatus();
    }

    private void loadConnections() {
        new FirebaseCollection<>(connectionUrl(), Connection.class)
                .query(getString(R.string.connectionStatus), 2, new DataCallback<List<Connection>>() {
                    @Override
                    public void onSuccess(List<Connection> data) {
                        int size = data.size();
                        noContacts.setVisibility(View.INVISIBLE);
                        if (size > 0) {
                            ArrayList<Connection> myRequest = new ArrayList<>();
                            for (Connection connection : data) {
                                if (isConnectionPending(connection)) {
                                    myRequest.add(connection);
                                }
                                connections = myRequest;
                                requestsAdapter.swapList(connections);
                            }
                            if (connections.size() > 0) {
                                noContactRequest.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            noContactRequest.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        displayMessage(errorMessage);
                    }
                });
    }

    private boolean isConnectionPending(Connection connection) {
        return !connection.getSender().equals(user.getId()) &&
                connection.getConnectionStatus() == ConnectionStatus.PENDING.getStatus();
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

    private void updateConnection(Connection connection, String connectionUrl) {
        new FirebaseCollection<>(connectionUrl, Connection.class)
                .save(connection, new DataCallback<Connection>() {
                    @Override
                    public void onSuccess(Connection data) {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }

    private String connectionUrl(String userId) {
        return Constants.CONNECTIONS + "/" + userId;
    }

}