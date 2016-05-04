package com.andela.helpmebuy.utilities;


import com.andela.helpmebuy.models.PurchaseItem;

public interface PurchaseCreateCallback {
    void onPurchaseCreated(PurchaseItem purchaseItem);
}
