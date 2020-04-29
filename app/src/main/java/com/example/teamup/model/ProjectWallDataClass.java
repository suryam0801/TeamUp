package com.example.teamup.model;

public class ProjectWallDataClass {

    private String Link;
    private String FileName;
    private long Time;
    private String description;
    private String ownerId;
    private String ownerName;
    private String ownerPicURL;

    public ProjectWallDataClass(String link, String fileName, long time, String description, String ownerId, String ownerName, String ownerPicURL) {
        Link = link;
        FileName = fileName;
        Time = time;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerPicURL = ownerPicURL;
    }

    public ProjectWallDataClass() {
    }

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

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }
}