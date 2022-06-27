package com.example.friendfield.Model.Profile.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileRegisterModel {

    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ProfileModel personalInfoModel;

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

    public ProfileModel getPersonalInfoModel() {
        return personalInfoModel;
    }

    public void setPersonalInfoModel(ProfileModel personalInfoModel) {
        this.personalInfoModel = personalInfoModel;
    }
}
