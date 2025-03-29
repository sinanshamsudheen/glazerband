package com.bandapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.bandapp.database.entities.EmergencyContact;
import java.util.List;

@Dao
public interface EmergencyContactDao {
    @Insert
    long insert(EmergencyContact contact);

    @Update
    void update(EmergencyContact contact);

    @Delete
    void delete(EmergencyContact contact);

    @Query("SELECT * FROM emergency_contacts WHERE userId = :userId ORDER BY priority DESC")
    LiveData<List<EmergencyContact>> getContactsByUserId(int userId);

    @Query("SELECT * FROM emergency_contacts WHERE contactId = :contactId")
    LiveData<EmergencyContact> getContactById(int contactId);

    @Query("UPDATE emergency_contacts SET priority = :priority WHERE contactId = :contactId")
    void updateContactPriority(int contactId, int priority);

    @Query("DELETE FROM emergency_contacts WHERE userId = :userId")
    void deleteAllContactsForUser(int userId);
} 