package com.andela.helpmebuy.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.andela.helpmebuy.dialogs.CityPickerDialog;
import com.andela.helpmebuy.dialogs.CountryPickerDialog;
import com.andela.helpmebuy.models.Address;
import com.andela.helpmebuy.models.City;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Region;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.Constants;
import com.andela.helpmebuy.utilities.CurrentUser;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.andela.helpmebuy.utilities.LocationPickerDialog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity  {
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

    private String userLocation = CityPickerDialog.userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        addActionBar();

        loadTravels();

        loadComponents();

        initializeUserLocation();
        saveTravels();
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

        travellersView = (RecyclerView) findViewById(R.id.travellers_recycler_view);
        travellersView.addItemDecoration(new ItemDivider(this));
        registerForContextMenu(travellersView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        travellersView.setLayoutManager(layoutManager);

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

    public void saveTravels() {
        User user = CurrentUser.get(this);
        Travel travel = new Travel();
        travel.setId("150");
        travel.setUserId(user.getId());
        Country country = new Country();
        country.setId("200");
        country.setName("Nigeria");
        Region region = new Region();
        region.setId("100");
        region.setName("Lagos");
        region.setCountryId("200");
        City city = new City();
        city.setId("500");
        city.setCountryId("200");
        city.setName("Yaba");
        city.setRegionId("100");
        Location location = new Location(country, region, city);
        Address depature = new Address();
        depature.setLocation(location);
        travel.setDepartureAddress(depature);
        travel.setArrivalAddress(depature);
        travel.setArrivalDate(DateTime.now());
        travel.setDepartureDate(DateTime.now());
        FirebaseCollection<Travel> firebaseCollection =new FirebaseCollection<Travel>(Constants.TRAVELS, Travel.class);
        firebaseCollection.save(travel, new DataCallback<Travel>() {
            @Override
            public void onSuccess(Travel data) {
                Log.d(TAG, "SUCCESS");
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(TAG, errorMessage);
            }
        });

    }

    private void loadTravels() {
        travels = new ArrayList<>();

        travelsCollection = new FirebaseCollection<>(Constants.TRAVELS, Travel.class);

        travelsCollection.getAll(new DataCallback<List<Travel>>() {
            @Override
            public void onSuccess(List<Travel> data) {
                for (Travel travel : data) {
                    int index = findIndex(travel);

                    if (index <= 0) {
                        travels.add(travel);

                        adapter.notifyItemInserted(travels.size());
                    } else {
                        travels.set(index, travel);

                        adapter.notifyItemChanged(index);
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(TAG, errorMessage);
            }
        });
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
        userLocationTextView.setText("Lagos, Nigeria");
        view.setLayoutParams(new Toolbar.LayoutParams(Gravity.END));

        toolbar.addView(view);
    }

    public void changeLocation(View view) {
        final LocationPickerDialog dialog = new LocationPickerDialog(this);
        dialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
            @Override
            public void onLocationSet(Location location) {
                userLocationTextView.setText(location.toString());

                dialog.dismiss();
            }
        });

        dialog.show();

//        CountryPickerDialog countryPickerDialog = new CountryPickerDialog();
//        countryPickerDialog.show(HomeActivity.this.getSupportFragmentManager(), "countries_picker");
//        userLocationTextView.setText(CityPickerDialog.userLocation);
    }
}
