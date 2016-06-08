package com.andela.helpmebuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ItemRequestAdapter;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.utilities.RequestsItemDivider;

import java.util.ArrayList;

public class PurchaseReqResponse extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<PurchaseItem> items;
    private ItemRequestAdapter itemRequestAdapter;
    private String receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_req_response);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeComponents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_purchase_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_send_purchase_request) {
//            //createPurchaseRequest();
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    private void initializeComponents() {
        Intent i = getIntent();
        PurchaseRequest purchaseRequest = i.getExtras().getParcelable("request");
        receiverId = purchaseRequest.getReceiver();
        itemRequestAdapter = new ItemRequestAdapter(this, purchaseRequest);
        recyclerView = (RecyclerView) findViewById(R.id.purch_req_responce_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemRequestAdapter);
        recyclerView.addItemDecoration(new RequestsItemDivider(this));
    }
}
