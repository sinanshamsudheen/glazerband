package com.bandapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.bandapp.database.entities.EmergencyEvent;
import java.util.List;

@Dao
public interface EmergencyEventDao {
    @Insert
    long insert(EmergencyEvent event);

    @Update
    void update(EmergencyEvent event);

    @Delete
    void delete(EmergencyEvent event);

    @Query("SELECT * FROM emergency_events WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<EmergencyEvent>> getEventsByUserId(int userId);

    @Query("SELECT * FROM emergency_events WHERE eventId = :eventId")
    LiveData<EmergencyEvent> getEventById(int eventId);

    @Query("SELECT * FROM emergency_events WHERE status = 'active' ORDER BY timestamp DESC")
    LiveData<List<EmergencyEvent>> getActiveEvents();

    @Query("UPDATE emergency_events SET status = :status WHERE eventId = :eventId")
    void updateEventStatus(int eventId, String status);

    @Query("DELETE FROM emergency_events WHERE userId = :userId")
    void deleteAllEventsForUser(int userId);
} 