package com.andela.helpmebuy;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.andela.helpmebuy.adapters.TravellersAdapter;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.utilities.Constants;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public final static String TAG = "HomeActivity";

    private Firebase firebase;

    private RecyclerView travellersView;

    private TravellersAdapter adapter;

    private List<Travel> travels;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private TextView userLocationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Firebase.setAndroidContext(this);
        firebase = new Firebase(Constants.FIREBASE_URL + "/" + Constants.TRAVELS);

        travels = new ArrayList<>();

        drawerLayout = (DrawerLayout) findViewById(R.id.home_activity_drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.home_activity_navigation_view);

        travellersView = (RecyclerView) findViewById(R.id.travellers_recycler_view);
        travellersView.addItemDecoration(new ItemDivider(this));
        registerForContextMenu(travellersView);

        initializeUserLocation();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        travellersView.setLayoutManager(layoutManager);

        adapter = new TravellersAdapter(this, travels);
        travellersView.setAdapter(adapter);

        loadTravels();
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
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Travel travel = snapshot.getValue(Travel.class);

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
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, firebaseError.getMessage());
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.message_action:
                return true;
            case R.id.connect_action:
                return true;
            case R.id.more_action:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

}
