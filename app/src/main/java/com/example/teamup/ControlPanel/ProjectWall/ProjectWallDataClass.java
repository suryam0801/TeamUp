package com.example.teamup.ControlPanel.ProjectWall;

public class ProjectWallDataClass {

    private String Link;
    private String FileName;
    private long Time;

    public ProjectWallDataClass(String link, String fileName, long time) {
        Link = link;
        FileName = fileName;
        Time = time;
    }

    public ProjectWallDataClass() {
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