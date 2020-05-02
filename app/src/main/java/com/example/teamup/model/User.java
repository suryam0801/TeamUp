package com.example.teamup.model;
import android.os.Parcel;


public class User {



    public User(){

    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\n' +
                ", lastName='" + lastName + '\n' +
                ", contact='" + contact + '\n' +
                ", specialization='" + interests + '\n' +
                ", profileImageLink='" + profileImageLink + '\n' +
                ", location='" + location + '\n' +
                ", userId='" + userId + '\n' +
                ", createdProjects='" + createdProjects + '\n' +
                ", workingProjects='" + workingProjects + '\n' +
                ", completedProjects='" + completedProjects + '\n' +
                ", token_id='" + token_id + '\n' +
                '}';
    }

    public void setCreatedProjects(int createdProjects) {
        this.createdProjects = createdProjects;
    }

    public int getWorkingProjects() {
        return workingProjects;
    }

    public void setWorkingProjects(int workingProjects) {
        this.workingProjects = workingProjects;
    }

    public int getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(int completedProjects) {
        this.completedProjects = completedProjects;
    }

    private User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        contact = in.readString();
        interests = in.readString();
        location = in.readString();
        userId = in.readString();
        profileImageLink = in.readString();
        createdProjects = in.readInt();
        workingProjects = in.readInt();
        completedProjects = in.readInt();
        //Token id of a current user
        token_id = in.readString();
    }

    public User(String firstName, String lastName, String contact,
                String interests, String location, String userId, String profileImageLink,
                int createdProjects, int workingProjects, int completedProjects, String token_id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact=contact;
        this.interests = interests;
        this.location = location;
        this.userId = userId;
        this.profileImageLink=profileImageLink;
        this.createdProjects = createdProjects;
        this.workingProjects = workingProjects;
        this.completedProjects = completedProjects;
        this.token_id = token_id;

    }

    private String firstName;

    private String lastName;

    private String contact;

    private String interests;

    private String profileImageLink;

    private String location;

    private String userId;

    private int createdProjects;

    private int workingProjects;

    private int completedProjects;
    private String token_id;

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public int getCreatedProjects() {
        return createdProjects;
    }

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



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
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
    public String getContact() {
    return contact;
}

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
