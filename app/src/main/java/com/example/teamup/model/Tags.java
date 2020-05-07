package com.example.teamup.model;

import java.util.List;

public class Tags {

    private List<String> locationTags;

    private List<String> interestTags;

    public Tags(List<String> locationTags, List<String> interestTags) {
        this.locationTags = locationTags;
        this.interestTags = interestTags;
    }

    public List<String> getInterestTags() {
        return interestTags;
    }

    public void setInterestTags(List<String> interestTags) {
        this.interestTags = interestTags;
    }

    public List<String> getLocationTags() {
        return locationTags;
    }

    public void setLocationTags(List<String> locationTags) {
        this.locationTags = locationTags;
    }
}
