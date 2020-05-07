package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Broadcast implements Parcelable{

    private String creatorId;

    private String creatorEmail;

    private String creatorName;

    private String broadcastId;

    private String broadcastName;

    private String broadcastDescription;

    private String category;

    private int newTasks;

    private int newApplicants;

    private List<String> interestTags =new ArrayList<>();

    private List<String> locationTags = new ArrayList<>();

    public Broadcast(String creatorId, String creatorEmail, String creatorName, String broadcastId, String broadcastName, String broadcastDescription, String category, int newTasks, int newApplicants, List<String> interestTags, List<String> locationTags, List<Applicant> applicantList, List<String> applicantId, List<String> workersId, List<Worker> workersList, List<Task> taskList, String broadcastStatus) {
        this.creatorId = creatorId;
        this.creatorEmail = creatorEmail;
        this.creatorName = creatorName;
        this.broadcastId = broadcastId;
        this.broadcastName = broadcastName;
        this.broadcastDescription = broadcastDescription;
        this.category = category;
        this.newTasks = newTasks;
        this.newApplicants = newApplicants;
        this.interestTags = interestTags;
        this.locationTags = locationTags;
        this.applicantList = applicantList;
        this.applicantId = applicantId;
        this.workersId = workersId;
        this.workersList = workersList;
        this.taskList = taskList;
        this.broadcastStatus = broadcastStatus;
    }

    private List<Applicant> applicantList=new ArrayList<>();
    private List<String> applicantId=new ArrayList<>();
    private List<String> workersId = new ArrayList<>();
    private List<Worker> workersList =new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();

    public int getNewTasks() {
        return newTasks;
    }

    public void setNewTasks(int newTasks) {
        this.newTasks = newTasks;
    }

    public int getNewApplicants() {
        return newApplicants;
    }

    public void setNewApplicants(int newApplicants) {
        this.newApplicants = newApplicants;
    }

    private String broadcastStatus;

    protected Broadcast(Parcel in) {
        creatorId = in.readString();
        creatorEmail = in.readString();
        creatorName = in.readString();
        broadcastId = in.readString();
        broadcastName = in.readString();
        broadcastDescription = in.readString();
        newTasks = in.readInt();
        newApplicants = in.readInt();
        interestTags = in.createStringArrayList();
        broadcastStatus = in.readString();
        category = in.readString();
        applicantId = in.createStringArrayList();
        workersId = in.createStringArrayList();
        in.readStringList(locationTags);
        in.readStringList(interestTags);
        in.readTypedList(applicantList, Applicant.CREATOR);
        in.readTypedList(taskList, Task.CREATOR);
        in.readTypedList(workersList, Worker.CREATOR);
    }

    public static final Creator<Broadcast> CREATOR = new Creator<Broadcast>() {
        @Override
        public Broadcast createFromParcel(Parcel in) {
            return new Broadcast(in);
        }

        @Override
        public Broadcast[] newArray(int size) {
            return new Broadcast[size];
        }
    };

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getBroadcastName() {
        return broadcastName;
    }

    public void setBroadcastName(String broadcastName) {
        this.broadcastName = broadcastName;
    }

    public String getBroadcastDescription() {
        return broadcastDescription;
    }

    public void setBroadcastDescription(String broadcastDescription) {
        this.broadcastDescription = broadcastDescription;
    }


    public List<String> getInterestTags() {
        return interestTags;
    }

    public void setInterestTags(List<String> interestTags) {
        this.interestTags = interestTags;
    }


    public List<String> getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(List<String> applicantId) {
        this.applicantId = applicantId;
    }

    public List<Applicant> getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(List<Applicant> applicantList) {
        this.applicantList = applicantList;
    }

    public List<Worker> getWorkersList() {
        return workersList;
    }
    public void setWorkersList(List<Worker> workersList) {
        this.workersList = workersList;
    }

    public List<String> getWorkersId() {
        return workersId;
    }
    public void setWorkersId(List<String> workersId) {
        this.workersId = workersId;
    }

    public void addApplicant(Applicant applicant){
        applicantList.add(applicant);
    }

    public String getBroadcastStatus() {
        return broadcastStatus;
    }

    public void setBroadcastStatus(String broadcastStatus) {
        this.broadcastStatus = broadcastStatus;
    }

    public Broadcast() {
    }

    public List<String> getLocationTags() {
        return locationTags;
    }

    public void setLocationTags(List<String> locationTags) {
        this.locationTags = locationTags;
    }

    @Override
    public String toString() {
        return "Broadcast{" +
                "creatorId='" + creatorId + '\'' +
                ", creatorEmail='" + creatorEmail + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", broadcastId='" + broadcastId + '\'' +
                ", broadcastName='" + broadcastName + '\'' +
                ", broadcastDescription='" + broadcastDescription + '\'' +
                ", category='" + category + '\'' +
                ", newTasks=" + newTasks +
                ", newApplicants=" + newApplicants +
                ", interestTags=" + interestTags +
                ", locationTags=" + locationTags +
                ", applicantList=" + applicantList +
                ", applicantId=" + applicantId +
                ", workersId=" + workersId +
                ", workersList=" + workersList +
                ", taskList=" + taskList +
                ", broadcastStatus='" + broadcastStatus + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creatorId);
        dest.writeString(creatorEmail);
        dest.writeString(creatorName);
        dest.writeString(broadcastId);
        dest.writeString(broadcastName);
        dest.writeString(category);
        dest.writeInt(newApplicants);
        dest.writeInt(newTasks);
        dest.writeString(broadcastDescription);
        dest.writeStringList(interestTags);
        dest.writeString(broadcastStatus);
        dest.writeStringList(workersId);
        dest.writeStringList(applicantId);
        dest.writeTypedList(applicantList);
        dest.writeTypedList(taskList);
        dest.writeTypedList(workersList);
    }

}
