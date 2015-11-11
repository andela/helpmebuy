package com.andela.helpmebuy.models;


public class Region extends Model {

    private String countryId;

    private String name;

    public Region() {
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
