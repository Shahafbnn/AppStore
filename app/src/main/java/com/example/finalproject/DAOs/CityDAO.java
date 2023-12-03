package com.example.finalproject.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.DatabaseClasses.City;
import com.example.finalproject.Classes.Constants;

import java.util.List;

@Dao
public interface CityDAO {

    @Query("SELECT * FROM tblCity")
    List<City> getAllCities();

    @Query("SELECT * FROM tblCity Where " + Constants.CITY_ID_KEY + " = :id")
    City getCityById(long id);

    @Query("SELECT * FROM tblCity Where " + Constants.CITY_NAME_KEY + " = :name")
    City getCityByName(String name);

    @Insert
    long insert(City city);

    @Update
    void update(City city);

    @Delete
    void delete(City city);
}
