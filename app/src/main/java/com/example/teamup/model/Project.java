package com.example.teamup.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Project implements Parcelable{

    private String creatorId;

    private String creatorEmail;

    private String creatorName;

    private String projectId;

    private String projectName;

    private String projectDescription;

    private String category;

    private int newTasks;

    private int newApplicants;

    private List<String> requiredSkills=new ArrayList<>();
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

    private String projectStatus;



    protected Project(Parcel in) {
        creatorId = in.readString();
        creatorEmail = in.readString();
        creatorName = in.readString();
        projectId = in.readString();
        projectName = in.readString();
        projectDescription = in.readString();
        newTasks = in.readInt();
        newApplicants = in.readInt();
        requiredSkills = in.createStringArrayList();
        projectStatus = in.readString();
        category = in.readString();
        applicantId = in.createStringArrayList();
        workersId = in.createStringArrayList();
        in.readTypedList(applicantList, Applicant.CREATOR);
        in.readTypedList(taskList, Task.CREATOR);
        in.readTypedList(workersList, Worker.CREATOR);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }


    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
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

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Project() {
    }

    public Project(String creatorId, String category, String creatorEmail, String creatorName, String projectId, String projectName, String projectDescription, List<String> requiredSkills, String projectStatus, List<Applicant> applicantList, List<String> applicantId, List<Worker> workersList, List<Task> TaskList, List<String> workersId, int newApplicants, int newTasks) {
        this.creatorId = creatorId;
        this.creatorEmail = creatorEmail;
        this.creatorName = creatorName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.requiredSkills = requiredSkills;
        this.projectStatus = projectStatus;
        this.applicantList = applicantList;
        this.applicantId = applicantId;
        this.workersList = workersList;
        this.taskList = TaskList;
        this.workersId = workersId;
        this.category = category;
        this.newApplicants = newApplicants;
        this.newTasks = newTasks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Project{" +
                "creatorId='" + creatorId + '\'' +
                ", creatorEmail='" + creatorEmail + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", category='" + category + '\'' +
                ", newTasks=" + newTasks +
                ", newApplicants=" + newApplicants +
                ", requiredSkills=" + requiredSkills +
                ", applicantList=" + applicantList +
                ", applicantId=" + applicantId +
                ", workersId=" + workersId +
                ", workersList=" + workersList +
                ", taskList=" + taskList +
                ", projectStatus='" + projectStatus + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creatorId);
        dest.writeString(creatorEmail);
        dest.writeString(creatorName);
        dest.writeString(projectId);
        dest.writeString(projectName);
        dest.writeString(category);
        dest.writeInt(newApplicants);
        dest.writeInt(newTasks);
        dest.writeString(projectDescription);
        dest.writeStringList(requiredSkills);
        dest.writeString(projectStatus);
        dest.writeStringList(workersId);
        dest.writeStringList(applicantId);
        dest.writeTypedList(applicantList);
        dest.writeTypedList(taskList);
        dest.writeTypedList(workersList);
    }

}
