package com.andela.helpmebuy.models;

/**
 * Created by andela-jugba on 2/22/16.
 */
public class Connection extends Model{
    private User user;
    private int connectionStatus;

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
