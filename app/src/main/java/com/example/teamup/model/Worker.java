package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Worker implements Parcelable {

    private String projectId, workerName,userId, profilePicURL;
    private List<String> locationTags, interestTags;

    public Worker() {
    }

    public Worker(String projectId, String workerName, String userId, String profilePicURL, List<String> locationTags, List<String> interestTags) {
        this.projectId = projectId;
        this.workerName = workerName;
        this.userId = userId;
        this.profilePicURL = profilePicURL;
        this.locationTags = locationTags;
        this.interestTags = interestTags;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "projectId='" + projectId + '\'' +
                ", workerName='" + workerName + '\'' +
                ", userId='" + userId + '\'' +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", locationTags=" + locationTags +
                ", interestTags=" + interestTags +
                '}';
    }

    public List<String> getLocationTags() {
        return locationTags;
    }

    public void setLocationTags(List<String> locationTags) {
        this.locationTags = locationTags;
    }

    public List<String> getInterestTags() {
        return interestTags;
    }

    public void setInterestTags(List<String> interestTags) {
        this.interestTags = interestTags;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
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
        parcel.writeStringList(locationTags);
        parcel.writeStringList(interestTags);
    }

    private Worker(Parcel in) {
        projectId = in.readString();
        workerName = in.readString();
        userId = in.readString();
        profilePicURL = in.readString();
        in.readStringList(locationTags);
        in.readStringList(interestTags);
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
