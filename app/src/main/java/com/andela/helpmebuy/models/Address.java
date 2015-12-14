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


}
