package com.example.friendfield.Model.BusinessInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessInfoModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("subCategory")
    @Expose
    private String subCategory;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("interestedCategory")
    @Expose
    private String interestedCategory;
    @SerializedName("interestedSubCategory")
    @Expose
    private String interestedSubCategory;
    @SerializedName("active")
    @Expose
    private Boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getInterestedCategory() {
        return interestedCategory;
    }

    public void setInterestedCategory(String interestedCategory) {
        this.interestedCategory = interestedCategory;
    }

    public String getInterestedSubCategory() {
        return interestedSubCategory;
    }

    public void setInterestedSubCategory(String interestedSubCategory) {
        this.interestedSubCategory = interestedSubCategory;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
