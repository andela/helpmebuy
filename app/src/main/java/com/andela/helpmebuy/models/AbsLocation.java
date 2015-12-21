package com.andela.helpmebuy.models;

public abstract class AbsLocation extends Model {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
