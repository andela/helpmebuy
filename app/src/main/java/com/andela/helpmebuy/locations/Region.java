package com.andela.helpmebuy.locations;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private String name;

    private List<String> cities;

    public Region(String name) {
        this.name = name;

        cities = new ArrayList<>();
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
