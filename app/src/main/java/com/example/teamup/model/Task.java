package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String taskName;
    private String taskDescription;
    private String taskID;
    private String priority;
    private String taskStatus;
    private String creatorId;

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskID='" + taskID + '\'' +
                ", priority='" + priority + '\'' +
                ", creatorId='" + creatorId + '\'' +
                '}';
    }

    public Task(String taskName, String taskDescription, String taskId, String priority, String creatorId, String taskStatus){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskId;
        this.priority = priority;
        this.creatorId = creatorId;
        this.taskStatus = taskStatus;
    }

    public Task(){

    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(taskName);
        parcel.writeString(taskDescription);
        parcel.writeString(taskID);
        parcel.writeString(priority);
        parcel.writeString(creatorId);
        parcel.writeString(taskStatus);
    }

    private Task(Parcel in) {
        taskName = in.readString();
        taskDescription = in.readString();
        taskID = in.readString();
        priority = in.readString();
        creatorId = in.readString();
        taskStatus = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR
            = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
