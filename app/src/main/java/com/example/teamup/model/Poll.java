package com.example.teamup.model;

import java.util.List;

public class Poll {
    private String Question;
    private String id;
    private List<String> options;

    public Poll(String question, List<String> options, String id) {
        Question = question;
        this.options = options;
        this.id = id;
    }

    public Poll() {

    }

    @Override
    public String toString() {
        return "Poll{" +
                "Question='" + Question + '\'' +
                ", id='" + id + '\'' +
                ", options=" + options +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
