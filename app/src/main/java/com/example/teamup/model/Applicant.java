package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Applicant implements Parcelable {

    private String projectId,applicantName,applicantEmail,userId,acceptedStatus,shortPitch, profilePicURL, specialization, location;
    private int workingProject;

    public Applicant() {
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "projectId='" + projectId + '\'' +
                ", applicantName='" + applicantName + '\'' +
                ", applicantEmail='" + applicantEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", acceptedStatus='" + acceptedStatus + '\'' +
                ", shortPitch='" + shortPitch + '\'' +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", primarySkill='" + specialization + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public Applicant(String projectId, String applicantName, String applicantEmail, String userId, String acceptedStatus,
                     String shortPitch, String profilePicURL, String primarySkill, String location, int workingProject) {

        this.projectId = projectId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.userId = userId;
        this.acceptedStatus = acceptedStatus;
        this.shortPitch = shortPitch;
        this.profilePicURL = profilePicURL;
        this.specialization = primarySkill;
        this.location = location;
        this.workingProject = workingProject;
    }

    public int getWorkingProject() {
        return workingProject;
    }

    public void setWorkingProject(int workingProject) {
        this.workingProject = workingProject;
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

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcceptedStatus() {
        return acceptedStatus;
    }

    public void setAcceptedStatus(String acceptedStatus) {
        this.acceptedStatus = acceptedStatus;
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
        parcel.writeString(applicantEmail);
        parcel.writeString(userId);
        parcel.writeString(acceptedStatus);
        parcel.writeString(shortPitch);
        parcel.writeString(profilePicURL);
        parcel.writeString(specialization);
        parcel.writeString(location);
        parcel.writeInt(workingProject);
    }

    private Applicant(Parcel in) {
        projectId = in.readString();
        applicantName = in.readString();
        applicantEmail = in.readString();
        userId = in.readString();
        acceptedStatus = in.readString();
        shortPitch = in.readString();
        profilePicURL = in.readString();
        specialization = in.readString();
        location = in.readString();
        workingProject = in.readInt();
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
