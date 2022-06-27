package com.example.friendfield.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CSRFTokenData {
    @SerializedName("authToken")
    @Expose
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
