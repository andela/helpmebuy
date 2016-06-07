package com.andela.helpmebuy.models;

import java.util.ArrayList;

public class PurchaseRequest extends Model {
    private int purchaseStatus;
    private String sender;
    private String receiver;
    private ArrayList<PurchaseItem> purchaseList;
    private String date;
    private String receiverFullname;

    public String getReceiverFullname() {
        return receiverFullname;
    }

    public void setReceiverFullname(String receiverFullname) {
        this.receiverFullname = receiverFullname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public ArrayList<PurchaseItem> getPurchaseList() {
        return purchaseList;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setPurchaseList(ArrayList<PurchaseItem> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public int getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(int purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }
}
