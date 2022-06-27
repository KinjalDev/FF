package com.example.friendfield.Model.Business.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessRegisterModel {

    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private BusinessDataModel businessDataModel;

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

    public BusinessDataModel getBusinessDataModel() {
        return businessDataModel;
    }

    public void setBusinessDataModel(BusinessDataModel businessDataModel) {
        this.businessDataModel = businessDataModel;
    }
}
