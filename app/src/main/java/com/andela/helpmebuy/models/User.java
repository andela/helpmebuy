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
     * The fullname of the user.
     */
    private String fullName;

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
     * The list of users that a user is connected to.
     */
    private List<User> connections;

    /**
     * the url for the user profile picture
     */
    private String profilePictureUrl;

    /**
     * Creates a new user.
     */

    public User() {
        deliveryAddresses = new ArrayList<>();
        connections = new ArrayList<>();
    }

    /**
     * Creates a new user with the specified id.
     * @param id the ID of the newly created user.
     */
    public User(String id) {
        this();
        this.id = id;
    }

    public User(String id, String fullName) {
        this(id);
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public List<User> getConnections() {
        return connections;
    }

    public void setConnections(List<User> connections) {
        this.connections = connections;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
