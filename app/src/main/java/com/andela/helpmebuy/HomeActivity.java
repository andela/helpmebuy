package com.andela.helpmebuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.andela.helpmebuy.adapters.TravellersAdapter;
import com.andela.helpmebuy.models.Address;
import com.andela.helpmebuy.models.Travel;
import com.andela.helpmebuy.models.User;

import org.joda.time.DateTime;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView travellersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        travellersView = (RecyclerView) findViewById(R.id.travellers_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        travellersView.setLayoutManager(layoutManager);

        Travel graceTravel = new Travel("1");
        graceTravel.setUser(new User("1", "Grace Bukola"));
        graceTravel.setDepartureAddress(new Address("Arizona", "USA"));
        graceTravel.setDepartureDate(DateTime.now());

        Travel obiomaTravel = new Travel("2");
        obiomaTravel.setUser(new User("2", "Obioma Ofoamalu"));
        obiomaTravel.setDepartureAddress(new Address("London", "UK"));
        obiomaTravel.setDepartureDate(DateTime.now());

        TravellersAdapter adapter = new TravellersAdapter(this, Arrays.asList(graceTravel, obiomaTravel));

        travellersView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
