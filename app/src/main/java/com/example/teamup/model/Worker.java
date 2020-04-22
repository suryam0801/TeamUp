package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Worker implements Parcelable {

    private String projectId, workerName,userId, profilePicURL, specialization, location;

    public Worker() {
    }

    @Override
    public String toString() {
        return "Member{" +
                "projectId='" + projectId + '\'' +
                ", applicantName='" + workerName + '\'' +
                ", userId='" + userId + '\'' +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", primarySkill='" + specialization + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Worker(String projectId, String applicantName, String userId, String profilePicURL, String primarySkill, String location) {
        this.projectId = projectId;
        this.workerName = applicantName;
        this.userId = userId;
        this.profilePicURL = profilePicURL;
        this.specialization = primarySkill;
        this.location = location;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String applicantName) {
        this.workerName = applicantName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(projectId);
        parcel.writeString(workerName);
        parcel.writeString(userId);
        parcel.writeString(profilePicURL);
        parcel.writeString(specialization);
        parcel.writeString(location);
    }

    private Worker(Parcel in) {
        projectId = in.readString();
        workerName = in.readString();
        userId = in.readString();
        profilePicURL = in.readString();
        specialization = in.readString();
        location = in.readString();
    }

    public static final Parcelable.Creator<Worker> CREATOR
            = new Parcelable.Creator<Worker>() {
        public Worker createFromParcel(Parcel in) {
            return new Worker(in);
        }

        public Worker[] newArray(int size) {
            return new Worker[size];
        }
    };
}
