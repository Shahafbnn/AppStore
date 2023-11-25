package com.example.finalproject.Classes;

public class LocationAddress {
    private String country, city, street, streetNum;

    public LocationAddress(String country, String city, String street, String streetNum) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNum = streetNum;
    }


    public static LocationAddress getLocationAddressFromString(String locationAddressStr){
        // example input: BritLand/Massachusetts/Bonk/11
        //country, city, street, streetNum;
        String[] splitAddress = locationAddressStr.split("/");
        String country, city, street, streetNum;
        country = "Unknown";
        city = "Unknown";
        street = "Unknown";
        streetNum = "Unknown";

        if(splitAddress.length == 4){
            if((boolean)LocationAddress.validateCountry(splitAddress[0])[0]) country = splitAddress[0];
            if((boolean)LocationAddress.validateCity(splitAddress[1])[0]) city = splitAddress[1];
            if((boolean)LocationAddress.validateStreet(splitAddress[2])[0]) street = splitAddress[2];
            if((boolean)LocationAddress.validateStreetNum(splitAddress[3])[0]) streetNum = splitAddress[3];
        }
        return new LocationAddress(country,city,street,streetNum);
    }

    public static LocationAddress getLocationAddressFromString(String country, String locationAddressStr){
        // example input: BritLand, Massachusetts/Bonk/11
        String[] splitAddress = locationAddressStr.split("/");
        String city, street, streetNum;
        city = "Unknown";
        street = "Unknown";
        streetNum = "Unknown";

        if(splitAddress.length == 3){
            if((boolean)LocationAddress.validateCity(splitAddress[1])[0]) city = splitAddress[0];
            if((boolean)LocationAddress.validateStreet(splitAddress[2])[0]) street = splitAddress[1];
            if((boolean)LocationAddress.validateStreetNum(splitAddress[3])[0]) streetNum = splitAddress[2];
        }
        return new LocationAddress(country,city,street,streetNum);
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

    //country, city, street, streetNum;
    public static Object[] validateCountry(String country){
        return new Object[]{true};
    }
    public static Object[] validateCity(String city){
        return new Object[]{true};
    }
    public static Object[] validateStreet(String street){
        return new Object[]{true};
    }
    public static Object[] validateStreetNum(String streetNum){
        return new Object[]{true};
    }
}
