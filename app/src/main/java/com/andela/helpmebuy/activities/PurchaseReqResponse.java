package com.andela.helpmebuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ItemRequestAdapter;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.models.PurchaseRequestStatus;
import com.andela.helpmebuy.utilities.ActionBar;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.RequestsItemDivider;

import java.util.ArrayList;

public class PurchaseReqResponse extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ArrayList<PurchaseItem> items;
    private ItemRequestAdapter itemRequestAdapter;
    private PurchaseRequest purchaseRequest;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_req_response);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_purchase_request);
        toolbar.setTitle("Purchase");
        setSupportActionBar(toolbar);
        actionBar.enableHomeButton(this);
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
        switch (id) {
            case R.id.action_accept_pur_req:
                CurrentUserManager.get(this).getId();
                updatePurchaseRequest(PurchaseRequestStatus.ACCEPTED.getStatus());
                performRequestUpdates();
                finish();
                return true;
            case R.id.action_reject_pur_req:
                updatePurchaseRequest(PurchaseRequestStatus.REJECTED.getStatus());
                performRequestUpdates();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updatePurchaseRequest(int status) {
        purchaseRequest.getSenderId();
        purchaseRequest.setPurchaseStatus(status);
        purchaseRequest.setId(purchaseRequest.getSenderId());
    }

    public void performRequestUpdates() {
        updateRequest(purchaseRequest, Constants.PURCHASE_REQUEST + "/" + CurrentUserManager.get(this).getId());
        updateRequest(purchaseRequest, Constants.PURCHASE_REQUEST + "/" + purchaseRequest.getSender());
    }


    private void initializeComponents() {
        Intent i = getIntent();
        purchaseRequest = i.getExtras().getParcelable("request");
        items = purchaseRequest.getPurchaseList();
        itemRequestAdapter = new ItemRequestAdapter(this, items);
        recyclerView = (RecyclerView) findViewById(R.id.purch_req_responce_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemRequestAdapter);
        recyclerView.addItemDecoration(new RequestsItemDivider(this));
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
