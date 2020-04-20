package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String taskName;
    private String taskDescription;
    private String taskID;

    public Task(String taskName, String taskDescription, String taskId){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskId;
    }

    public Task(){

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
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\n' +
                ", taskDescription='" + taskDescription + '\n' +
                ", taskID='" + taskID + '\n' +
                '}';
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
    }

    private Task(Parcel in) {
        taskName = in.readString();
        taskDescription = in.readString();
        taskID = in.readString();
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
