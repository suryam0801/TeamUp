package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

    private String projectId, memberName, memberEmail,userId,acceptedStatus,shortPitch, profilePicURL, specialization, location;

    public Member() {
    }

    @Override
    public String toString() {
        return "Member{" +
                "projectId='" + projectId + '\'' +
                ", applicantName='" + memberName + '\'' +
                ", applicantEmail='" + memberEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", acceptedStatus='" + acceptedStatus + '\'' +
                ", shortPitch='" + shortPitch + '\'' +
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

    public Member(String projectId, String applicantName, String applicantEmail, String userId, String acceptedStatus, String shortPitch, String profilePicURL, String primarySkill, String location) {
        this.projectId = projectId;
        this.memberName = applicantName;
        this.memberEmail = applicantEmail;
        this.userId = userId;
        this.acceptedStatus = acceptedStatus;
        this.shortPitch = shortPitch;
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String applicantName) {
        this.memberName = applicantName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String applicantEmail) {
        this.memberEmail = applicantEmail;
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
        parcel.writeString(memberName);
        parcel.writeString(memberEmail);
        parcel.writeString(userId);
        parcel.writeString(acceptedStatus);
        parcel.writeString(shortPitch);
        parcel.writeString(profilePicURL);
        parcel.writeString(specialization);
        parcel.writeString(location);
    }

    private Member(Parcel in) {
        projectId = in.readString();
        memberName = in.readString();
        memberEmail = in.readString();
        userId = in.readString();
        acceptedStatus = in.readString();
        shortPitch = in.readString();
        profilePicURL = in.readString();
        specialization = in.readString();
        location = in.readString();
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
