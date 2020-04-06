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

    private String projectStatus;

    protected Project(Parcel in) {
        creatorId = in.readString();
        creatorEmail = in.readString();
        creatorName = in.readString();
        projectId = in.readString();
        projectName = in.readString();
        projectDescription = in.readString();
        requiredSkills = in.createStringArrayList();
        projectStatus = in.readString();
        applicantId = in.createStringArrayList();
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

    public Project(String creatorId, String creatorEmail, String creatorName, String projectId, String projectName, String projectDescription, List<String> requiredSkills, String projectStatus, List<Applicant> applicantList, List<String> applicantId) {
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
        dest.writeString(projectStatus);
        dest.writeStringList(applicantId);
    }

    @Override
    public String toString() {
        return "Project{" +
                "creatorId='" + creatorId + '\n' +
                ", creatorEmail='" + creatorEmail + '\n' +
                ", creatorName='" + creatorName + '\n' +
                ", projectId='" + projectId + '\n' +
                ", projectName='" + projectName + '\n' +
                ", projectDescription='" + projectDescription + '\n' +
                ", requiredSkills=" + requiredSkills +
                ", applicantList=" + applicantList +
                ", applicantId=" + applicantId +
                ", projectStatus='" + projectStatus + '\n' +
                '}';
    }
}
