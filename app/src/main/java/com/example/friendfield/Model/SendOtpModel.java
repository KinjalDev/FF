package com.example.friendfield.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOtpModel {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CSRFTokenData csrfTokenData;

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

    public CSRFTokenData getCsrfTokenData() {
        return csrfTokenData;
    }

    public void setCsrfTokenData(CSRFTokenData csrfTokenData) {
        this.csrfTokenData = csrfTokenData;
    }
}
