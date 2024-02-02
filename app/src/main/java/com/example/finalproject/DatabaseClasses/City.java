package com.example.finalproject.DatabaseClasses;

import com.example.finalproject.Classes.Constants;

public class City {
    private String cityId;
    private String cityName;

    public City() {
    }
    //test
    public City(String cityName) {
        this.cityName = cityName;
    }
    public City(String cityName, String cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }


    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
