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
    User getUserByEmail(String email);

    @Query("SELECT * FROM tblUser WHERE " +
            // Check if the search string is null or empty
            "(:search IS NULL OR :search = '' OR " +
            // If not, search for users whose first name contains the searched text
            Constants.USER_FIRST_NAME_KEY + " LIKE :search OR " +
            // Or whose last name contains the searched text
            Constants.USER_LAST_NAME_KEY + " LIKE :search OR " +
            // Or whose first name and last name combined match the searched text
            "(" + Constants.USER_FIRST_NAME_KEY + " || ' ' || " + Constants.USER_LAST_NAME_KEY + ") LIKE :search) " +
            "ORDER BY " +
            // If isSortedByFirstName is 1, sort by first name
            "CASE WHEN :isSortedByFirstName = 1 THEN " + Constants.USER_FIRST_NAME_KEY + " END ASC, " +
            // If isSortedByLastName is 1, sort by last name
            "CASE WHEN :isSortedByLastName = 1 THEN " + Constants.USER_LAST_NAME_KEY + " END ASC")    List<User> getUsersContainingAndSorted(String search, int isSortedByFirstName, int isSortedByLastName);
    @Insert
    long insert(User User);

    @Update
    void update(User User);

    @Delete
    void delete(User User);
}
