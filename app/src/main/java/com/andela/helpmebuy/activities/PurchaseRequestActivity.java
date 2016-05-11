package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class PurchaseRequestActivity extends AppCompatActivity implements ItemDeleteListener {
    private RecyclerView itemView;
    private ArrayList<PurchaseItem> items;
    private PurchaseRequestAdapter purchaseRequestAdapter;
    private String receiverId;
    TextView requestReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request);
        initializeComponents();
        displayText();
        receiverId = getIntent().getExtras().getString("userId");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddDialog();
            }
        });
        showSnackBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_purchase_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send_purchase_request) {
            createPurchaseRequest();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        purchaseRequestAdapter = new PurchaseRequestAdapter(this, items, this);
        itemView.setLayoutManager(new LinearLayoutManager(this));
        itemView.addItemDecoration(new RequestsItemDivider(this));
        itemView.setAdapter(purchaseRequestAdapter);
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
        createQuery(purchaseRequest, senderId, receiverId);
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
            public void onSuccess(PurchaseRequest data) {
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }

    public void showSnackBar() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.purchase_request);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Tap the button with the plus sign to add a new item", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void displayText() {
        String name = getIntent().getExtras().getString("name");
        String[] firstName = name.split(" ");
        if (firstName.length > 1) {
            requestReceiver.setText("Hi " + firstName[1] + " I'd like you to help me buy this items");
        }
        requestReceiver.setText("Hi " + name + " I'd like you to help me buy this items");
    }
}