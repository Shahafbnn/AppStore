package com.example.finalproject.Classes.User;


import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_INITIALIZED_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;
import static com.example.finalproject.Classes.Constants.USER_ID_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.StorageFunctions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class User implements Serializable {

    private String userId;
    private String userFirstName;
    private String userLastName;
    private Date userBirthDate;
    private Double userWeight;
    private String userEmail;
    private String homeCityName; //should it be the ID?
    private String userHomeAddress;
    private String userPassword;
    private String userPhoneNumber;
    private boolean userIsAdmin;
    private String userImagePath;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public Date getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(Date userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public Double getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(Double userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getHomeCityName() {
        return homeCityName;
    }

    public void setHomeCityName(String homeCityId) {
        this.homeCityName = homeCityId;
    }

    public String getUserHomeAddress() {
        return userHomeAddress;
    }

    public void setUserHomeAddress(String userHomeAddress) {
        this.userHomeAddress = userHomeAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public boolean isUserIsAdmin() {
        return userIsAdmin;
    }

    public void setUserIsAdmin(boolean userIsAdmin) {
        this.userIsAdmin = userIsAdmin;
    }

    public String getUserImagePath() {
        return userImagePath;
    }
    public Double getAge(){
        Date now = new Date();
        Date birth = userBirthDate;
        long ageMillis = now.getTime() - birth.getTime();
        double ageYears = ageMillis / 31556952000.0;

        if (now.getMonth() < birth.getMonth()) {
            ageYears--;
        } else if (now.getMonth() == birth.getMonth()) {
            if (now.getDate() < birth.getDate()) {
                ageYears--;
            }
        }
        return Math.round(ageYears*10)/10.0;
    }



    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public String birthdateToString(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(userBirthDate);
    }
    public static String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
    public static Date getDateFromString(String date){
        String[] dates = date.split("/");
        if(dates.length != 3) return null;
        return new Date(Integer.parseInt(dates[2]) - 1900, Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[0]));    }
    public static boolean isAdmin(String phoneNumber){
        for (String s: Constants.ADMIN_PHONE_NUMBERS) {
            if (phoneNumber.equals(s)) return true;
        }
        return false;
    }
    public static boolean isPasswordConfirmed(String password, String password2){return password.equals(password2);}

    public static void addUserIdToSharedPreferences(String id, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).edit();
        editor.clear();
        editor.putString(USER_ID_KEY, id);
        editor.putBoolean(SHARED_PREFERENCES_INITIALIZED_KEY, true);
        editor.apply();
    }
    public String getFullNameAdmin(){
        String fullName = this.getUserFirstName() + " " + this.getUserLastName();
        boolean isAdmin = User.isAdmin(this.getUserPhoneNumber());
        if(isAdmin) fullName += " (Admin)";
        return fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", userBirthDate=" + userBirthDate +
                ", userWeight=" + userWeight +
                ", userEmail='" + userEmail + '\'' +
                ", homeCityName='" + homeCityName + '\'' +
                ", userHomeAddress='" + userHomeAddress + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userIsAdmin=" + userIsAdmin +
                ", userImgSrc='" + userImagePath + '\'' +
                '}';
    }
}
