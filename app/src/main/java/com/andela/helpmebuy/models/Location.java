package com.andela.helpmebuy.models;


public class Location {

    private Country country;

    private Region region;

    private City city;

    public Location() {
    }

    public Location(Country country) {
        this.country = country;
    }

    public Location(Country country, Region region, City city) {
        this(country);
        this.region = region;
        this.city = city;
    }

    public Location(Country country, City city) {
        this(country);
        this.city = city;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (city != null) {
            builder.append(city.getName());
        }

        if (region != null) {
            builder.append(", ");
            builder.append(region.getName());
        }

        if (country != null) {
            builder.append(", ");
            builder.append(country.getName());
        }

        return builder.toString();
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String toFullString(){
        return getCountry().getId() + ":" + getCountry().getName() +
                ";" + getRegion().getId() + ":" + getRegion().getName() +
                ";" + getCity().getId() + ":" + getCity().getName();

    }
}
