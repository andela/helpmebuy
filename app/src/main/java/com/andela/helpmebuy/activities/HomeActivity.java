package com.andela.helpmebuy.activities;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.TravellersAdapter;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.utilities.Constants;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.andela.helpmebuy.utilities.Launcher;
import com.andela.helpmebuy.utilities.LocationPickerDialog;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public final static String TAG = "HomeActivity";

    private RecyclerView travellersView;

    private TravellersAdapter adapter;

    private List<Travel> travels;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private TextView userLocationTextView;

    private CoordinatorLayout parentLayout;

    private FirebaseCollection<Travel> travelsCollection;

    private Location userLocation;

    private ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        addActionBar();
        initializeUserLocation();
        loadTravels();
        loadComponents();
    }

    private void addActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void loadComponents() {
        parentLayout = (CoordinatorLayout) findViewById(R.id.parent_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.home_activity_drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.home_activity_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        travellersView = (RecyclerView) findViewById(R.id.travellers_recycler_view);
        travellersView.addItemDecoration(new ItemDivider(this));
        registerForContextMenu(travellersView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        travellersView.setLayoutManager(layoutManager);
        travels = new ArrayList<>();
        adapter = new TravellersAdapter(this, travels);
        travellersView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void loadTravels() {
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        progressWheel.spin();
        travels = new ArrayList<>();
        travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);

        travelsCollection.getAll(new DataCallback<List<Travel>>() {
            @Override
            public void onSuccess(List<Travel> data) {
                progressWheel.stopSpinning();

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

                progressWheel.stopSpinning();
            }

            @Override
            public void onError(String errorMessage) {
                progressWheel.stopSpinning();
                Log.d(TAG, errorMessage);
                progressWheel.stopSpinning();
            }
        });
    }

    public void loadTravelsByLocation(){
        FeedLoader feedLoader = new FeedLoader();
        feedLoader.execute(TAG);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.traveller_item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenu.ContextMenuInfo i =  item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.message_action:
                message(info);
                return true;

            case R.id.connect_action:
                connect(info);
                return true;

            case R.id.more_action:
                more(info);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_travel) {
            Launcher.launchActivity(this, CreateTravelActivity.class);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void connect(AdapterView.AdapterContextMenuInfo info) {
        Snackbar.make(parentLayout,"Connect clicked", Snackbar.LENGTH_LONG).show();
    }

    private void message(AdapterView.AdapterContextMenuInfo info) {
        Snackbar.make(parentLayout, "Message clicked", Snackbar.LENGTH_LONG).show();
    }

    private void more(AdapterView.AdapterContextMenuInfo info) {
        Snackbar.make(parentLayout,"More clicked",Snackbar.LENGTH_LONG).show();
    }

    private int findIndex(Travel travel) {
        for (int i = 0, size = travels.size(); i < size; ++i) {
            if (travel.getId().equals(travels.get(i).getId())) {
                return i;
            }
        }

        return -1;
    }

    private void initializeUserLocation() {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.user_location, null, false);
        userLocationTextView = (TextView) view.findViewById(R.id.user_location_text_view);

        userLocationTextView.setText("Departure Address");
        view.setLayoutParams(new Toolbar.LayoutParams(Gravity.END));
        toolbar.addView(view);
    }

    public void changeLocation(View view) {
        travels.clear();

        final LocationPickerDialog dialog = new LocationPickerDialog(this);
        dialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
            @Override
            public void onLocationSet(Location location) {
                userLocationTextView.setText(location.toString());
                userLocation = location;
                loadTravelsByLocation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public class FeedLoader extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);
            travelsCollection.query("departureAddress/location", userLocation.toFullString(), new DataCallback<List<Travel>>() {
                @Override
                public void onSuccess(List<Travel> data) {
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
                }

                @Override
                public void onError(String errorMessage) {

                }
            });

            return null;
        }
    }
}
