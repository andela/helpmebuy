package com.andela.helpmebuy.models;

/**
 * Created by andela-jugba on 2/22/16.
 */
public enum ConnectionStatus {
    ACCEPTED(3),
    PENDING(2),
    REJECTED(1),
    BLOCKED(0);

    private int status;

    ConnectionStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
