package com.andela.helpmebuy.models;

<<<<<<< HEAD
/**
 * Created by Oluwatosin on 11/10/2015.
 */
public class City {
=======

public class City extends Model {

    private String regionId;

    private String countryId;

    private String name;

    public City() {
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
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
>>>>>>> feature-restructure-countries-data
}
