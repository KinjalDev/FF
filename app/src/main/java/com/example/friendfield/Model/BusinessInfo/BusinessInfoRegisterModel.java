package com.example.friendfield.Model.BusinessInfo;

import com.example.friendfield.Model.Business.Register.BusinessDataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessInfoRegisterModel {

    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private BusinessInfoModel businessDataModel;

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

    public BusinessInfoModel getBusinessInfoModel() {
        return businessDataModel;
    }

    public void setBusinessInfoModel(BusinessInfoModel businessInfoModel) {
        this.businessDataModel = businessInfoModel;
    }
}
