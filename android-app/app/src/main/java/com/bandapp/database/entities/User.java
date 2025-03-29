package com.bandapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;

    @NonNull
    private String name;

    @NonNull
    private String phoneNumber;

    private long createdAt;
    private long lastUpdated;

    // Constructor
    public User(@NonNull String name, @NonNull String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdAt = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
        this.lastUpdated = System.currentTimeMillis();
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}