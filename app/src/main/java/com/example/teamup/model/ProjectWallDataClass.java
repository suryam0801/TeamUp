package com.example.teamup.model;

import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

public class ProjectWallDataClass {

    private String Link;
    private long Time;
    private String description;
    private String ownerId;
    private String ownerName;
    private String pollID;
    private HashMap<String, Object> Poll;
    private boolean hasPoll;

    public ProjectWallDataClass(String link, long time, String description, String ownerId, String ownerName, String pollID, boolean hasPoll, String ownerPicURL, HashMap<String, Object> Poll) {
        Link = link;
        Time = time;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.pollID = pollID;
        this.hasPoll = hasPoll;
        this.ownerPicURL = ownerPicURL;
        this.Poll = Poll;
    }


    public ProjectWallDataClass() {
    }

    @Override
    public String toString() {
        return "ProjectWallDataClass{" +
                "Link='" + Link + '\'' +
                ", Time=" + Time +
                ", description='" + description + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", pollID='" + pollID + '\'' +
                ", Poll=" + Poll +
                ", hasPoll=" + hasPoll +
                ", ownerPicURL='" + ownerPicURL + '\'' +
                '}';
    }

    public HashMap<String, Object> getPoll() {
        return Poll;
    }

    public void setPoll(HashMap<String, Object> poll) {
        Poll = poll;
    }

    public String getPollID() {
        return pollID;
    }

    public void setPollID(String pollID) {
        this.pollID = pollID;
    }

    public boolean isHasPoll() {
        return hasPoll;
    }

    public void setHasPoll(boolean hasPoll) {
        this.hasPoll = hasPoll;
    }

    private String ownerPicURL;

    public String getOwnerPicURL() {
        return ownerPicURL;
    }

    public void setOwnerPicURL(String ownerPicURL) {
        this.ownerPicURL = ownerPicURL;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }
}