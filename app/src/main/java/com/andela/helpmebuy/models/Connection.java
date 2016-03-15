package com.andela.helpmebuy.models;


public class Connection extends Model{
    private User user;

    private int connectionStatus;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Connection(){
    }

    public Connection(User user, int connectionStatus){
        this.user = user;
        this.connectionStatus = connectionStatus;
    }

    public int getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
