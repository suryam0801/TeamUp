package com.example.teamup;

public class Applicant {
    private String applicantName;
    private String userId;
    private String pitch;

    //constructor

    public Applicant(String applicantName, String userId, String pitch) {
        this.applicantName = applicantName;
        this.userId = userId;
        this.pitch = pitch;
    }

    //setters and getters

    public String getUserId() {
        return userId;
    }
    public String getPitch() {
        return pitch;
    }
    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName){
        this.applicantName = applicantName;
    }
    public void setuserID(String userId){
        this.userId = userId;
    }
    public void setpitch(String pitch){
        this.pitch = pitch;
    }

}
