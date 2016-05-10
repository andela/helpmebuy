package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.PurchaseRequestAdapter;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.dialogs.PurchaseRequestDialog;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.ItemDeleteListener;
import com.andela.helpmebuy.utilities.PurchaseCreateCallback;
import com.andela.helpmebuy.views.CustomRecyclerView;

import java.util.ArrayList;
import java.util.UUID;

public class PurchaseRequestActivity extends AppCompatActivity implements ItemDeleteListener {
    private CustomRecyclerView itemView;
    private ArrayList<String> items;
    private PurchaseRequestAdapter purchaseRequestAdapter;
    private Button sendButton;
    private String receiverId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request);
        initializeComponents();
        String name = getIntent().getExtras().getString("name");
        receiverId = getIntent().getExtras().getString("userId");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddDialog();
            }
        });
    }

    private void launchAddDialog() {
        PurchaseRequestDialog dialog = new PurchaseRequestDialog();
        dialog.setCallback(purchaseCreateCallback);
        dialog.show(getSupportFragmentManager(), "addPurchase");
    }

    private PurchaseCreateCallback purchaseCreateCallback = new PurchaseCreateCallback() {
        @Override
        public void onPurchaseCreated(PurchaseItem purchaseItem) {
            items.add(purchaseItem.getItem());
            purchaseRequestAdapter.notifyDataSetChanged();
        }
    };

    private void initializeComponents() {
        items = new ArrayList<>();
        itemView = (CustomRecyclerView) findViewById(R.id.purchase_requests_view);
        purchaseRequestAdapter = new PurchaseRequestAdapter(this, items, this);
        itemView.setAdapter(purchaseRequestAdapter);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        sendButton = (Button) findViewById(R.id.send_request);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPurchaseRequest();
            }
        });
    }

    @Override
    public void deleteSelection(View v, int position) {
        items.remove(position);
        purchaseRequestAdapter.notifyDataSetChanged();
    }

    public void createPurchaseRequest() {
        String id = UUID.randomUUID().toString();
        String senderId = CurrentUserManager.get(this).getId();
        String receiverId = this.receiverId;
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setId(id);
        purchaseRequest.setPurchaseList(items);
        purchaseRequest.setReceiver(receiverId);
        purchaseRequest.setSender(senderId);
        createQuery(purchaseRequest,senderId,receiverId);
    }

    private void createQuery(PurchaseRequest purchaseRequest, String senderId, String receiverId) {
        sendRequest(purchaseRequest, senderId);
        sendRequest(purchaseRequest, receiverId);

    }

    private void sendRequest(PurchaseRequest purchaseRequest, String id) {
        FirebaseCollection<PurchaseRequest> firebaseCollection
                = new FirebaseCollection<PurchaseRequest>("purchaseRequest/" + id, PurchaseRequest.class);
        firebaseCollection.save(purchaseRequest, new DataCallback<PurchaseRequest>() {
            @Override
            public void onSuccess(PurchaseRequest data) {}

            @Override
            public void onError(String errorMessage) {}
        });
    }
}