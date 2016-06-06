package com.andela.helpmebuy.models;

import java.util.ArrayList;

public class PurchaseRequest extends Model {
    private String sender;
    private String receiver;
    private ArrayList<PurchaseItem> purchaseList;
    private int purchaseStatus;
    private String receiverFullname;
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<PurchaseItem> getPurchaseList() {
        return purchaseList;
    }

    public void setReceiverFullname(String receiverFullname) {
        this.receiverFullname = receiverFullname;
    }

    public String getReceiverFullname() {
        return receiverFullname;
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

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }


    public void setPurchaseStatus(int purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public int getPurchaseStatus() {
        return purchaseStatus;
    }
}
