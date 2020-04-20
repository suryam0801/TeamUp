package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

    private String projectId,applicantName,applicantEmail,userId,acceptedStatus,shortPitch;

    public Member() {
    }

    public Member(String projectId, String applicantName, String applicantEmail, String userId,String acceptedStatus, String shortPitch) {
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

    public String getMemberName() {
        return applicantName;
    }

    public void setMemberName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getMemberEmail() {
        return applicantEmail;
    }

    public void setMemberEmail(String applicantEmail) {
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

    private Member(Parcel in) {
        projectId = in.readString();
        applicantName = in.readString();
        applicantEmail = in.readString();
        userId = in.readString();
        acceptedStatus = in.readString();
        shortPitch = in.readString();
    }

    public static final Parcelable.Creator<Member> CREATOR
            = new Parcelable.Creator<Member>() {
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
