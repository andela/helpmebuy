package com.andela.helpmebuy.dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.utilities.PurchaseCreateCallback;

public class PurchaseRequestDialog extends DialogFragment {

    private Dialog dialog;

    private TextView purchaseName;

    private TextView purchaseDescription;

    private TextView purchaseCost;

    private Button cancelButton;

    private Button addButton;

    private PurchaseCreateCallback callback;

    public void setCallback(PurchaseCreateCallback callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.purchase_request_dialog);
        initViews();
        return dialog;
    }

    private void initViews() {
        purchaseName = (TextView) dialog.findViewById(R.id.purchase_name);
        purchaseDescription = (TextView) dialog.findViewById(R.id.purchase_desc);
        purchaseCost = (TextView) dialog.findViewById(R.id.purchase_cost);
        cancelButton = (Button) dialog.findViewById(R.id.btn_cancel_purchase);
        addButton = (Button) dialog.findViewById(R.id.btn_add_purchase);
        assignClickHandlers();
    }

    private void assignClickHandlers() {
        cancelButton.setOnClickListener(cancelListener);
        addButton.setOnClickListener(addListener);
    }

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PurchaseItem purchaseItem = getPurchaseFromViews();
            callback.onPurchaseCreated(purchaseItem);
            dialog.dismiss();
        }
    };

    private PurchaseItem getPurchaseFromViews() {
        PurchaseItem item = new PurchaseItem();
        item.setItemName(purchaseName.getText().toString());
        item.setItemDescription(purchaseDescription.getText().toString());
        item.setItemPrice(purchaseCost.getText().toString());
        return item;
    }
}
