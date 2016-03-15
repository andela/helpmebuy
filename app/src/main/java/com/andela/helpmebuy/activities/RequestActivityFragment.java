package com.andela.helpmebuy.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ConnectionRequestsAdapter;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.utilities.ConnectionRequestListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;

import java.util.ArrayList;
import java.util.List;


public class RequestActivityFragment extends Fragment {

    private List<Connection> connections;
    private ConnectionRequestsAdapter requestsAdapter;
    private Context context;
    private View view;
    private ProgressWheel progressWheel;

    public RequestActivityFragment() {
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

        context = getContext();

        requestsAdapter = new ConnectionRequestsAdapter(connections);
        requestsAdapter.setConnectionRequestListener(
                (ConnectionRequestListener) getActivity());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(requestsAdapter);
        recyclerView.setHasFixedSize(true);

        progressWheel = (ProgressWheel)view.findViewById(R.id.connection_progress_wheel);

        this.view =  view;

        loadConnections();
    }

    private void loadConnections() {
        progressWheel.spin();

        String userId = CurrentUserManager.get(context).getId();

        DataCallback<List<Connection>> connectionDataCallback = new DataCallback<List<Connection>>() {
            @Override
            public void onSuccess(List<Connection> data) {
                if (!data.isEmpty()) {
                    initializeAdapter(data);
                } else {
                    Toast.makeText(context, "No data available.", Toast.LENGTH_SHORT).show();
                }
                progressWheel.stopSpinning();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Error fetching connections", Toast.LENGTH_SHORT).show();
                progressWheel.stopSpinning();
            }
        };

        FirebaseCollection<Connection> connectionCollection
                = new FirebaseCollection<>(Constants.CONNECTIONS, Connection.class);
        connectionCollection.query(userId + "/connectionStatus", "2", connectionDataCallback);
    }

    private void initializeAdapter(List<Connection> connections) {
        for (Connection connection : connections) {
            this.connections.add(connection);
        }
        requestsAdapter.notifyDataSetChanged();
    }


}
