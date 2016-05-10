package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.andela.helpmebuy.utilities.RequestsItemDivider;

import java.util.ArrayList;
import java.util.UUID;

;

public class PurchaseRequestActivity extends AppCompatActivity implements ItemDeleteListener, View.OnClickListener {
    private RecyclerView itemView;
    private ArrayList<PurchaseItem> items;
    private PurchaseRequestAdapter purchaseRequestAdapter;
    private Button sendButton;
    private String receiverId;
    TextView requestReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request);
        initializeComponents();
        String name = getIntent().getExtras().getString("name");
        requestReceiver.setText(name);
        receiverId = getIntent().getExtras().getString("userId");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    private void launchAddDialog() {
        PurchaseRequestDialog dialog = new PurchaseRequestDialog();
        dialog.setCallback(purchaseCreateCallback);
        dialog.show(getSupportFragmentManager(), "addPurchase");
    }

    private PurchaseCreateCallback purchaseCreateCallback = new PurchaseCreateCallback() {
        @Override
        public void onPurchaseCreated(PurchaseItem purchaseItem) {
            items.add(purchaseItem);
            purchaseRequestAdapter.notifyDataSetChanged();
        }
    };

    private void initializeComponents() {
        items = new ArrayList<>();
        itemView = (RecyclerView) findViewById(R.id.purchase_requests_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.purchase_toolbar);
        requestReceiver = (TextView) findViewById(R.id.purchase_request_recipient);
        itemView.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        purchaseRequestAdapter = new PurchaseRequestAdapter(this, items, this);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        itemView.addItemDecoration(new RequestsItemDivider(this));
        itemView.setAdapter(purchaseRequestAdapter);
        sendButton = (Button) findViewById(R.id.send_request);
        sendButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        createPurchaseRequest();
        finish();
    }
}