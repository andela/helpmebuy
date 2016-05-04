package com.andela.helpmebuy.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.dialogs.PurchaseRequestDialog;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.utilities.PurchaseCreateCallback;

public class PurchaseRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request);
        String name = getIntent().getExtras().getString("name");
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
            Toast.makeText(PurchaseRequestActivity.this, purchaseItem.getItem(), Toast.LENGTH_LONG)
                    .show();
        }
    };

}
