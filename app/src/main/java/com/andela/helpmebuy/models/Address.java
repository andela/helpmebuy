package com.andela.helpmebuy.models;

/**
 * Represents a simple address.
 */
public class Address extends Model {

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
