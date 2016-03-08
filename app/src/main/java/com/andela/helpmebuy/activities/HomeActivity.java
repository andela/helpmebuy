package com.andela.helpmebuy.activities;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.TravellersAdapter;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.Connection;
import com.andela.helpmebuy.models.ConnectionStatus;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.utilities.CurrentTravelListener;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.HomeCountryDetector;
import com.andela.helpmebuy.utilities.HomeCountryDetectorListener;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.andela.helpmebuy.utilities.Launcher;
import com.andela.helpmebuy.utilities.LocationPickerDialog;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CurrentTravelListener {
    public final static String TAG = "HomeActivity";

    private RecyclerView travellersView;

    private TravellersAdapter adapter;

    private List<Travel> travels;

    private Travel travel;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private TextView userLocationTextView;

    private TextView usernameTextView;

    private TextView userEmailTextView;

    private TextView notify;

    private CoordinatorLayout parentLayout;

    private LinearLayout drawerHeader;

    private FirebaseCollection<Travel> travelsCollection;

    private Location userLocation;

    private HomeCountryDetector homeCountryDetector;

    private android.location.Location location;

    private HomeCountryDetectorListener listener;

    private String country = "";

    private ProgressWheel progressWheel;

    private Context context = HomeActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        parentLayout = (CoordinatorLayout) findViewById(R.id.parent_layout);

        addActionBar();
        initializeUserLocation();

        //loadTravels();

        loadComponents();

        //setUserProfile(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        homeCountryDetector.connect();
    }

    @Override
    protected void onStop() {
        homeCountryDetector.disconnect();
        super.onStop();
    }

    private void addActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

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

        View drawerHeader = LayoutInflater.from(this).inflate(R.layout.home_activity_drawer_header, null);
        navigationView.addHeaderView(drawerHeader);
        usernameTextView = (TextView) drawerHeader.findViewById(R.id.user_name_text);
        userEmailTextView = (TextView) drawerHeader.findViewById(R.id.user_email_text);

        travellersView = (RecyclerView) findViewById(R.id.travellers_recycler_view);
        travellersView.addItemDecoration(new ItemDivider(this));
        registerForContextMenu(travellersView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        travellersView.setLayoutManager(layoutManager);
        travels = new ArrayList<>();
        adapter = new TravellersAdapter(this, travels);
        adapter.setCurrentTravelListener(this);
        travellersView.setAdapter(adapter);
        setUserProfile(this);

        notify = (TextView) findViewById(R.id.notify);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        notify.setVisibility(View.INVISIBLE);
        progressWheel.spin();

        detectCountry();
    }

    public void detectCountry() {
        homeCountryDetector = new HomeCountryDetector(HomeActivity.this);

        homeCountryDetector.setListener(new HomeCountryDetectorListener() {
            @Override
            public void onCountryDetected(String name) {
                country = homeCountryDetector.getCountryName();
                loadTravellersByCountry(country);
                Log.d(TAG, country);
                homeCountryDetector.disconnect();
            }
        });
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

    private void loadTravels() {

        travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);

        travelsCollection.getAll(travelDataCallback);
    }

    private void loadTravellersByCountry(String countryName) {
        progressWheel.spin();
        travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);
        travelsCollection.query("departureAddress/country", countryName, travelDataCallback);
    }


    public void loadTravelsByLocation() {
        travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);
        travelsCollection.query("departureAddress/location", userLocation.toFullString(), travelDataCallback);
    }

    private void setUserProfile(Context context) {
        User user = CurrentUserManager.get(context);
        String name = user.getFullName();
        String email = user.getEmail();
        usernameTextView.setText(name);
        userEmailTextView.setText(email);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.traveller_item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenu.ContextMenuInfo i = item.getMenuInfo();
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
                Launcher.launchActivity(this, ConnectTravellersActivity.class);
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
        if (id == R.id.manage_profile) {
            Launcher.launchActivity(this, UserSettingsActivity.class);

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void connect(AdapterView.AdapterContextMenuInfo info) {
        if (this.travel != null) {
            User user = CurrentUserManager.get(context);
            Connection connection = new Connection(user, ConnectionStatus.PENDING.getStatus());
            connection.setId(user.getId());

            FirebaseCollection<Connection> firebaseCollection =
                    new FirebaseCollection<>(Constants.CONNECTIONS+"/"+travel.getUserId(), Connection.class);
            firebaseCollection.save(connection, new DataCallback<Connection>() {
                @Override
                public void onSuccess(Connection data) {
                    Toast.makeText(context, "Connect request sent", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorMessage) {
                }
            });
        }
    }

    private void message(AdapterView.AdapterContextMenuInfo info) {
        Snackbar.make(parentLayout, "Message clicked", Snackbar.LENGTH_LONG).show();
    }

    private void more(AdapterView.AdapterContextMenuInfo info) {
        Snackbar.make(parentLayout, "More clicked", Snackbar.LENGTH_LONG).show();
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
        final LocationPickerDialog dialog = new LocationPickerDialog(this);
        dialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
            @Override
            public void onLocationSet(Location location) {
                travels.clear();
                userLocationTextView.setText(location.toString());
                userLocation = location;
                notify.setVisibility(View.VISIBLE);
                loadTravelsByLocation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void getCurrentTravel(Travel travel) {
        this.travel = travel;
    }
}
