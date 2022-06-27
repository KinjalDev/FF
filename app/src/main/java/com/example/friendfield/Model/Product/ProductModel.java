package com.example.friendfield.Model.Product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel implements Serializable {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ProductDetailsModel productDetailsModel;

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

    public ProductDetailsModel getProductDetailsModel() {
        return productDetailsModel;
    }

    public void setProductDetailsModel(ProductDetailsModel productDetailsModel) {
        this.productDetailsModel = productDetailsModel;
    }
}
