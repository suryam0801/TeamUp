package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {
    private String projectName, projectId, from, state, date;
    private Long timestamp;

    public Notification () {

    }

    @Override
    public String toString() {
        return "Notification{" +
                "projectName='" + projectName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", from='" + from + '\'' +
                ", state='" + state + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public Notification(String projectName, String projectId, String from, String state, Long timestamp, String date) {
        this.projectName = projectName;
        this.projectId = projectId;
        this.from = from;
        this.state = state;
        this.timestamp = timestamp;
        this.date = date;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(projectName);
        parcel.writeString(projectId);
        parcel.writeString(from);
        parcel.writeString(state);
        parcel.writeLong(timestamp);
        parcel.writeString(date);
    }

    private Notification(Parcel in) {
        projectName = in.readString();
        projectId = in.readString();
        from = in.readString();
        state = in.readString();
        timestamp = in.readLong();
        date = in.readString();
    }

    public static final Parcelable.Creator<Notification> CREATOR
            = new Parcelable.Creator<Notification>() {
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
