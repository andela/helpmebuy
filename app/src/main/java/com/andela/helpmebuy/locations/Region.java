package com.andela.helpmebuy.locations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel James on 10/28/2015.
 */
public class Region {

    private String name;

    private List<String> cities;

    public Region(String name) {
        this.name = name;

        cities = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
