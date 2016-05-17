package com.andela.helpmebuy.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
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

    private TextInputLayout inputLayoutName;

    private TextInputLayout inputLayoutDescription;

    private TextInputLayout inputLayoutCost;

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
        inputLayoutName = (TextInputLayout) dialog.findViewById(R.id.input_layout_name);
        inputLayoutDescription = (TextInputLayout) dialog.findViewById(R.id.input_layout_description);
        inputLayoutCost = (TextInputLayout) dialog.findViewById(R.id.input_layout_cost);
        purchaseName = (TextView) dialog.findViewById(R.id.purchase_name);
        purchaseDescription = (TextView) dialog.findViewById(R.id.purchase_desc);
        purchaseCost = (TextView) dialog.findViewById(R.id.purchase_cost);
        cancelButton = (Button) dialog.findViewById(R.id.btn_cancel_purchase);
        purchaseName.addTextChangedListener(new MyTextWatcher(purchaseName));
        purchaseDescription.addTextChangedListener(new MyTextWatcher(purchaseDescription));
        purchaseCost.addTextChangedListener(new MyTextWatcher(purchaseCost));
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
            submitForm();
        }
    };

    private PurchaseItem getPurchaseFromViews() {
        PurchaseItem item = new PurchaseItem();
        item.setItemName(purchaseName.getText().toString());
        item.setItemDescription(purchaseDescription.getText().toString());
        item.setItemPrice(purchaseCost.getText().toString());
        return item;
    }

    private boolean validateName() {
        if (purchaseName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Item name cannot be empty");
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescription() {
        String description = purchaseDescription.getText().toString().trim();
        if (description.isEmpty()) {
            inputLayoutDescription.setError("Item description cannot be empty");
            return false;
        } else {
            inputLayoutDescription.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCost() {
        if (purchaseCost.getText().toString().trim().isEmpty()) {
            inputLayoutCost.setError("Item cost cannot be empty");
            return false;
        } else {
            inputLayoutCost.setErrorEnabled(false);
        }
        return true;
    }

    private void submitForm() {
        int check = 0;
        if (validateName()) {
            check++;
        }
        if (validateDescription()) {
            check++;
        }
        if (validateCost()) {
            check++;
        }
        if (check == 3) {
            PurchaseItem purchaseItem = getPurchaseFromViews();
            callback.onPurchaseCreated(purchaseItem);
            dialog.dismiss();
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.purchase_name:
                    validateName();
                    break;
                case R.id.purchase_desc:
                    validateDescription();
                    break;
                case R.id.purchase_cost:
                    validateCost();
                    break;
            }
        }
    }
}

