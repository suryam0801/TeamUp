package com.example.teamup.model;

public class Applicant {

    private String projectId;

    private String applicantName;

    private String applicantEmail;

    private String userId;

    private boolean acceptedStatus;

    private String shortPitch;

    public Applicant() {
    }

    public Applicant(String projectId, String applicantName, String applicantEmail, String userId,boolean acceptedStatus, String shortPitch) {
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

    public boolean getAcceptedStatus() {
        return acceptedStatus;
    }

    public void setAcceptedStatus(boolean acceptedStatus) {
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
}
