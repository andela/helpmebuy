package com.andela.helpmebuy.models;

import java.util.ArrayList;

public class PurchaseRequest extends Model{
    private String sender;
    private String receiver;
    private ArrayList<PurchaseItem> purchaseList;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ArrayList<PurchaseItem> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(ArrayList<PurchaseItem> purchaseList) {
        this.purchaseList = purchaseList;
    }
}
