package com.andela.helpmebuy.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PurchaseRequest extends Model implements Parcelable {
    private String sender;
    private String receiver;
    private ArrayList<PurchaseItem> purchaseList;
    private int purchaseStatus;
    private String receiverFullname;
    private String sendersFullName;
    private String senderId;

    public String getSendersFullName() {
        return sendersFullName;
    }

    public void setSendersFullName(String sendersFullName) {
        this.sendersFullName = sendersFullName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    private Long dateMillis;


    public PurchaseRequest() {
    }

    protected PurchaseRequest(Parcel in) {
        sender = in.readString();
        receiver = in.readString();
        purchaseList = in.createTypedArrayList(PurchaseItem.CREATOR);
        purchaseStatus = in.readInt();
        receiverFullname = in.readString();
        sendersFullName = in.readString();
        dateMillis = in.readLong();
        senderId = in.readString();

    }

    public static final Creator<PurchaseRequest> CREATOR = new Creator<PurchaseRequest>() {
        @Override
        public PurchaseRequest createFromParcel(Parcel in) {
            return new PurchaseRequest(in);
        }

        @Override
        public PurchaseRequest[] newArray(int size) {
            return new PurchaseRequest[size];
        }
    };

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

    public Long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(Long dateMillis) {
        this.dateMillis = dateMillis;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(receiver);
        dest.writeTypedList(purchaseList);
        dest.writeInt(purchaseStatus);
        dest.writeString(receiverFullname);
        dest.writeString(sendersFullName);
        dest.writeLong(dateMillis);
        dest.writeString(senderId);
    }

    public ArrayList<PurchaseItem> getPurchaseList() {
        return purchaseList;
    }
}
