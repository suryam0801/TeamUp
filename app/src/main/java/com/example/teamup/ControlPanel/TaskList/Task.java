package com.example.teamup.ControlPanel.TaskList;

import java.util.List;

public class Task {
    private String taskName;
    private String taskDescription;
    private String taskID;

    public Task(String taskName, String taskDescription, String taskId){
        this.taskName = taskName;
        this.taskName = taskDescription;
        this.taskID = taskId;
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
                '}';
    }
}
