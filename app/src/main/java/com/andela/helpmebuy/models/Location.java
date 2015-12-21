package com.andela.helpmebuy.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private Country country;

    private Region region;

    private City city;

    private static Location location;

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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel location, int flag) {
        location.writeString(country.getId());
        location.writeString(country.getName());
        location.writeString(region.getId());
        location.writeString(region.getName());
        location.writeString(city.getId());
        location.writeString(city.getName());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            Country country = new Country();
            Region region = new Region();
            City city = new City();
            country.setId(in.readString());
            country.setName(in.readString());
            region.setId(in.readString());
            region.setName(in.readString());
            city.setId(in.readString());
            city.setName(in.readString());
            location = new Location(country, region, city);
            return location;
        }
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };


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

    public Location getLocation() {
        return location;
    }
}
