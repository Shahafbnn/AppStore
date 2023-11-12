package com.example.finalproject.Classes;

public class LocationAddress {
    private String country, city, street, streetNum;

    public LocationAddress(String country, String city, String street, String streetNum) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNum = streetNum;
    }



    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }
}
