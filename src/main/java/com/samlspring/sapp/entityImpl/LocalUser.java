package com.samlspring.sapp.entityImpl;

import java.util.List;

public class LocalUser {

    private final String ID;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String locale;
    private final List Roles;


    public LocalUser(String samlID, String username, String firstName, String lastName, String email, String locale, List roles) {
        this.ID = samlID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.locale = locale;
        Roles = roles;
    }

    public String getID() { return ID; }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLocale() {
        return locale;
    }

    public List getRoles() {
        return Roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", locale='" + locale + '\'' +
                ", Roles=" + Roles +
                '}';
    }
}
