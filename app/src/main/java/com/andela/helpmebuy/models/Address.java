package com.andela.helpmebuy.models;

import com.andela.helpmebuy.serializers.LocationDeserializer;
import com.andela.helpmebuy.serializers.LocationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents a simple address.
 */
public class Address extends Model {


    @JsonSerialize(using=LocationSerializer.class)
    @JsonDeserialize(using=LocationDeserializer.class)
    private Location location;
    /**
     * The country part of the address.
     */
    private String country;
    /**
     * The region part of the address.
     */
    private String region;
    /**
     * The city part of the address.
     */
    private  String city;
    /**
     * The street part of the address.
     */
    private String street;

    /**
     * The zipcode part of the address.
     */
    private String zipCode;

    /**
     * The phone number part of the address.
     */
    private String phoneNumber;

    /**
     * Creates a new address.
     */
    public Address(){
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
