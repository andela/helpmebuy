package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.PurchaseRequestHistoryAdapter;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.PurchaseRequestItemDivider;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity {
    private ArrayList<PurchaseRequest> purchaseList = new ArrayList<>();
    private PurchaseRequestHistoryAdapter purchaseRequestHistoryAdapter;
    private RecyclerView recyclerView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        intializeView();

    }

    public void intializeView() {
        userId = CurrentUserManager.get(this).getId();
        purchaseRequestHistoryAdapter = new PurchaseRequestHistoryAdapter(purchaseList);
        recyclerView = (RecyclerView) findViewById(R.id.purchase_history_list);
        recyclerView.setAdapter(purchaseRequestHistoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new PurchaseRequestItemDivider(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.purchase_history_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fetchPurchaseRequest();
    }

    public void fetchPurchaseRequest() {
        new FirebaseCollection<>(Constants.PURCHASE_REQUEST + "/" + userId, PurchaseRequest.class).getAll(new DataCallback<List<PurchaseRequest>>() {
            @Override
            public void onSuccess(List<PurchaseRequest> data) {
                for (PurchaseRequest purchaseRequest : data) {
                    purchaseList.add(purchaseRequest);
                }
                purchaseRequestHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
