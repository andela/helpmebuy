package com.andela.helpmebuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ItemRequestAdapter;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.models.PurchaseRequestStatus;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.ItemClickListener;
import com.andela.helpmebuy.utilities.RequestsItemDivider;

import java.util.ArrayList;

public class PurchaseReqResponse extends AppCompatActivity implements ItemClickListener{

    private RecyclerView recyclerView;
    private ArrayList<PurchaseItem> items;
    private ArrayList<PurchaseItem> acceptedItems;
    private ItemRequestAdapter itemRequestAdapter;
    private String receiverId;
    private String requestId;
    private boolean send;
    private PurchaseRequest purchaseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_req_response);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_purchase_request);
        toolbar.setTitle("Purchase");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeComponents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_purchase_req_response, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_accept_pur_req) {
            if(send) {
                purchaseRequest.getSenderId();
                CurrentUserManager.get(this).getId();
                purchaseRequest.setPurchaseStatus(PurchaseRequestStatus.ACCEPTED.getStatus());
                purchaseRequest.setId(purchaseRequest.getSenderId());
                performRequestUpdates();
            }
            finish();
            return true;
        }
        if (id == R.id.action_reject_pur_req) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void performRequestUpdates() {
        updateRequest(purchaseRequest, Constants.PURCHASE_REQUEST + "/" + CurrentUserManager.get(this).getId());
        updateRequest(purchaseRequest, Constants.PURCHASE_REQUEST + "/" + purchaseRequest.getSender());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    private void initializeComponents() {
        Intent i = getIntent();
        send = false;
        acceptedItems = new ArrayList<>();
        purchaseRequest = i.getExtras().getParcelable("request");
        receiverId = purchaseRequest.getReceiver();
        requestId = purchaseRequest.getSenderId();
        items = purchaseRequest.getPurchaseList();
        itemRequestAdapter = new ItemRequestAdapter(this, items, this);
        recyclerView = (RecyclerView) findViewById(R.id.purch_req_responce_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemRequestAdapter);
        recyclerView.addItemDecoration(new RequestsItemDivider(this));
    }

    @Override
    public void rejectItem(View view, int position) {
        items.remove(position);
        itemRequestAdapter.notifyDataSetChanged();
    }

    @Override
    public void acceptItem(View view, int position) {
        acceptedItems.add(items.get(position));
        send = true;
//        itemRequestAdapter.notifyDataSetChanged();
    }

    private String purchaseRequestUrl() {
        return Constants.PURCHASE_REQUEST + "/" + CurrentUserManager.get(this).getId() ;
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateRequest(PurchaseRequest request, String requestUrl) {
        new FirebaseCollection<>(requestUrl, PurchaseRequest.class)
                .save(request, new DataCallback<PurchaseRequest>() {
                    @Override
                    public void onSuccess(PurchaseRequest data) {
                        displayMessage(getString(R.string.operation_successful));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        displayMessage(getString(R.string.operation_failed));
                    }
                });
    }
}
