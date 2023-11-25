package com.example.finalproject.DAOs;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.Classes.User;
import com.example.finalproject.Classes.Constants;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM tblUser")
    List<User> get();

    @Query("SELECT * FROM tblUser Where " + Constants.USER_ID_KEY + " = :id")
    User get(long id);

    @Query("SELECT * FROM tblUser Where " + Constants.USER_FIRST_NAME_KEY + " = :firstName")
    List<User> get(String firstName);

    @Insert
    long insert(User User);

    @Update
    void update(User User);

    @Delete
    void delete(User User);
}
