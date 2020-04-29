package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Applicant implements Parcelable {

    private String projectId,applicantName, applicantPhn,userId,shortPitch, profilePicURL, specialization, location;
    private long workingProject;

    public Applicant() {
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "projectId='" + projectId + '\n' +
                ", applicantName='" + applicantName + '\n' +
                ", applicantPhn='" + applicantPhn + '\n' +
                ", userId='" + userId + '\n' +
                ", shortPitch='" + shortPitch + '\n' +
                ", profilePicURL='" + profilePicURL + '\n' +
                ", specialization='" + specialization + '\n' +
                ", location='" + location + '\n' +
                ", workingProject=" + workingProject +
                '}';
    }

    public Applicant(String projectId, String applicantName, String applicantEmail, String userId, String acceptedStatus,
                     String shortPitch, String profilePicURL, String primarySkill, String location, long workingProject) {

        this.projectId = projectId;
        this.applicantName = applicantName;
        this.applicantPhn = applicantEmail;
        this.userId = userId;
        this.shortPitch = shortPitch;
        this.profilePicURL = profilePicURL;
        this.specialization = primarySkill;
        this.location = location;
        this.workingProject = workingProject;
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
        parcel.writeString(specialization);
        parcel.writeString(location);
        parcel.writeLong(workingProject);
    }

    private Applicant(Parcel in) {
        projectId = in.readString();
        applicantName = in.readString();
        applicantPhn = in.readString();
        userId = in.readString();
        shortPitch = in.readString();
        profilePicURL = in.readString();
        specialization = in.readString();
        location = in.readString();
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
