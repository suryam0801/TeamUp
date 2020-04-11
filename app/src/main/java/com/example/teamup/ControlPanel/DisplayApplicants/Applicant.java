package com.example.teamup.ControlPanel.DisplayApplicants;

import android.os.Parcel;
import android.os.Parcelable;

public class Applicant implements Parcelable {

    private String projectId,applicantName,applicantEmail,userId,acceptedStatus,shortPitch;

    public Applicant() {
    }

    public Applicant(String projectId, String applicantName, String applicantEmail, String userId,String acceptedStatus, String shortPitch) {
        this.projectId = projectId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.userId = userId;
        this.acceptedStatus = acceptedStatus;
        this.shortPitch = shortPitch;
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
    public String toString() {
        return "Applicant{" +
                "applicantName='" + applicantName + '\'' +
                ", applicantEmail='" + applicantEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", acceptedStatus=" + acceptedStatus +
                ", shortPitch='" + shortPitch + '\'' +
                '}';
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
    }

    private Applicant(Parcel in) {
        projectId = in.readString();
        applicantName = in.readString();
        applicantEmail = in.readString();
        userId = in.readString();
        acceptedStatus = in.readString();
        shortPitch = in.readString();
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
