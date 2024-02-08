package com.example.finalproject.Classes.App;

import java.io.Serializable;

public class App implements Serializable {
    private String appId;
    private String appName;
    private String appImagePath;
    private String appCreator;
    //private long appDownloadCount;
    private long appSizeMB;
    private String appPerms;
    //private double appAvgRating;
    private Double appPrice;
    private Double appDiscountPercentage; //out of 100%
    private String appMainCategory;

    public App(String appName, String appImagePath, String appCreator, long appSizeMB, String appPerms, Double appPrice, Double appDiscountPercentage, String appMainCategory) {
        this.appName = appName;
        this.appImagePath = appImagePath;
        this.appCreator = appCreator;
        this.appSizeMB = appSizeMB;
        this.appPerms = appPerms;
        this.appPrice = appPrice;
        this.appDiscountPercentage = appDiscountPercentage;
        this.appMainCategory = appMainCategory;
    }

    public App() {
    }

    public String getAppMainCategory() {
        return appMainCategory;
    }

    public void setAppMainCategory(String appMainCategory) {
        this.appMainCategory = appMainCategory;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppImagePath() {
        return appImagePath;
    }

    public void setAppImagePath(String appImagePath) {
        this.appImagePath = appImagePath;
    }

    public String getAppCreator() {
        return appCreator;
    }

    public void setAppCreator(String appCreator) {
        this.appCreator = appCreator;
    }

    public long getAppSizeMB() {
        return appSizeMB;
    }

    public void setAppSizeMB(long appSizeMB) {
        this.appSizeMB = appSizeMB;
    }

    public String getAppPerms() {
        return appPerms;
    }

    public void setAppPerms(String appPerms) {
        this.appPerms = appPerms;
    }

    public Double getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(Double appPrice) {
        this.appPrice = appPrice;
    }

    public Double getAppDiscountPercentage() {
        return appDiscountPercentage;
    }

    public void setAppDiscountPercentage(Double appDiscountPercentage) {
        this.appDiscountPercentage = appDiscountPercentage;
    }
}
