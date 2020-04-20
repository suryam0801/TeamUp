package com.example.teamup.model;
import java.util.ArrayList;


public class User {

    public User(String firstName, String lastName, String email,
                String specialization, String secondarySkill, String location,
                String userId,String profileImageLink) {
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
        this.secondarySkill = secondarySkill;
        this.location = location;
        this.userId = userId;
        this.profileImageLink=profileImageLink;

    }

    private String firstName;

    private String lastName;

    private String email;

    private String specialization;

    private String secondarySkill;

    private String profileImageLink;

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", secondarySkill='" + secondarySkill + '\'' +
                ", Location='" + location + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    private String location;

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getSecondarySkill() {
        return secondarySkill;
    }

    public void setSecondarySkill(String secondarySkill) {
        this.secondarySkill = secondarySkill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        location = location;
    }

    private String userId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
