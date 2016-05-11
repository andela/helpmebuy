package com.andela.helpmebuy.models;

import java.util.ArrayList;

public class PurchaseRequest extends Model {
    private String sender;
    private String receiver;
    private ArrayList<PurchaseItem> purchaseList;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setPurchaseList(ArrayList<PurchaseItem> purchaseList) {
        this.purchaseList = purchaseList;
    }
}
