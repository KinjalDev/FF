package com.example.friendfield.Model.Story;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoryModel {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private StoryData storyData;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StoryData getStoryData() {
        return storyData;
    }

    public void setStoryData(StoryData storyData) {
        this.storyData = storyData;
    }
}
