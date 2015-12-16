package com.andela.helpmebuy.models;

import com.andela.helpmebuy.serializers.DateTimeDeserializer;
import com.andela.helpmebuy.serializers.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.time.DateTime;


/**
 * Represents a travel information.
 */
public class Travel extends Model {

    /**
     * The ID of the user who is travelling.
     */
    private String userId;

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

    private String departureLocationCode;

    /**
     * The date of arrival.
     */
    @JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
    private DateTime arrivalDate;

    /**
     * The arrival address.
     */
    private Address arrivalAddress;

    private String arrivalLocationCode;

    /**
     * Creates a new travel.
     */
    public Travel() {
    }

    /**
     * Creates a new travel with the specified ID.
     * @param id the ID of the travel.
     */
    public Travel(String id) {
        super(id);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getDepartureLocationCode() {
        return departureLocationCode;
    }

    public String getArrivalLocationCode() {
        return arrivalLocationCode;
    }

    public void setDepartureLocationCode(String departureLocationCode) {
        this.departureLocationCode = departureLocationCode;
    }

    public void setArrivalLocationCode(String arrivalLocationCode) {
        this.arrivalLocationCode = arrivalLocationCode;
    }
}
