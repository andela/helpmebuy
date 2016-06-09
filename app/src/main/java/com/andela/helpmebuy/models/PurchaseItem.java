package com.andela.helpmebuy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PurchaseItem implements Parcelable{
    private String itemName;
    private String itemPrice;
    private String itemDescription;
    private String itemQuantity;

    public PurchaseItem() {

    }

    protected PurchaseItem(Parcel in) {
        itemName = in.readString();
        itemPrice = in.readString();
        itemDescription = in.readString();
        itemQuantity = in.readString();
    }

    public static final Creator<PurchaseItem> CREATOR = new Creator<PurchaseItem>() {
        @Override
        public PurchaseItem createFromParcel(Parcel in) {
            return new PurchaseItem(in);
        }

        @Override
        public PurchaseItem[] newArray(int size) {
            return new PurchaseItem[size];
        }
    };

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemPrice);
        dest.writeString(itemDescription);
        dest.writeString(itemQuantity);
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
