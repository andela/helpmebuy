package com.andela.helpmebuy.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A user of HelpMeBuy application.
 */
public class User {

    /**
     * The ID of the user.
     */
    private String id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The firstname of the user.
     */
    private String firsName;

    /**
     * The lastname of the user.
     */
    private String lastName;

    /**
     * The email addresss of the user.
     */
    private String email;

    /**
     * The residency address of the user.
     */
    private Address residencyAddress;

    /**
     * The list of delivery addresses of the user.
     */
    private List<Address> deliveryAddresses;

    /**
     * Creates a new user.
     */
    public User() {
        deliveryAddresses = new ArrayList<>();
    }

    /**
     * Creates a new user with the specified id.
     * @param id the ID of the newly created user.
     */
    public User(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getResidencyAddress() {
        return residencyAddress;
    }

    public void setResidencyAddress(Address residencyAddress) {
        this.residencyAddress = residencyAddress;
    }

    public List<Address> getDeliveryAddresses() {
        return deliveryAddresses;
    }

    public void setDeliveryAddresses(List<Address> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }
}
