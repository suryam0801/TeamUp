package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Applicant implements Parcelable {

    private String projectId,applicantName, applicantPhn,userId,shortPitch, profilePicURL, locationTags, interestTags;
    private long workingProject;

    public Applicant() {
    }

    public Applicant(String projectId, String applicantName, String applicantPhn, String userId, String shortPitch,
                     String profilePicURL, String locationTags, String interestTags, long workingProject) {
        this.projectId = projectId;
        this.applicantName = applicantName;
        this.applicantPhn = applicantPhn;
        this.userId = userId;
        this.shortPitch = shortPitch;
        this.profilePicURL = profilePicURL;
        this.locationTags = locationTags;
        this.interestTags = interestTags;
        this.workingProject = workingProject;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "projectId='" + projectId + '\'' +
                ", applicantName='" + applicantName + '\'' +
                ", applicantPhn='" + applicantPhn + '\'' +
                ", userId='" + userId + '\'' +
                ", shortPitch='" + shortPitch + '\'' +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", locationTags=" + locationTags +
                ", interestTags=" + interestTags +
                ", workingProject=" + workingProject +
                '}';
    }

    public String getLocationTags() {
        return locationTags;
    }

    public void setLocationTags(String locationTags) {
        this.locationTags = locationTags;
    }

    public String getInterestTags() {
        return interestTags;
    }

    public void setInterestTags(String interestTags) {
        this.interestTags = interestTags;
    }

    public long getWorkingProject() {
        return workingProject;
    }

    public void setWorkingProject(long workingProject) {
        this.workingProject = workingProject;
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

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantPhn() {
        return applicantPhn;
    }

    public void setApplicantPhn(String applicantPhn) {
        this.applicantPhn = applicantPhn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShortPitch() {
        return shortPitch;
    }

    public void setShortPitch(String shortPitch) {
        this.shortPitch = shortPitch;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(projectId);
        parcel.writeString(applicantName);
        parcel.writeString(applicantPhn);
        parcel.writeString(userId);
        parcel.writeString(shortPitch);
        parcel.writeString(profilePicURL);
        parcel.writeString(locationTags);
        parcel.writeString(interestTags);
        parcel.writeLong(workingProject);
    }

    private Applicant(Parcel in) {
        projectId = in.readString();
        applicantName = in.readString();
        applicantPhn = in.readString();
        userId = in.readString();
        shortPitch = in.readString();
        profilePicURL = in.readString();
        locationTags = in.readString();
        interestTags = in.readString();
        workingProject = in.readLong();
    }

    public static final Parcelable.Creator<Applicant> CREATOR
            = new Parcelable.Creator<Applicant>() {
        public Applicant createFromParcel(Parcel in) {
            return new Applicant(in);
        }

        public Applicant[] newArray(int size) {
            return new Applicant[size];
        }
    };
}
