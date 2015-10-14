package com.andela.helpmebuy.models;


public abstract class Model {

    private String id;

    protected Model() {
    }

    protected Model(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
