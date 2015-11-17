package com.andela.helpmebuy.models;

public abstract class Location extends Model {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
