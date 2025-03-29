package com.bandapp.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "emergency_events", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Location.class, parentColumns = "locationId", childColumns = "locationId", onDelete = ForeignKey.CASCADE)
})
public class EmergencyEvent {
    @PrimaryKey(autoGenerate = true)
    private int eventId;

    private int userId;
    private int locationId;

    @NonNull
    private String eventType;

    private long timestamp;
    private String status;

    // Constructor
    public EmergencyEvent(int userId, int locationId, @NonNull String eventType) {
        this.userId = userId;
        this.locationId = locationId;
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis();
        this.status = "active";
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @NonNull
    public String getEventType() {
        return eventType;
    }

    public void setEventType(@NonNull String eventType) {
        this.eventType = eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}