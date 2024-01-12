package com.example.finalproject.Classes;


import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_INITIALIZED_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;
import static com.example.finalproject.Classes.Constants.USER_ID_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.firebase.Timestamp;


public class User {

    private String id;
    private String firstName;
    private String lastName;
    private Timestamp birthDate;
    private Double weight;
    private String email;
    private String homeCityName;
    private String homeAddress;
    private String password;
    private String phoneNumber;
    private boolean isAdmin;
    private String imgSrc;

    public User() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeCityName() {
        return homeCityName;
    }

    public void setHomeCityName(String homeCityId) {
        this.homeCityName = homeCityId;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getImgSrc() {
        return imgSrc;
    }
    public Bitmap getImgBitmap(Context context){
        return StorageFunctions.getBitmapFromPath(imgSrc, context);
    }
    public Uri getImgUri(Context context){
        return StorageFunctions.getUriFromPath(imgSrc, context);
    }
    public Double getAge(){
        Date now = new Date();
        Date birth = birthDate.toDate();
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



    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String birthdateToString(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(birthDate);
    }
    public static String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
    public static Timestamp getDateFromString(String date){
        String[] dates = date.split("/");
        if(dates.length != 3) return null;
        return new Timestamp(new Date(Integer.parseInt(dates[2]) - 1900, Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[0])));    }
    public static boolean isAdmin(String phoneNumber){
        for (String s:Constants.ADMIN_PHONE_NUMBERS) {
            if (phoneNumber.equals(s)) return true;
        }
        return false;
    }
    public static boolean isPasswordConfirmed(String password, String password2){return password.equals(password2);}

    public static void addUserToSharedPreferences(User u, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).edit();
        editor.clear();
        editor.putString(USER_ID_KEY, u.getId());
        editor.putBoolean(SHARED_PREFERENCES_INITIALIZED_KEY, true);
        editor.apply();
    }
    public String getFullNameAdmin(){
        String fullName = this.getFirstName() + " " + this.getLastName();
        boolean isAdmin = User.isAdmin(this.getPhoneNumber());
        if(isAdmin) fullName += " (Admin)";
        return fullName;
    }
}
