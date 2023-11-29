package com.example.finalproject.DatabaseClasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.finalproject.Classes.Constants;

@Entity(tableName = "tblCity")
public class City {

    @ColumnInfo(name = Constants.CITY_ID_KEY)
    @PrimaryKey(autoGenerate = true)
    private long cityId;

    @ColumnInfo(name = Constants.CITY_NAME_KEY)
    private String cityName;

    public City() {
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
