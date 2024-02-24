package com.example.finalproject.Classes.App;

import com.example.finalproject.Classes.User.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class App implements Serializable {
    private String appId;
    private String appName;
    private String appNameLowercase;
    private String appImagePath;
    private User appCreator;
    private long appDownloadCount;
    private String appSize;
    private ArrayList<String> appPerms;
    private double appAvgRating;
    private Double appPrice;
    private Double appDiscountPercentage; //out of 100%
    private String appMainCategory;
    private String appDescription;
    private String appApkPath;
    private Date appUploadDate;

    public App(String appName, String appImagePath, User appCreator, String appSize, ArrayList<String> appPerms, Double appPrice, Double appDiscountPercentage, String appMainCategory, Date appUploadDate) {
        this.appName = appName;
        this.appImagePath = appImagePath;
        this.appCreator = appCreator;
        this.appSize = appSize;
        this.appPerms = appPerms;
        this.appPrice = appPrice;
        this.appDiscountPercentage = appDiscountPercentage;
        this.appMainCategory = appMainCategory;
        this.appUploadDate = appUploadDate;
        this.appNameLowercase = appName.toLowerCase(Locale.ROOT);
    }

    public String getAppNameLowercase() {
        return appNameLowercase;
    }

    public void setAppNameLowercase(String appNameLowercase) {
        this.appNameLowercase = appNameLowercase;
    }

    public App() {
    }

    public Date getAppUploadDate() {
        return appUploadDate;
    }

    public void setAppUploadDate(Date appUploadDate) {
        this.appUploadDate = appUploadDate;
    }

    public double getAppAvgRating() {
        return appAvgRating;
    }

    public void setAppAvgRating(double appAvgRating) {
        this.appAvgRating = appAvgRating;
    }

    public long getAppDownloadCount() {
        return appDownloadCount;
    }

    public void setAppDownloadCount(long appDownloadCount) {
        this.appDownloadCount = appDownloadCount;
    }

    public String getAppApkPath() {
        return appApkPath;
    }

    public void setAppApkPath(String appApkPath) {
        this.appApkPath = appApkPath;
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

    public User getAppCreator() {
        return appCreator;
    }

    public void setAppCreator(User appCreator) {
        this.appCreator = appCreator;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public ArrayList<String> getAppPerms() {
        return appPerms;
    }

    public void setAppPerms(ArrayList<String> appPerms) {
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

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }
}
