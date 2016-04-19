package com.andela.helpmebuy.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.TravellersAdapter;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.models.ConnectionStatus;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.utilities.CurrentTravelListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.HomeCountryDetector;
import com.andela.helpmebuy.utilities.HomeCountryDetectorListener;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.andela.helpmebuy.utilities.LocationPickerDialog;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class TravelListFragment extends Fragment implements CurrentTravelListener {

    public final static String TAG = "HomeActivity";

    private RecyclerView travellersView;

    private TravellersAdapter adapter;

    private List<Travel> travels;

    private TextView notify;

    private FirebaseCollection<Travel> travelsCollection;

    private HomeCountryDetector homeCountryDetector;

    private String country = "";

    private ProgressWheel progressWheel;

    private Context context;

    private boolean isRunning;

    private Location userLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        loadComponents(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isRunning) {
            homeCountryDetector.connect();
        }
    }

    private void loadComponents(final View rootView) {
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.search_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLocation();

            }
        });
        travellersView = (RecyclerView) rootView.findViewById(R.id.travellers_recycler_view);
        travellersView.addItemDecoration(new ItemDivider(context));
        registerForContextMenu(travellersView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        travellersView.setLayoutManager(layoutManager);
        travels = new ArrayList<>();
        adapter = new TravellersAdapter(context, travels);
        adapter.setCurrentTravelListener(this);
        travellersView.setAdapter(adapter);
        notify = (TextView) rootView.findViewById(R.id.notify);
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);

        notify.setVisibility(View.INVISIBLE);
        progressWheel.spin();
        detectCountry();
    }

    public void detectCountry() {
        homeCountryDetector = new HomeCountryDetector(getActivity());

        homeCountryDetector.setListener(new HomeCountryDetectorListener() {
            @Override
            public void onCountryDetected(String name) {
                country = homeCountryDetector.getCountryName();
                loadTravellersByCountry(country);
                Log.d(TAG, country);
                homeCountryDetector.disconnect();
                isRunning = true;
            }
        });
    }

    private void loadTravellersByCountry(String countryName) {
        progressWheel.spin();
        travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);
        travelsCollection.query("arrivalAddress/country", countryName, travelDataCallback);
    }

    DataCallback<List<Travel>> travelDataCallback = new DataCallback<List<Travel>>() {
        @Override
        public void onSuccess(List<Travel> data) {
            if (!data.isEmpty()) {
                notify.setVisibility(View.INVISIBLE);

                for (Travel travel : data) {
                    int index = findIndex(travel);

                    if (index < 0) {
                        travels.add(travel);
                        adapter.notifyItemInserted(travels.size() - 1);
                    } else {
                        travels.set(index, travel);
                        adapter.notifyItemChanged(index);
                    }
                }
            } else {
                notify.setVisibility(View.VISIBLE);
            }
            progressWheel.stopSpinning();
        }

        @Override
        public void onError(String errorMessage) {
            progressWheel.stopSpinning();
        }
    };

    private int findIndex(Travel travel) {
        for (int i = 0, size = travels.size(); i < size; ++i) {
            if (travel.getId().equals(travels.get(i).getId())) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void getCurrentTravel(Travel travel) {
        if (travel != null) {
            postConnectionRequest(travel.getUserId());
        }
    }

    private void postConnectionRequest(final String toUserId) {
        String currentUserId = CurrentUserManager.get(context).getId();

        Connection connection = new Connection(ConnectionStatus.PENDING.getStatus());
        connection.setId(currentUserId);
        connection.setSender(currentUserId);
        connection.setReceiver(toUserId);

        Connection connection1 = new Connection(ConnectionStatus.PENDING.getStatus());
        connection1.setId(toUserId);
        connection1.setSender(currentUserId);
        connection1.setReceiver(toUserId);

        sendConnection(Constants.CONNECTIONS + "/" + toUserId, connection);
        sendConnection(Constants.CONNECTIONS + "/" + currentUserId, connection1);
    }

    private void sendConnection(String url, Connection connection) {
        new FirebaseCollection<>(url, Connection.class)
                .save(connection, new DataCallback<Connection>() {
                    @Override
                    public void onSuccess(Connection data) {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }
    public void changeLocation() {
        final LocationPickerDialog dialog = new LocationPickerDialog(context);
        dialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
            @Override
            public void onLocationSet(Location location) {
                travels.clear();
                adapter.notifyDataSetChanged();
                userLocation = location;
                notify.setVisibility(View.VISIBLE);
                loadTravelsByLocation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void loadTravelsByLocation() {
        travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);
        travelsCollection.query("arrivalAddress/location", userLocation.toFullString(), travelDataCallback);
    }
}
