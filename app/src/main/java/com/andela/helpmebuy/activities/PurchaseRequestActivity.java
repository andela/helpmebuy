package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView requestReceiver;
    private TextView instruction;
    private Menu menu;
    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request);
        initializeComponents();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        menuItem = menu.findItem(R.id.action_send_purchase_request);
        menuItem.setEnabled(false);
        return true;
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
            showIndicator();
        }
    };

    private void initializeComponents() {
        items = new ArrayList<>();
        itemView = (RecyclerView) findViewById(R.id.purchase_requests_view);
        instruction = (TextView) findViewById(R.id.item_indicator);
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
        showIndicator();
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
                Toast.makeText(getBaseContext(),"Purchase request sent",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }

    public void displayText() {
        requestReceiver.setVisibility(View.VISIBLE);
        String receiversName = getIntent().getExtras().getString("name");
        String[] receiversFullName = receiversName.split(" ");
        if (receiversFullName.length > 1) {
            requestReceiver.setText("Hi " + receiversFullName[0] + " I'd like you to help me buy this item(s)");
        } else {
            requestReceiver.setText("Hi " + receiversName + " I'd like you to help me buy this item(s)");
        }
    }

    public void showIndicator() {
        if (items.size() > 0) {
            menuItem.setEnabled(true);
            displayText();
            instruction.setVisibility(View.GONE);
        }
        else {
            menuItem.setEnabled(false);
            instruction.setVisibility(View.VISIBLE);
            requestReceiver.setVisibility(View.INVISIBLE);
        }
    }
}