package com.andela.helpmebuy.models;


public class Connection extends Model {

    private int connectionStatus;
    private String message;
    private String receiver;
    private String sender;

    public Connection() {
    }

    public Connection(int connectionStatus) {
        this.connectionStatus = connectionStatus;
        this.message = "would like to connect with you.";
    }

    public int getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
