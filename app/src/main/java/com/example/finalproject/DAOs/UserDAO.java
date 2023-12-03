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
    List<User> getAllUsers();

    @Query("SELECT * FROM tblUser Where " + Constants.USER_ID_KEY + " = :id")
    User getUserById(long id);

    @Query("SELECT * FROM tblUser Where " + Constants.USER_FIRST_NAME_KEY + " = :firstName")
    List<User> getUsersByFirstName(String firstName);

    @Query("SELECT * FROM tblUser Where " + Constants.USER_PHONE_NUMBER_KEY + " = :phoneNumber")
    List<User> getUsersByPhoneNumber(String phoneNumber);

    @Query("SELECT * FROM tblUser Where " + Constants.USER_EMAIL_ADDRESS_KEY + " = :email")
    List<User> getUsersByEmail(String email);

    @Insert
    long insert(User User);

    @Update
    void update(User User);

    @Delete
    void delete(User User);
}
