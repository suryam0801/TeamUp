package com.example.teamup.Explore;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.teamup.ControlPanel.DisplayApplicants.Applicant;

import java.util.ArrayList;
import java.util.List;

public class Project implements Parcelable{

    private String creatorId;

    private String creatorEmail;

    private String creatorName;

    private String projectId;

    private String projectName;

    private String projectDescription;


    private List<String> requiredSkills=new ArrayList<>();
    private List<Applicant> applicantList=new ArrayList<>();
    private List<String> applicantId=new ArrayList<>();
    private List<String> workersId=new ArrayList<>();
    private List<Applicant> workersList=new ArrayList<>();
    private String projectStatus;


    protected Project(Parcel in) {
        creatorId = in.readString();
        creatorEmail = in.readString();
        creatorName = in.readString();
        projectId = in.readString();
        projectName = in.readString();
        projectDescription = in.readString();
        requiredSkills = in.createStringArrayList();
        applicantList = in.createTypedArrayList(Applicant.CREATOR);
        applicantId = in.createStringArrayList();
        workersId = in.createStringArrayList();
        workersList = in.createTypedArrayList(Applicant.CREATOR);
        projectStatus = in.readString();
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

    public List<String> getWorkersId() {
        return workersId;
    }

    public void setWorkersId(List<String> workersId) {
        this.workersId = workersId;
    }

    public List<Applicant> getWorkersList() {
        return workersList;
    }

    public void setWorkersList(List<Applicant> workersList) {
        this.workersList = workersList;
    }


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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public Project(String creatorId, String creatorEmail, String creatorName, String projectId, String projectName, String projectDescription, List<String> requiredSkills, List<Applicant> applicantList, List<String> applicantId, List<String> workersId, List<Applicant> workersList, String projectStatus) {
        this.creatorId = creatorId;
        this.creatorEmail = creatorEmail;
        this.creatorName = creatorName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.requiredSkills = requiredSkills;
        this.applicantList = applicantList;
        this.applicantId = applicantId;
        this.workersId = workersId;
        this.workersList = workersList;
        this.projectStatus = projectStatus;
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
                ", requiredSkills=" + requiredSkills +
                ", applicantList=" + applicantList +
                ", applicantId=" + applicantId +
                ", workersId=" + workersId +
                ", workersList=" + workersList +
                ", projectStatus='" + projectStatus + '\'' +
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
        dest.writeString(projectId);
        dest.writeString(projectName);
        dest.writeString(projectDescription);
        dest.writeStringList(requiredSkills);
        dest.writeTypedList(applicantList);
        dest.writeStringList(applicantId);
        dest.writeStringList(workersId);
        dest.writeTypedList(workersList);
        dest.writeString(projectStatus);
    }
\
}
