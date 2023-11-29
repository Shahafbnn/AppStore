package com.example.finalproject.DatabaseClasses;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.finalproject.Classes.User;
import com.example.finalproject.DAOs.CityDAO;
import com.example.finalproject.DAOs.UserDAO;
@Database(entities = {User.class, City.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MyDatabase extends RoomDatabase
{
    private static MyDatabase INSTANCE;
    public abstract UserDAO userDAO();
    public abstract CityDAO cityDAO();

    public static MyDatabase getInstance(Context context)
    {
        return INSTANCE == null ? createDataBase(context): INSTANCE;
    }


    private static MyDatabase createDataBase(Context context)
    {
        return (INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "DataBase").allowMainThreadQueries().build());
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }
}
