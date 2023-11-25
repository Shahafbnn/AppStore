package com.example.finalproject.Classes;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity (tableName = "tblUser", foreignKeys = @ForeignKey(entity = City.class, parentColumns = Constants.CITY_ID_KEY, childColumns = Constants.CITY_ID_KEY, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class User {
    @ColumnInfo(name = Constants.USER_ID_KEY)
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = Constants.USER_FIRST_NAME_KEY)
    private String firstName;
    @ColumnInfo(name = Constants.USER_LAST_NAME_KEY)
    private String lastName;
    @ColumnInfo(name = Constants.USER_BIRTH_DATE_KEY)
    @TypeConverters(DateConverter.class)
    private Date birthDate;
    @ColumnInfo(name = Constants.USER_WEIGHT_KEY)
    private double weight;
    @ColumnInfo(name = Constants.USER_EMAIL_ADDRESS_KEY)
    private String email;
    @ColumnInfo(name = Constants.CITY_ID_KEY)
    private long homeCityId;
    @ColumnInfo(name = Constants.USER_HOME_ADDRESS_KEY)
    private String homeAddress;
    @ColumnInfo(name = Constants.USER_PASSWORD_KEY)
    private String password;
    @ColumnInfo(name = Constants.USER_PHONE_NUMBER_KEY)
    private String phoneNumber;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

    public long getHomeCityId() {
        return homeCityId;
    }

    public void setHomeCityId(long homeCityId) {
        this.homeCityId = homeCityId;
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
}
