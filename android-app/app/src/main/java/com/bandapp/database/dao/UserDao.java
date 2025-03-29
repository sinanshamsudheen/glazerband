package com.bandapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.bandapp.database.entities.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE userId = :userId")
    LiveData<User> getUserById(int userId);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<User> getFirstUser();

    @Query("DELETE FROM users")
    void deleteAllUsers();
} 