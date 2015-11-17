package com.andela.helpmebuy.models;

public class Region extends Location {

    private String countryId;

    public Region() {
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
