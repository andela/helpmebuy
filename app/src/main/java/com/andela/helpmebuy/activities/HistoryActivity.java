package com.andela.helpmebuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.utilities.ActionBar;

public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        ActionBar.enableHomeButton(this);
        initializeView();
    }

    public void initializeView() {
        String[] history = {"Purchase Request"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.history_item, history);
        ListView listView = (ListView) findViewById(R.id.history_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            Intent intent = new Intent(getApplicationContext(), PurchaseHistoryActivity.class);
            startActivity(intent);
        }
    }
}
