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
import com.andela.helpmebuy.utilities.ConnectionRequestListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class RequestActivityFragment extends Fragment {

    private List<Connection> connections;
    private ConnectionRequestsAdapter requestsAdapter;
    private ProgressWheel progressWheel;
    private ImageView retry;
    private CountDownTimer countDownTimer;

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
        requestsAdapter.setConnectionRequestListener((ConnectionRequestListener) getActivity());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(requestsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        progressWheel = (ProgressWheel) view.findViewById(R.id.connection_progress_wheel);
    }

    private void loadConnections() {
        countDown();

        new FirebaseCollection<>(connectionUrl(), Connection.class)
                .query(getString(R.string.connectionStatus), 2, new DataCallback<List<Connection>>() {
                    @Override
                    public void onSuccess(List<Connection> data) {
                        stopTimer();

                        if (!data.isEmpty()) {
                            for (Connection connection : data) {
                                int index = findIndex(connection);
                                if (index < 0) {
                                    connections.add(connection);
                                    requestsAdapter.notifyItemInserted(connections.size() - 1);
                                } else {
                                    connections.set(index, connection);
                                    requestsAdapter.notifyItemChanged(index);
                                }
                            }
                        } else {
                            displayMessage(getString(R.string.no_request_found));
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        displayMessage(errorMessage);
                        stopTimer();
                    }
                });
    }

    private int findIndex(Connection connection) {
        for (int i = 0, size = connections.size(); i < size; ++i) {
            if (connection.getId().equals(connections.get(i).getId())) {
                return i;
            }
        }

        return -1;
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
                    displayMessage("No connection request");
                }
            }
        };
        countDownTimer.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
        progressWheel.stopSpinning();
    }
}
