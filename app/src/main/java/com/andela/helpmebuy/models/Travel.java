package com.andela.helpmebuy.models;

import com.andela.helpmebuy.serializers.DateTimeDeserializer;
import com.andela.helpmebuy.serializers.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * Represents a travel information.
 */
public class Travel {

    /**
     * The ID of the travel.
     */
    private String id;

    /**
     * The user who is travelling.
     */
    private User user;

    /**
     * The date of departure.
     */
    @JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
    private DateTime departureDate;

    /**
     * The departure address.
     */
    private Address departureAddress;

    /**
     * The date of arrival.
     */
    private DateTime arrivalDate;

    /**
     * The arrival address.
     */
    private Address arrivalAddress;

    /**
     * Creates a new travel.
     */
    public Travel() {
    }

    /**
     * Creates a new travel with the specified ID.
     * @param id the ID of the travel.
     */
    public Travel(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(DateTime departureDate) {
        this.departureDate = departureDate;
    }

    public Address getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(Address departureAddress) {
        this.departureAddress = departureAddress;
    }

    public DateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(DateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Address getArrivalAddress() {
        return arrivalAddress;
    }

    public void setArrivalAddress(Address arrivalAddress) {
        this.arrivalAddress = arrivalAddress;
    }
}
