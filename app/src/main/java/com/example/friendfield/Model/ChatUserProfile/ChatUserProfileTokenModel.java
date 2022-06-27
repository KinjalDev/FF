package com.example.friendfield.Model.ChatUserProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatUserProfileTokenModel {

    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ChatUserProfileModel chatUserProfileModel;

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

    public ChatUserProfileModel getChatUserProfileModel() {
        return chatUserProfileModel;
    }

    public void setChatUserProfileModel(ChatUserProfileModel chatUserProfileModel) {
        this.chatUserProfileModel = chatUserProfileModel;
    }
}
