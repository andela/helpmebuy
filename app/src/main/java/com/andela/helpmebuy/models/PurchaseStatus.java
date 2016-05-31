package com.andela.helpmebuy.models;

public enum PurchaseStatus {
    ACCEPTED(3),
    PENDING(2),
    REJECTED(1);
    private int status;

    PurchaseStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
