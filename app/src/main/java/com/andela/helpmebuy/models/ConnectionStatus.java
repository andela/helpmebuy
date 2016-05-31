package com.andela.helpmebuy.models;

public enum ConnectionStatus {
    ACCEPTED(3),
    PENDING(2),
    REJECTED(1),
    BLOCKED(0);

    private int status;

    ConnectionStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
