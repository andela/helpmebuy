package com.andela.helpmebuy.models;


public class Connection extends Model{

    private int connectionStatus;

    private String message;

    private String receiver;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Connection(){
    }

    public Connection(User user, int connectionStatus){
        this.connectionStatus = connectionStatus;
    }

    public int getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

}
