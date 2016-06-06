package com.andela.helpmebuy.models;

/**
 * Created by andeladev on 05/06/2016.
 */
public enum PurchaseRequestStatus {
    ACCEPTED(3),
    PENDING(2),
    REJECTED(1),
    BLOCKED(0);

    private int status;

    PurchaseRequestStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}