package com.example.friendfield.Model.Verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyDataModel {
    @SerializedName("user")
    @Expose
    private VerifyUserModel verifyUserModel;
    @SerializedName("token")
    @Expose
    private VerifyTokenModel verifyTokenModel;

    public VerifyUserModel getVerifyUserModel() {
        return verifyUserModel;
    }

    public void setVerifyUserModel(VerifyUserModel verifyUserModel) {
        this.verifyUserModel = verifyUserModel;
    }

    public VerifyTokenModel getVerifyTokenModel() {
        return verifyTokenModel;
    }

    public void setVerifyTokenModel(VerifyTokenModel verifyTokenModel) {
        this.verifyTokenModel = verifyTokenModel;
    }
}
