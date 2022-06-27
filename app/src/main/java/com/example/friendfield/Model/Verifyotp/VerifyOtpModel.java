package com.example.friendfield.Model.Verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpModel {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private VerifyDataModel verifyDataModel;

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

    public VerifyDataModel getVerifyDataModel() {
        return verifyDataModel;
    }

    public void setVerifyDataModel(VerifyDataModel verifyDataModel) {
        this.verifyDataModel = verifyDataModel;
    }
}
