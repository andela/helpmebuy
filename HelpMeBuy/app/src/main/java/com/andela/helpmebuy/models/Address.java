package com.andela.helpmebuy.models;

/**
 * Represents a simple address.
 */
public class Address {

    /**
     * The city part of the address.
     */
    private String city;

    /**
     * The state part of the address.
     */
    private String state;

    /**
     * The country part of the address.
     */
    private String country;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
