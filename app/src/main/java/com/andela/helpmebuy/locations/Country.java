package com.andela.helpmebuy.locations;

import com.andela.helpmebuy.models.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel James on 10/28/2015.
 */
public class Country extends Model {
    private String name;

    private List<Region> states;

    public Country(String name) {
        this.name = name;

        states = new ArrayList<Region>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Region> getStates() {
        return states;
    }

    public void setStates(List<Region> states) {
        this.states = states;
    }
}
